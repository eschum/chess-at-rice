'use strict';
//app to draw polymorphic shapes on canvas
let app;
let socket;

//Global variables re: chess board size.
let boardImgFile = "chessboard-480-rice.png";
let boardSide = 480;
let spaceLen = boardSide / 8;
let mouseDelaySetting = 3;
let mouseMoveInterval = mouseDelaySetting;

//Global gameplay status
let gameStarted = false;
let lightPlayerTurn = false;
let isSpectator = false;
let isLightPlayer = false;
let isDarkPlayer = false;
let moveOrigin = null;
let moveDestination = null;
let gameID;
let squares = new Array();
let pieces = new Array();
let squareImages = new Array();
let pieceImages = new Array();


//Player Data
let username;

/**
 * Create the ball world app for a canvas
 * @param canvas The canvas to draw balls on
 *
 */
function createApp(canvas) {
    let c = canvas.getContext("2d");
    let boardImg = new Image();
    boardImg.src = boardImgFile;


    let drawBoard = function() {
        c.drawImage(boardImg, 0, 0, boardSide, boardSide);
    }

    let drawPiece = function(imageStr, x, y) {
        let newImg = new Image();
        newImg.src = imageStr;
        let halfSide = 30;

        newImg.onload = function() {
            c.drawImage(newImg, x - halfSide, y - halfSide);
        }

        newImg.style.zIndex = "1";
    }

    let drawSquare = function(imageStr, x, y) {
        console.log("x: " + x);
        console.log("y: " + y);
        let newImg = new Image();
        newImg.src = imageStr;
        let halfSide = spaceLen / 2;

        let xCoord, yCoord;

        switch (x) {
            case 30:
                xCoord = 3;
                break;
            case 90:
                xCoord = 62;
                break;
            case 150:
                xCoord = 122;
                break;
            case 210:
                xCoord = 181;
                break;
            case 270:
                xCoord = 240;
                break;
            case 330:
                xCoord = 299;
                break;
            case 390:
                xCoord = 359;
                break;
            case 450:
                xCoord = 418;
                break;
            default:
                break;
        }

        switch (y) {
            case 30:
                yCoord = 3;
                break;
            case 90:
                yCoord = 62;
                break;
            case 150:
                yCoord = 122;
                break;
            case 210:
                yCoord = 181;
                break;
            case 270:
                yCoord = 240;
                break;
            case 330:
                yCoord = 299;
                break;
            case 390:
                yCoord = 359;
                break;
            case 450:
                yCoord = 418;
                break;
            default:
                break;
        }

        newImg.onload = function() {
            c.drawImage(newImg, xCoord, yCoord);
        }
    }

    let loadAndDrawImg = function(imageStr, x, y) {
        let newImg = new Image();
        newImg.src = imageStr;
        let halfSide = 30;

        newImg.onload = function() {
            c.drawImage(newImg, x - halfSide, y - halfSide);
        }

        pieceImages.push(newImg);
    }

    let drawImgOnly = function(Img, x, y) {
        c.drawImage(Img, x - 30, y - 30);
    }

    /**
     * Function: Load and Draw Square
     * Draws the square that will cover the board.
     * @param imageStr
     * @param x
     * @param y
     */
    let loadAndDrawSquare = function(imageStr, x, y) {
        console.log("x: " + x);
        console.log("y: " + x);

        let newImg = new Image();
        newImg.src = imageStr;
        let halfSide = 30;

        let xCoord, yCoord;

        switch (x) {
            case 30:
                xCoord = 3;
                break;
            case 90:
                xCoord = 62;
                break;
            case 150:
                xCoord = 122;
                break;
            case 210:
                xCoord = 181;
                break;
            case 270:
                xCoord = 240;
                break;
            case 330:
                xCoord = 299;
                break;
            case 390:
                xCoord = 359;
                break;
            case 450:
                xCoord = 418;
                break;
            default:
                break;
        }

        switch (y) {
            case 30:
                yCoord = 3;
                break;
            case 90:
                yCoord = 62;
                break;
            case 150:
                yCoord = 122;
                break;
            case 210:
                yCoord = 181;
                break;
            case 270:
                yCoord = 240;
                break;
            case 330:
                yCoord = 299;
                break;
            case 390:
                yCoord = 359;
                break;
            case 450:
                yCoord = 418;
                break;
            default:
                break;
        }

        //2 + x - halfSide - 0.5 * x / 60
        //2 + y - halfSide- 0.5 * y / 60

        newImg.onload = function() {
            c.drawImage(newImg, xCoord, yCoord);
        }

        squareImages.push(newImg);
    }


    let drawSquareOnly = function(Img, x, y) {
        let halfSide = spaceLen / 2;

        let xCoord, yCoord;

        switch (x) {
            case 30:
                xCoord = 3;
                break;
            case 90:
                xCoord = 62;
                break;
            case 150:
                xCoord = 122;
                break;
            case 210:
                xCoord = 181;
                break;
            case 270:
                xCoord = 240;
                break;
            case 330:
                xCoord = 299;
                break;
            case 390:
                xCoord = 359;
                break;
            case 450:
                xCoord = 418;
                break;
            default:
                break;
        }

        switch (y) {
            case 30:
                yCoord = 3;
                break;
            case 90:
                yCoord = 62;
                break;
            case 150:
                yCoord = 122;
                break;
            case 210:
                yCoord = 181;
                break;
            case 270:
                yCoord = 240;
                break;
            case 330:
                yCoord = 299;
                break;
            case 390:
                yCoord = 359;
                break;
            case 450:
                yCoord = 418;
                break;
            default:
                break;
        }

        //x - halfSide
        //y - halfSide
        c.drawImage(Img, xCoord, yCoord);
    }


    /**
     * Clear the canvas.
     */
    let clear = function() {
        c.clearRect(0,0, canvas.width, canvas.height);
    };

    return {
        drawBoard: drawBoard,
        drawPiece: drawPiece,
        drawSquare: drawSquare,
        loadAndDrawImg: loadAndDrawImg,
        drawImgOnly: drawImgOnly,
        loadAndDrawSquare: loadAndDrawSquare,
        drawSquareOnly: drawSquareOnly,
        clear: clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}

/**
 * Define action on window loading.
 */
window.onload = function() {
    app = createApp(document.querySelector("canvas"));

    //If deploying, need wss instead of ws.
    //If testing on localhost, use ws.
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
    //$("#btn-send").click(sendMove);  //Send the move right away instead. Can enable this for testing.
    $("#btn-clear").click(clearMove);
    $("#btn-draw").click(requestDraw);
    $("#btn-resign").click(requestResign);
    $("#btn-send-text").click(sendChatMessage);

    /*
    Send a heartbeat message to the server every 10 seconds.
     */
    let heartbeatInterval = 10000;
    setInterval(sendHeartBeat, heartbeatInterval);
};

/**
 * Event listener to take action when the page is fully loaded.
 */
window.addEventListener('load', (event) => {
    console.log('page is fully loaded');
});

/**
 * Event-listener wrapper to encapsulate any required event listeners.
 */
document.addEventListener('DOMContentLoaded', function () {
    let can = document.querySelector("canvas");
    can.addEventListener("click", reportClick, false);
    can.addEventListener("mousemove", moveMouse, false);
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
            document.getElementById("turn-banner").innerHTML =
                "It is <b>" + obj.turnName + "'s</b> turn";
            updateBoard_piece(obj);
            break;
        case "update_game":
            addUpdateToLog(obj.move);  //Add the update to the log before switching players.
            console.log(obj.move);
            lightPlayerTurn = obj.lightPlayerTurn;
            document.getElementById("turn-banner").innerHTML =
                "It is <b>" + obj.turnName + "'s</b> turn";
            updateBoard_piece(obj);
            break;
        case "chat":
            insertChat(obj.content);
            break;
        case "error":
            alert(obj.content);
            let logLast = document.querySelector(".scrollBox p:nth-last-child(1)");
            logLast.remove();
            //reset piece positions.
            resetPiecePositions();
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
            break;
        case "king_taken":
            //Write the message that the king has been taken. Gameplay will not continue.
            insertConnectionMessage(obj.content);
            alert(obj.content);
            window.location = "/index.html";
            break;
        case "heartbeat_response":
            console.log("Heartbeat response received");
            break;
        case "resignation":
            insertConnectionMessage(obj.content);
            alert(obj.content);
            window.location = "/index.html";
            break;
        case "draw_request_from_server":
            receiveDrawRequest(obj.content);
            break;
        case "draw_accepted":
            insertConnectionMessage(obj.content);
            alert(obj.content);
            window.location = "/index.html";
            break;
        case "draw_denied":
            insertConnectionMessage(obj.content);
            alert(obj.content);
            //Play continues.
            break;
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

/**
 * Print a string to the center of the log box. Used for connections and disconnections.
 * @param str A message string, likely about a connection or disconnection.
 */
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

    let banner = document.getElementById("connected-banner");
    banner.innerHTML = "Connected as <b>" + username + "</b>";
}

/**
 * Write to the match log record when a move is happening.
 * This is called after the update message is received from the server.
 * @param moveString
 */
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
 * @param gameMsg The Websockets message from the server with an update of the game's condition.
 */
async function updateBoard_piece(gameMsg) {
    app.clear();
    app.drawBoard();
    squares = [];
    pieces = [];
    squareImages = [];
    pieceImages = [];

    //Load and Draw the "highlighted previous piece move" squares
    //Note: Need to check the type first; so we short circuit and never look up the fromLoc field.
    console.log(gameMsg.type + " " + gameMsg.fromLoc)
    if (gameMsg.type === "update_game" && gameMsg.fromLoc !== undefined) {
        //draw the location prior to the move.

        app.loadAndDrawSquare("selected_square.png", calcBoardX(gameMsg.fromLoc), calcBoardY(gameMsg.fromLoc));
        squares.push(["selected_square.png", calcBoardX(gameMsg.fromLoc), calcBoardY(gameMsg.fromLoc)]);

        //draw the location after the move.
        squares.push(["selected_square.png", calcBoardX(gameMsg.toLoc), calcBoardY(gameMsg.toLoc)]);
        app.loadAndDrawSquare("selected_square.png", calcBoardX(gameMsg.toLoc), calcBoardY(gameMsg.toLoc));

    }

    //Load and draw the pieces
    gameMsg.lightPieces.forEach(function (obj) {
        //app.drawPiece(obj.image, calcBoardX(obj.boardLoc), calcBoardY(obj.boardLoc));
        pieces.push([obj.image, calcBoardX(obj.boardLoc), calcBoardY(obj.boardLoc), obj.boardLoc]);
        app.loadAndDrawImg(obj.image, calcBoardX(obj.boardLoc), calcBoardY(obj.boardLoc));
    });

    gameMsg.darkPieces.forEach(function (obj) {
        //app.drawPiece(obj.image, calcBoardX(obj.boardLoc), calcBoardY(obj.boardLoc));
        app.loadAndDrawImg(obj.image, calcBoardX(obj.boardLoc), calcBoardY(obj.boardLoc));
        pieces.push([obj.image, calcBoardX(obj.boardLoc), calcBoardY(obj.boardLoc), obj.boardLoc]);
    });
}

/**
 * Function: Calc Board X
 * Helper function to calculate the canvas x coordinate based on a chess notation string.
 * @param pos The chess notation string of the piece.
 * Note: spaceLen = boardSide / 8. boardSize is length of the entire board.
 * @returns {number} The x coordinate of the chess piece image.
 */
function calcBoardX(pos) {
    let horizontal = pos.charCodeAt(0) - 97;
    if (isDarkPlayer) horizontal = 7 - horizontal;  //Invert the piece view if the player is darkPlayer.
    return horizontal * spaceLen + spaceLen / 2;
}

/**
 * Function: Calc Board Y
 * Helper function to calculate the canvas y coordinate based on a chess notation string.
 * @param pos The chess notation string of the piece.
 * Note: spaceLen = boardSide / 8. boardSize is length of the entire board.
 * @returns {number} The y coordinate of the chess piece image.
 */
function calcBoardY(pos) {
    let vertical = pos.charCodeAt(1) - 48;
    if (isDarkPlayer) vertical = 8 - vertical + 1;  //Invert the piece view if the player is darkPlayer.
    return (8 - vertical) * spaceLen + spaceLen / 2;
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

        //Send the move right away!
        sendMove();

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
    let horizontal = Math.floor(X / spaceLen);
    let vertical = 8 - Math.floor(Y / spaceLen);

    /*
    If the player is darkPlayer, the board display is inverted.
    Thus, we need to also invert the clicked chess notation position.
     */
    if (isDarkPlayer) {
        horizontal = 7 - horizontal;
        vertical = 8 - vertical + 1;
    }
    let boardPos = String.fromCharCode(97 + horizontal) + vertical.toString();

    //Print debugging info and log the click position
    console.log("click at: " + boardPos);
    return boardPos;
}

let X, Y;
let needForRAF = true;

function moveMouse(e) {
    if (moveOrigin == null || moveDestination != null || mouseMoveInterval-- > 0) return;  //take no action if no move is selected.

    X = e.clientX - e.target.getBoundingClientRect().left;
    Y = e.clientY - e.target.getBoundingClientRect().top;

    if (needForRAF) {
        needForRAF = false;
        requestAnimationFrame(mouseMoveUpdateCanvas);
    }

}

function mouseMoveUpdateCanvas(timestamp) {
    needForRAF = true;
    //mouseMoveInterval = mouseDelaySetting;
    //Re-draw the board, squares, and pieces
    app.clear();
    app.drawBoard();

    let j = 0;
    squares.forEach(function (obj) {
        app.drawSquareOnly(squareImages[j++], obj[1], obj[2]);
        //app.drawSquare(obj[0], obj[1], obj[2]);
    });

    let i = 0;
    pieces.forEach(function (obj) {
        if (obj[3] != moveOrigin)
            //app.drawPiece(obj[0], obj[1], obj[2]);
            app.drawImgOnly(pieceImages[i], obj[1], obj[2])
        else
            app.drawImgOnly(pieceImages[i], X , Y);
        i++;
    });
}

function resetPiecePositions() {
    //mouseMoveInterval = mouseDelaySetting;
    //Re-draw the board, squares, and pieces
    app.clear();
    app.drawBoard();

    let j = 0;
    squares.forEach(function (obj) {
        app.drawSquareOnly(squareImages[j++], obj[1], obj[2]);
        //app.drawSquare(obj[0], obj[1], obj[2]);
    });

    let i = 0;
    pieces.forEach(function (obj) {
        app.drawImgOnly(pieceImages[i++], obj[1], obj[2])
    });
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
    if (!trueIfTurn()) return;  //Only permit this functionality if it is the user's turn.

    if (moveOrigin != null) {
        moveOrigin = null;
        moveDestination = null;

        let log = document.querySelector(".scrollBox p:nth-last-child(1)");
        log.remove();

        resetPiecePositions();  //reset the drawing of the piece positions.
    }
}

/**
 * Function: Send Message
 * Helper function to send a message of a given type.
 * @param str
 */
function sendMessage(str) {
    let msgJSON = {type: str};
    let msg = JSON.stringify(msgJSON);
    socket.send(msg);
}


/**
 * Function: Send Heart Beat
 * Helper function to send a heartbeat message to the server.
 * Currently this function sends no other parameters, but could be
 * expanded to sync a game clock, provide other status updates, etc.
 */
function sendHeartBeat () {
    sendMessage("heartbeat");
}

/**
 * Function: Resign
 * Send message to the server that this user is requesting a resign.
 * Further alerts to the player are handled when the answer message
 * is received.
 */
function requestResign () {
    if (!trueIfTurn()) return;
    sendMessage("request_resign")
}

/**
 *  Function: Request Draw
 *  Send a message to the server in request of a draw.
 *  Can only do this when it is your turn.
 *  Further alerts to the player are handled when the answer message
 *  is received.
 */
function requestDraw() {
    if (!trueIfTurn()) return;
    sendMessage("request_draw")
}

/**
 * Function: Receive Draw Request
 * Respond when a draw request is sent from the server.
 * @param str The message to display in the confirm dialog.
 */
function receiveDrawRequest(str) {
    if (confirm(str)) {
        //If the user selects yes, then agree.
        sendMessage("draw_agree");

    } else {
        //If the user instead cancels.
        sendMessage("draw_deny");
    }
}

/**
 * Function: True If Turn
 * Helper function to check whether it is the user's turn or not.
 * @returns {boolean}
 */
function trueIfTurn() {
    if (isDarkPlayer && !lightPlayerTurn || isLightPlayer && lightPlayerTurn)
        return true;
    else if (isDarkPlayer || isLightPlayer) {
        alert("Can only use this functionality when it is your turn!");
        return false;
    } else {
        alert("You are only spectating.");
        return false;
    }
}

