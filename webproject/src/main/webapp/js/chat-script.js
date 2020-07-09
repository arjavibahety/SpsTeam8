var database;
var name;

$(document).ready(function () {
    //TO-DO: Use Google to get user info
    //var profile = auth2.currentUser.get().getBasicProfile();
    //var name = profile.getGivenName();
    database = firebase.database(); 
    //Using random numbers to create different names for different users.
    //Different window = different user for now
    //Will be replaced with Google ID.
    name = "user " + Math.floor(Math.random() * Math.floor(5));

    database.ref('/messages/').on('child_added', function(snapshot) {    
        var snap = snapshot.val();
        var html = "<li class='message' id='message-" + snapshot.key + "'>";
        html += snap.time + " ";
        html += snap.sender + ": " + snap.message;
        html += "</li>";
 
        document.getElementById("messages").innerHTML += html;
    });
});

function sendMessage() {
    var message = document.getElementById("message").value;
    var date = new Date();
    var time = date.getHours() + ":" + date.getMinutes();
 
    database.ref('messages/').push().set({
        sender: name,
        message: message,
        time: time
    }, function(error) {
        if (error) {
            console.log("Write failed");
        }
    });
 
    return false;
}
