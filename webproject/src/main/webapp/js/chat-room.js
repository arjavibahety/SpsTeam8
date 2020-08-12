var parameters = window.location.search.substr(1);
var roomID = parameters.split('=')[1];
window.onload = function() {
    parameters = window.location.search.substr(1);
    roomID = parameters.split('=')[1];
    getRoomDetails(roomID);
    getHeaderLinks();
    document.getElementById('chat-box').innerHTML = '<iframe width="100%" height="100%" frameborder="0" src="chat.html?' + roomID + '"></iframe>';
}

function getHeaderLinks() {
    document.getElementById('my-order-link').href = '/roomMyOrder?roomId=' + roomID;
    document.getElementById('all-orders-link').href = '/roomAllOrders?roomId=' + roomID;
}
