var roomID = window.location.search.substr(1);
var username = null;

window.onload = function() {
    fetchBlobstoreUrl();  
}

$(document).ready(function() {
    database = firebase.database();
    name = "user " + Math.floor(Math.random() * Math.floor(5));
    roomID = window.location.search.substring(1);
    firebase.database().ref('messages/' + roomID).on('child_added', function(snapshot) {
        var snap = snapshot.val();
        var html = "<li class='message' id='message-" + snapshot.key + "'>";
        html += "<span class='chat-user'>" + snap.user + "</span> <br />";
        if (snap.type == "text") {
            html += "<span class='chat-message'>" + snap.message + "</span> <br />";
        } else {
            html += "<a href=\"" + snap.message + "\"><img src=\"" + snap.message + "\" /></a> <br />";
        }
        html += "<span class='chat-time'>" + snap.time + "</span>";
        html += "</li>";
        let messagesContainer = document.getElementById("messages");
        messagesContainer.innerHTML += html;
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    });
});

function sendMessage() {
    let usernamePromise = null;
    
    if (username == null) {
	    usernamePromise = getUsername();
    } else {
	    usernamePromise = Promise.resolve(username);
    }

    usernamePromise.then(username => {
        let messageBox = document.getElementById("message");
        var message = messageBox.value;
        messageBox.value = "";
        var date = new Date();
        var time = hours_with_leading_zeroes(date) + ":" + minutes_with_leading_zeroes(date);

        firebase.database().ref('messages/' + roomID).push().set({
            type: "text",
            user: username,
            message: message,
            time: time
        }, function(error) {
            if (error) {
                console.log("Write failed");
            }
        });
    });
    return false;
}

function getUsername() {
    return fetch('/username').then(response => response.text()).then(response => {
        username = response;
        return response;
    });
}

function fetchBlobstoreUrl() {
    fetch('/blobstore')
        .then((response) => {
            return response.json();
        }).then((responseJson) => {
            const imageUploadUrl = responseJson.url;
            const messageForm = document.getElementById('image-form');
            messageForm.action = imageUploadUrl;
            messageForm.classList.remove('hidden');
        });
}

function minutes_with_leading_zeroes(date) {
    return (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
}

function hours_with_leading_zeroes(date) {
    return (date.getHours() < 10 ? '0' : '') + date.getHours();
}

function showUploadingSpinner() {    	
    //Disabling a text input field with regular JavaScript.
    document.getElementById("file-input").disabled = true;
    document.getElementsByClassName('upload-button')[0].innerHTML = '<i class="fa fa-spinner fa-spin" style="font-size:24px"></i>';
}