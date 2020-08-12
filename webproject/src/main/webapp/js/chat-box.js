var parameters = window.location.search.substr(1);
var roomID = parameters.split('=')[1];
var username = null;
window.onload = function() {
    fetchBlobstoreUrl();
    fetchMessages();
}

async function fetchMessages() {
    const roomId = window.location.search.substring(1);
    fetch("/fetchMessages?roomId=" + roomId).then(response => response.json()).then(response => {
        let messagesContainer = document.getElementById("messages");
        messagesContainer.innerHTML = "";
        for (let key in response.messages) {
            const newMessage = response.messages[key];
            let html = "<li class='message' id='message-" + key + "'>";
            html += "<span class='chat-user'>" + newMessage.user + "</span> <br />";
            if (newMessage.type == "text") {
                html += "<span class='chat-message'>" + newMessage.message + "</span> <br />";
            } else {
                html += "<a href=\"" + newMessage.message + "\"><img src=\"" + newMessage.message + "\" /></a> <br />";
            }

            let date = new Date(parseInt(newMessage.time));
            let minutes = minutes_with_leading_zeroes(date);
            let hours = hours_with_leading_zeroes(date);
            let day = date.getDate();
            let month = getMonth(date);

            html += `<span class='chat-time'>${day} ${month} ${hours}:${minutes}</span>`;
            html += "</li>";
            messagesContainer.innerHTML += html;
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }
    });
}

function sendMessage() {
    const roomId = window.location.search.substring(1);
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
        $.ajax({
            type: 'POST',
            url: "/sendTextMessage",
            data: { "user": username, "message": message, "roomId": roomId},
            success: function(msg) {
                fetchMessages();
            },
            error: function(msg) {
                window.alert("Something went wrong!");
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
        .then(response => response.text())
        .then((imageUploadUrl) => {
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

function getMonth(date) {
    let months = ["January", "Feburary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    return months[date.getMonth()];
}

function showUploadingSpinner() {    	
    //Disabling a text input field with regular JavaScript.
    document.getElementById("file-input").disabled = true;
    document.getElementsByClassName('upload-button')[0].innerHTML = '<i class="fa fa-spinner fa-spin" style="font-size:24px"></i>';
}
