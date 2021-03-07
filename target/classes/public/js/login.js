'use strict';

/**
 * Define action on window loading.
 */
window.onload = function() {
    //Clear any cookies from the domain
    document.cookie = "username= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "gameid= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "role= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";

    //Buttons for interacting with the lobby menu
    $("#btn-login").click(authUser);
    $("#btn-guest").click(authGuest);

};

/**
 * Event Listener for taking actions when the page is fully loaded
 */
window.addEventListener('load', (event) => {
    console.log('page is fully loaded');
});

/**
 * Function Auth User: Check that username and password are both present.
 * Then send POST request to check credentials.
 */
function authUser() {
    alert("Not Yet Implemented!");
}

/**
 * Function Auth Guest: Permit the user to login as a guest.
 * Alert the user, generate random string as the username, and proceed to the lobby.
 */
function authGuest() {
    let userID;
    userID = Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5);
    alert("Proceeding as guest - random username: " + userID);

    commitUserNameAndForward(userID);
}

/**
 * Function Write Username and Forward:
 * Helper function to commit the username to the domain cookie and proceed to the game lobby.
 *
 */
function commitUserNameAndForward(userID) {
    console.log("Proceeding as userID: " + userID);
    document.cookie = "username=" + userID;
    window.location.replace("/lobby.html");
}


