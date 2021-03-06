'use strict';
let selectedGame;
let lightPlayer;
let darkPlayer;
let userID;

//Set the update interval for 5 seconds
//otherwise the rows will act erratic when clicked.
let updateInterval = 5000;


/**
 * Define action on window loading.
 */
window.onload = function() {

    //For now, generate a random string for user ID.
    userID = Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5);

    //Buttons for interacting with the lobby menu
    $("#btn-new").click(newGame);
    $("#btn-join").click(joinGame);
    $("#btn-leave").click(leaveLobby);

    refreshTable(); //initialize the table with current values

    //And then set the table to update every second.
    setInterval(refreshTable, updateInterval);
};

/**
 * Event Listener for taking actions when the page is fully loaded
 */
window.addEventListener('load', (event) => {
    console.log('page is fully loaded');
});

/**
 * Event Listener for table clicks.
 * Make it general - jquery document.on(); such that dynamically added
 * rows can also respond.
 */
$(document).on("click", "tr", function(e) {
    let selected = $(this).hasClass("highlight");
    $("#gameTable tr").removeClass("highlight");
    selectedGame = null;    //Reset the game selector to have not selected a game.
    lightPlayer = null;
    darkPlayer = null;
    if (!selected) {
        //Add highlighting
        $(this).addClass("highlight");
        //Get the Game ID, playerOne, and playerTwo.
        //We will use playerOne and playerTwo to determine if the game will allow for spectators.
        selectedGame = $(this).find(".gameid").text();
        lightPlayer = $(this).find(".lightplayer").text();
        darkPlayer = $(this).find(".darkplayer").text();
    }
});

/**
 * Function: New Game
 * POST request to have the controller / model initiate a new game with this user's session as light player.
 */
function newGame() {
    console.log("NEW GAME");
    console.log("User ID: " + userID);

    //Post to the server with our intent to create a new game, and the user name.
    $.post("/new", { username: userID }, function(data) {
        //Set Cookies - user ID and game ID - later this will include / be replaced with an access token.
        document.cookie = "username=" + userID;
        document.cookie = "gameid=" + data.gameid;
        document.cookie = "role=lightPlayer";
        window.location.replace("/match.html");  //use replace method so that browsing history is not appended
        //To save from infinite "back" loop
    }, "json");
}

function refreshTable() {
    $.post("/refresh", function(data) {
        //Once we receive a response from the server, clear the table.
        //TODO - clear the tbody of the gameTable tag.

        $("#gameTable > tbody").html(""); //clear the table if any rows should be present
        //data will be an array of game data - note, it is not an array of Game objects.
        //Loop through each game and add to table
        data.forEach(function(game) {
            //Add the HTML for the table
            let darkPlayer = game.darkPlayer === 'NULL' ? "" : game.darkPlayer

            let row = "<tr>"
                .concat("<td class='gameid'>" + game.gameID + "</td>")
                .concat("<td class='lightplayer'>" + game.lightPlayer + "</td>")
                .concat("<td class='darkplayer'>" + darkPlayer + "</td>")
                .concat("<td class='time'>" + "" + "</td>")
                .concat("<td class='score'>" + "" + "</td>")
                .concat("</tr>");

            // table.innerHTML +=

            $('#gameTable').find('tbody').append(row);
            //TODO - Score and time.
            console.log(game.gameID + ", " + game.lightPlayer + ", " + game.darkPlayer);
        });
    }, "json");
}

/**
 * Function: JOin Game
 * Join the selected game.
 * Will join as Dark Player if there is not a dark player currently.
 * Otherwise, will join as a spectator.
 */
function joinGame() {
    if (selectedGame == null) {
        alert("No Game Selected. Please Select a Game.");
        return;
    }
    console.log("JOIN GAME");
    console.log("User ID: " + userID);
    console.log("Selected Game: " + selectedGame);
    console.log("Light Player: " + lightPlayer);
    console.log("Dark Player: " + darkPlayer);
}

/**
 * Function: Leave Lobby
 * Helper Function to close the window.
 * Could add more advanced signout, cookie clearing, etc.
 */
function leaveLobby() {
    window.close();
}




