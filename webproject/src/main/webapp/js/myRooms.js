async function getListings() {
    let cardsContainer = document.getElementById("listings-card-container");
    let childHtmlString = "";
    var response = await fetch("https://summer20-sps-47.firebaseio.com/rooms.json");
    var data = await response.json();
    var entries = Object.entries(data);
    for (var i = 0; i < entries.length; i++) {
        let room = entries[i];
        var roomData = room[1];
        let isRoomJoinedResponse = await fetch("/userCheck?roomId=" + room[0]);
        let res = await isRoomJoinedResponse.text();
        if (roomData.isOpen && res === "true") {
            childHtmlString += `<div class="shadow-sm p-3 mb-5 bg-white rounded listing-card">
                    <div class="row form-group">
                    <div class="col-md-6 mb-3 mb-md-0">
                    <span class="card-heading title">${roomData.title}</span>
                    <br />
                    <span class="card-field">Postal Code: </span>
                    <span class="card-value postal-code-value">
                        ${roomData.deliveryLocation}
                    </span>
                    <br />
                    <span class="card-description">
                        ${roomData.description}
                    </span>
                    </div>
                    <div class="col-md-6 text-right">
                    <span class="card-field">Category: </span>
                    <span class="card-value category-value">${roomData.category}</span>
                    <br />
                    <span class="card-field">Delivery fee: </span>
                    <span class="card-value">$${roomData.deliveryFee}</span>
                    <br />
                    <br />`;

            childHtmlString += `<button id="action" class="btn btn-chat" onclick="toChat('${room[0]}')">Chat</button>`;
            childHtmlString += `</div></div></div>`;
        }        
    }

    cardsContainer.innerHTML = childHtmlString;
}

async function getUserCheck(roomId) {
    let isRoomJoinedResponse = await fetch("/userCheck?roomId=" + roomId);
    let res = await isRoomJoinedResponse.text();
    console.log("Check: " + res);
    return res;
}

function toChat(roomId) {
    window.location.href = `/roomChat?roomId=${roomId}`;
}
