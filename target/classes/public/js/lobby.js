'use strict';



/**
 * Define action on window loading.
 */
window.onload = function() {

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


$(document).ready(function() {
    $("#gameTable tbody tr").click(function() {
        let selected = $(this).hasClass("highlight");
        $("#gameTable tr").removeClass("highlight");
        if (!selected)
            $(this).addClass("highlight");
    });
});
