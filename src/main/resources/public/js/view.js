'use strict';

//app to draw polymorphic shapes on canvas
let app;

//Time interval for Updating chess board: 5000ms = 5 seconds.
let update_interval = 5000;

//Global variables re: chess board size.
let boardImgFile = "chessboard-768.png";
let boardSide = 768;
let spaceLen = boardSide / 8;

//Global gameplay status
let playerOneTurn = true;
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
        clear: clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}

/**
 * Define action on window loading.
 */
window.onload = function() {
    app = createApp(document.querySelector("canvas"));

    //Buttons for interacting with the game.
    $("#btn-send").click(sendMove);
    $("#btn-clear").click(clearMove);

    //Remove all the balls in case the browser is refreshed.
    clear();

    //Send canvas dimensions to the controller.
    canvasDims();

    //Establish an Interval to update the Ball World view.
    setInterval(updateBoard, update_interval);
};

/**
 * Event-listener wrapper to encapsulate any required event listeners.
 */
document.addEventListener('DOMContentLoaded', function () {
    let can = document.querySelector("canvas");
    can.addEventListener("click", reportClick, false);
});

/**
 * load ball at a location specified by model on the canvas
 */
function loadBall() {
    let values = $("#ball-type :selected").text();  //Load strategy type from the Strategy Dropdown.
    let type = $("#switchable-value").is(":checked") ? "true" : "false";
    let del = $("#deletable-value").is(":checked") ? "true" : "false";

    $.post("/load/ball", { strategies: values, switchable: type, deletable: del}, function (data) {
        app.drawCircle(data.loc.x, data.loc.y, data.radius, data.color);
        addListSelectElement(data.name);
        console.log("Drew " + data.name);
        }, "json");
}

/**
 * load fish at a location specified by model on the canvas
 */
function loadFish() {
    let values = $("#ball-type :selected").text();  //Load strategy type from the Strategy Dropdown.
    let type = $("#switchable-value").is(":checked") ? "true" : "false";

    $.post("/load/fish", { strategies: values, switchable: type }, function (data) {
        app.drawFish(data.loc.x, data.loc.y, data.image, data.scale, data.flip, data.rotate);
        addListSelectElement(data.name);
        console.log("Drew " + data.name);
    }, "json");
}

/**
 * Switch strategies
 */
function switchStrategy() {
    let values = $("#ball-type :selected").text();  //Load strategy type from the Strategy Dropdown.
    let selected = $("#objectSelect").val();  //String array with all the objects that are selected.
    let toSend = JSON.stringify(selected);
    console.log(selected);
    let ballCollision = $("#ball-collision-type").val();  //ball-ball collision type.
    let fishCollision = $("#fish-collision-type").val();  //fish-fish collision type.
    let ballFishCollision = $("#ball-fish-collision-type").val();  //fish-fish collision type.

    $.post("/switch", { strategies: values, selections: toSend, ballCollision: ballCollision, fishCollision: fishCollision,
    ballFishCollision: ballFishCollision}, "json");
    updateBallWorld();  //Just use our updateBallWorld function to redraw the world with new strategies.
}

/**
 *  Update the ball world.
 *  GET request will return a JSON array of all the balls to draw.
 *  Clear the canvas and redraw all the balls.
 */
function updateBallWorld() {
    let sel = document.getElementById('objectSelect');
    let updateList = false;
    console.log("Update");
    $.get("/update", function(data) {
        app.clear();    //Clear after we get word back from the server.

        console.log("Number of balls:" + data.length + "Multi-select length" + sel.length);
        updateList = data.length !== sel.length
        if (updateList) $("#objectSelect").empty();


        //Iterate through each object, adding list entry if necessary.
        data.forEach(function (obj) {
            if (obj.viewRenderType === "ball") app.drawCircle(obj.loc.x, obj.loc.y, obj.radius, obj.color);
            else if (obj.viewRenderType === "fish") app.drawFish(obj.loc.x, obj.loc.y, obj.image, obj.scale,
                obj.flip, obj.rotate);
            //If we emptied the list (due to an explosion), need to re-populate.
            if (updateList) addListSelectElement(obj.name);
        });
    }, "json");
}

function updateBoard() {
    //Change player turn. Currently doing this on a timer. Eventually do when message received.
    //Update and re-draw board
    console.log("update");
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
 * DeleteObj - call the endpoint to delete subset of objects that are selected.
 */
function deleteObj() {
    let selBox = $("#objectSelect");
    let selected = selBox.val();  //String array with all the objects that are selected.
    let toSend = JSON.stringify(selected);
    console.log(selected);
    $.post("/remove", { selections: toSend }, "json");

    //Clear the list of the items we are attempting to delete.
    selBox.find('option:selected').remove();

    //Re-adjust the list box length
    selBox.size = selBox.length > 12 ? 12 : selBox.length;
}

/**
 * Add List Select Element - helper function to add a list element when an object is added.
 * @param name
 */
function addListSelectElement(name) {
    //Obtain the Document select multi object and add the list item
    let sel = document.getElementById('objectSelect');
    let option = document.createElement("option");
    sel.options.add(option);
    option.text = name;
    option.value = name;

    //Adjust the size of the selection list
    sel.size = sel.length > 12 ? 12 : sel.length;
    console.log(sel.length);

    //Return - return false for JavaScript to add the item
    return false;
}

/**
 * Take Action on a click.
 */
function reportClick(e) {
    let boardPos = returnClickPosition(e);
    let log = document.getElementById('scrollBox');

    if (moveOrigin == null) {
        moveOrigin = boardPos;
        let align = playerOneTurn ? "style=\"text-align:left\"" : "style=\"text-align:right\"";
        let turn = playerOneTurn ? "Player 1: " : "Player 2: ";
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
        //would need to verify move correctness, etc.

        playerOneTurn = !playerOneTurn;
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

        //TODO - Clear the text of move sent out of the list box.
        //let log = document.querySelector(".scrollBox p");
        //let blank_log = document.querySelector(".scrollBox p:nth-last-child(1)");
        let log = document.querySelector(".scrollBox p:nth-last-child(1)");

        log.remove();
        //blank_log.remove();
    }




}
