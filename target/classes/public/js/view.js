'use strict';

//app to draw polymorphic shapes on canvas
let app;

let socket;

//Time interval for Updating chess board: 5000ms = 5 seconds.
let update_interval = 5000;

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


    //Buttons for interacting with the game.
    $("#btn-send").click(sendMove);
    $("#btn-clear").click(clearMove);

    //Remove all the balls in case the browser is refreshed.
    //clear();

    //Send canvas dimensions to the controller.
    //canvasDims();

    //Establish an Interval to update the Ball World view.

    //setInterval(updateBoard, update_interval);

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

    switch(obj.type) {
        case "player_join":
            initialDrawBoard();
            let log = document.getElementById('scrollBox');
            let align = lightPlayerTurn ? "style=\"text-align:left\"" : "style=\"text-align:right\"";
            let turn = lightPlayerTurn ? "Player 1: " : "Player 2: ";
            log.innerHTML += "<p class='log'" + align + ">" + obj.name + " Connected<\p>";
            let blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
            blank_log.remove();
            log.scrollTop = log.scrollHeight;
            break;
            //TO DO - Act on the "start_game" message - to render the board.
        case "start_game":
            gameStarted = true; //set the game status to start playing.
            isLightPlayer = obj.lightPlayer;
            isDarkPlayer = obj.darkPlayer;
            isSpectator = obj.spectator;
            lightPlayerTurn = true;
            updateBoard_piece(obj);
            default:
            break;
    }
}

/**
 * Render the positions of all the current pieces on the board.
 * @param game
 */
function updateBoard_piece(gameMsg) {
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
        let align = lightPlayerTurn ? "style=\"text-align:left\"" : "style=\"text-align:right\"";
        let turn = lightPlayerTurn ? "Player 1: " : "Player 2: ";
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
 */
function sendMove() {
    if (moveOrigin != null && moveDestination != null) {
        //send the move to the model. A stub for now that just changes the player.
        let log = document.querySelector(".scrollBox p:nth-last-child(1)");
        log.innerHTML += " (sent)";

        //Send message.
        socket.send(moveOrigin + "," + moveDestination);

        moveOrigin = null;
        moveDestination = null;

    } else {
        alert("Must have an origin and destination move selected!");
    }
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
