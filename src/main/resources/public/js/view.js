'use strict';

//app to draw polymorphic shapes on canvas
let app;
let socket;

//Global variables re: chess board size.
let boardImgFile = "chessboard-600-rice.png";
let boardSide = 600;
let spaceLen = boardSide / 8;

//Global gameplay status
let gameStarted = false;
let lightPlayerTurn = false;
let isSpectator = false;
let isLightPlayer = false;
let isDarkPlayer = false;
let moveOrigin = null;
let moveDestination = null;
let gameID;

//Player Data
let username;

/**
 * Create the ball world app for a canvas
 * @param canvas The canvas to draw balls on
 * @returns {{dims: {width, height}, drawCircle: drawCircle, clear: clear}}
 */
function createApp(canvas) {
    let c = canvas.getContext("2d");
    let boardImg = new Image();
    boardImg.src = boardImgFile;

    /**
     * Draw a circle
     * @param x  The x location coordinate
     * @param y  The y location coordinate
     * @param radius  The circle radius
     * @param color The circle color
     */
    let drawCircle = function(x, y, radius, color) {
        c.fillStyle = color;
        c.beginPath();
        c.arc(x, y, radius, 0, 2 * Math.PI, false);
        c.closePath();
        c.fill();
    };

    /**
     * Draw a fish image.
     * @param x - fish x pos.
     * @param y - fish y pos
     * @param imgName - name of the image to draw
     * @param scale - scaling factor to scale the image by
     * @param flip - boolean whether or not the image should be flipped.
     * @param angle - How the image should be scaled.
     */
    let drawFish = function(x, y, imgName, scale, flip, angle) {
        let Img = new Image();
        Img.src = imgName;
        let height = Img.height;
        let width = Img.width;

        c.save();
        c.translate(x, y);
        c.rotate(angle);
        if (flip) c.scale(1, -1); //Flip only after rotating the canvas to the proper angle.
        c.translate(- width * scale / 2, - height * scale / 2);
        c.drawImage(Img, 0, 0, width * scale, height * scale);
        c.translate(width * scale / 2, height * scale / 2)
        c.restore();
    }

    let drawBoard = function() {
        c.drawImage(boardImg, 0, 0, boardSide, boardSide);
    }

    let drawPiece = function(imageStr, x, y) {
        let newImg = new Image();
        newImg.src = imageStr;
        let halfSide = 30;

        newImg.onload = function() {
            console.log(imageStr + " " + x - halfSide + " " + y - halfSide);
            c.drawImage(newImg, x - halfSide, y - halfSide);
        }
    }


    /**
     * Clear the canvas.
     */
    let clear = function() {
        c.clearRect(0,0, canvas.width, canvas.height);
    };

    return {
        drawCircle: drawCircle,
        drawFish: drawFish,
        drawBoard: drawBoard,
        drawPiece: drawPiece,
        clear: clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}

/**
 * Define action on window loading.
 */
window.onload = function() {
    app = createApp(document.querySelector("canvas"));

    socket = new WebSocket("ws://" + location.hostname + ":" + location.port +
        "/chess");
    socket.addEventListener('message', function (event) {
        onMessage(event);

        console.log('Message from server ', event.data);
    });

    socket.addEventListener('open', function (event) {
        onConnect(event);
    });


    //Buttons for interacting with the game.
    $("#btn-send").click(sendMove);
    $("#btn-clear").click(clearMove);
    $("#btn-send-text").click(sendChatMessage);

    //Remove all the balls in case the browser is refreshed.
    //clear();

    //Send canvas dimensions to the controller.
    //canvasDims();
};

window.addEventListener('load', (event) => {
    console.log('page is fully loaded');
});


/**
 * Event-listener wrapper to encapsulate any required event listeners.
 */
document.addEventListener('DOMContentLoaded', function () {
    let can = document.querySelector("canvas");
    can.addEventListener("click", reportClick, false);
});

/**
 * Handler for receiving all messages.
 * @param msg
 */
function onMessage(msg) {
    let obj = JSON.parse(msg.data);
    let log = document.getElementById('scrollBox');
    let blank_log;
    let connectString

    switch(obj.type) {
        case "player_join":
            initialDrawBoard();
            connectString = obj.name + " Connected";
            insertConnectionMessage(connectString);
            //log.innerHTML += "<p class='log'>" + obj.name + " Connected<\p>";
            //blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
            //blank_log.remove();
            //log.scrollTop = log.scrollHeight;
            break;
        case "spectator_join":
            if (!isLightPlayer && !isDarkPlayer) isSpectator = true;
            gameStarted = true;
            connectString = obj.name + " Connected as Spectator";
            insertConnectionMessage(connectString);
            //log.innerHTML += "<p class='log'>" + obj.name + " Connected as Spectator<\p>";
            //blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
            //blank_log.remove();
            //log.scrollTop = log.scrollHeight;
            break;
        case "start_game":
            gameStarted = true; //set the game status to start playing.
            isLightPlayer = obj.lightPlayer;
            isDarkPlayer = obj.darkPlayer;
            isSpectator = obj.spectator;
            lightPlayerTurn = obj.lightPlayerTurn;
            updateBoard_piece(obj);
            break;
        case "update_game":
            addUpdateToLog(obj.move);  //Add the update to the log before switching players.
            console.log(obj.move);
            lightPlayerTurn = obj.lightPlayerTurn;
            updateBoard_piece(obj);
            break;
        case "chat":
            insertChat(obj.content);
            break;
        case "error":
            alert(obj.content);
            let logLast = document.querySelector(".scrollBox p:nth-last-child(1)");
            logLast.remove();
            break;
        case "spectator_leave":
            //Write the message that the spectator has left. Allow gameplay to continue.
            insertConnectionMessage(obj.content);
            break;
        case "player_leave":
            //Write the message that the player has left. Gameplay will not be able to continue.
            insertConnectionMessage(obj.content);
            alert(obj.content);
            //Take action to close or redirect the browser.
            window.location = "/index.html";
        case "king_taken":
            //Write the message that the king has been taken. Gameplay will not continue.
            insertConnectionMessage(obj.content);
            alert(obj.content);
            window.location = "/index.html";
            default:
            break;
    }
}

/**
 * Helper function to insert a chat string into the log box for everyone, player and spectator.
 * @param str
 */
function insertChat(str) {
    let log = document.getElementById('scrollBox');
    log.innerHTML += "<p class='log' style='text-align: left'>" + str + "<\p>";
    log.scrollTop = log.scrollHeight;
}

function insertConnectionMessage(str) {
    let log = document.getElementById('scrollBox');
    log.innerHTML += "<p class='log'>" + str + "<\p>";
    let blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
    blank_log.remove();
    log.scrollTop = log.scrollHeight;
}


/**
 * Helper method to respond when a connection is opened.
 * We want to check the cookie for the requested data, and send a message to the server.
 * @param event
 */
function onConnect(event) {
    let role;
    console.log("Connection was just opened!");
    console.log("Cookie contents: " + document.cookie);

    //Get the username cookie
    username = document.cookie
        .split('; ')
        .find(row => row.startsWith('username='))
        .split('=')[1];

    //Get the role cookie
    role = document.cookie
        .split('; ')
        .find(row => row.startsWith('role='))
        .split('=')[1];

    gameID = document.cookie
        .split('; ')
        .find(row => row.startsWith('gameid='))
        .split('=')[1];

    //log the username in the console for debugging purposes.
    console.log("Username: " + username + "; role: " + role + "; gameID: " + gameID);

    //Send the connection message to the server.
    //We don't need the game ID because the user will have already been associated with the correct game.
    let msgJSON = {type: "join", username: username, role: role };
    let msg = JSON.stringify(msgJSON);
    socket.send(msg);
}


function addUpdateToLog(moveString) {
    if (moveString == "void") return;  //Empty position update for manual joining update of a spectator.
    let log = document.getElementById('scrollBox');
    let blank_log;
    //If own turn, we need to first remove the tentative update.
    if ((isLightPlayer && lightPlayerTurn) || (isDarkPlayer && !lightPlayerTurn)) {
        //Remove the tentative status.
        blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
        blank_log.remove();
    }

    //Add the finalized move to the log
    let style = (isLightPlayer && lightPlayerTurn) || (isDarkPlayer && !lightPlayerTurn) ?
        "style='text-align: right'" : "style='text-align: left'"
    log.innerHTML += "<p class='log' " + style + ">" + moveString + "<\p>";
    blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
    blank_log.remove();
    log.scrollTop = log.scrollHeight;
}

/**
 * Render the positions of all the current pieces on the board.
 * @param game
 */
function updateBoard_piece(gameMsg) {
    app.clear();
    app.drawBoard();
    gameMsg.lightPieces.forEach(function (obj) {
        app.drawPiece(obj.image, obj.loc.x, obj.loc.y);
    });

    gameMsg.darkPieces.forEach(function (obj) {
        app.drawPiece(obj.image, obj.loc.x, obj.loc.y);
    });
}

/**
 * Helper function to draw the initial board.
 */
function initialDrawBoard() {
    console.log("draw board");
    app.clear();
    app.drawBoard();
}

/**
 * Pass along the canvas dimensions
 */
function canvasDims() {
    $.post("/canvas/dims", {height: app.dims.height, width: app.dims.width});
}

/**
 * Clear the canvas
 */
function clear() {
    $.get("/clear");
    app.clear();
    $("#objectSelect").empty();
}

/**
 * Take Action on a click.
 */
function reportClick(e) {
    let boardPos = returnClickPosition(e);
    let log = document.getElementById('scrollBox');

    //Explicitly disallow several cases.
    if (!gameStarted) return;  //Ignore canvas clicks if the game has not started.
    if (isSpectator) return;   //Do not allow spectators to interact with the board.
    if (lightPlayerTurn && isDarkPlayer) return;   //Only allow light player to select when light player's turn.
    if (!lightPlayerTurn && isLightPlayer) return; //Only allow dark player on dark player's turn.

    if (moveOrigin == null) {
        moveOrigin = boardPos;
        let align = (lightPlayerTurn && isLightPlayer) || (!lightPlayerTurn && isDarkPlayer)
            ? "style=\"text-align:right\"" : "style=\"text-align:left\"";
        let turn = lightPlayerTurn ? "Selected: " : "Selected: ";
        log.innerHTML += "<p class='log'" + align + ">" + turn + boardPos + "<\p>";
        let blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
        blank_log.remove();
        log.scrollTop = log.scrollHeight;
    } else if (moveDestination == null) {
        moveDestination = boardPos;
        log = document.querySelector(".scrollBox p:nth-last-child(1)");
        log.innerHTML += " -> " + boardPos;
    } else {
        //Do nothing - wait for Clear move
    }
}

/**
 * Helper function to return the click position on the board
 * @param e EventListener for click on the canvas
 * @returns {string} A string with the board position.
 */
function returnClickPosition(e) {
    let X = e.clientX - e.target.getBoundingClientRect().left;
    let Y = e.clientY - e.target.getBoundingClientRect().top;
    let boardPos = String.fromCharCode(97 + (X / spaceLen)) + (8 - Math.floor(Y / spaceLen)).toString();

    //Print debugging info and log the click position
    console.log("click at: " + boardPos);
    return boardPos;
}

/**
 * Function to send the selected move to the model.
 * Note that reportClick() will only allow the moveOrigin and mostDest
 * to be populated based on correct player selecting the move.
 * So here, all we need to do is proceed if they are filled.
 *
 */
function sendMove() {
    if (isSpectator) {
        alert("You Are Just Spectating and Cannot Move");
        return;
    }

    if (moveOrigin != null && moveDestination != null) {
        //send the move to the model. A stub for now that just changes the player.
        let log = document.querySelector(".scrollBox p:nth-last-child(1)");
        log.innerHTML += " (sent)";

        //Send message.
        let msgJSON = {type: "move", fromLoc: moveOrigin, toLoc: moveDestination};
        let msg = JSON.stringify(msgJSON);
        socket.send(msg);

        moveOrigin = null;
        moveDestination = null;

    } else {
        alert("Must have an origin and destination move selected!");
    }
}

/**
 * Helper function to send a chat message to all entities (player, spectators) in the game.
 */
function sendChatMessage() {
    if (moveOrigin != null) {
        alert("Cannot Send a Message Move In Progress. Clear Move to send a Message first.");
        return;
    }
    //Anyone (a spectator or player) can send a message.
    let textValue = document.getElementById("chat-field").value;
    let msgJSON = {type: "chat", content: textValue};
    let msg = JSON.stringify(msgJSON);
    socket.send(msg);

    //clear the textbox
    document.getElementById("chat-field").value = "";
}




/**
 * Function to clear the move and remove the text from the log.
 */
function clearMove() {
    if (moveOrigin != null) {
        moveOrigin = null;
        moveDestination = null;


        let log = document.querySelector(".scrollBox p:nth-last-child(1)");
        log.remove();
    }
}
