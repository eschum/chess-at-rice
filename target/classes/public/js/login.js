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
 * Function Auth User:
 * First: Client-side validation to make sure username and password are present.
 * Second: POST request, server-side validation that the user is valid.
 * Respond accordingly.
 */
function authUser() {
    let userID = document.getElementById("username-input").value;
    let password = document.getElementById("password-input").value;

    //First, Client-side validation. If either field empty, let the HTML default validation reminder
    //user to fill out the fields.
    if (userID === "" || password === "")
        return; //Do

    else {
        //If both fields are complete, then route the request to the server via POST request.

        //Use AJAX request - Asynchronous JavaScript and XML
        //to not reload the page - sending username and password in the background.
        $.ajax({
            type: 'POST',
            url: "/auth",
            data: { username: userID, password: password},
            success: function(data) {
                //data will store the result of whether the login was successful.
                let response = JSON.parse(data);
                if (response.auth === "true") {
                    document.getElementById("username-input").classList.add("is-valid");
                    document.getElementById("password-input").classList.remove("is-invalid");
                    document.getElementById("password-input").classList.add("is-valid");
                    commitUserNameAndForward(userID);

                } else {
                    //Invalid. Alert user.
                    document.getElementById("password-input").classList.add("is-invalid");
                    document.getElementById("username-input").value = "";
                    document.getElementById("password-input").value = "";
                }
            }});
    }
    return false; //equates to calling e.preventDefault and e.stopPropagation:
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


