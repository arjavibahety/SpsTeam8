var roomID = window.location.search.substr(1);
window.onload = function() {
    roomID = window.location.search.substr(1);
    getRoomDetails(roomID);
    getHeaderLinks();
    document.getElementById('chat-box').innerHTML = '<iframe width="100%" height="100%" frameborder="0" src="chat.html?' + roomID + '"></iframe>';
}

function getHeaderLinks() {
    document.getElementById('my-order-link').href = '/roomMyOrder.html?' + roomID;
    document.getElementById('all-orders-link').href = '/roomAllOrders.html?' + roomID;
}
