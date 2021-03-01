'use strict';
let selectedGame;
let lightPlayer;
let darkPlayer;
let userID;


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

};

/**
 * Event Listener for taking actions when the page is fully loaded
 */
window.addEventListener('load', (event) => {
    console.log('page is fully loaded');
});

/**
 * General Event listener - listens for user clicks on the table.
 */
$(document).ready(function() {
    //Event Listener for table click.
    $("#gameTable tbody tr").click(function() {
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




