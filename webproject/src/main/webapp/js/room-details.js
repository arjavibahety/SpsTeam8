async function getRoomDetails(roomID) {
    var roomResponse = await fetch(`https://summer20-sps-47.firebaseio.com/rooms/${roomID}.json`);
    var roomDetails = await roomResponse.json();

    var userRoomResponse = await fetch(`https://summer20-sps-47.firebaseio.com/UserRoom.json?orderBy=%22roomId%22&equalTo=%22${roomID}%22`);
    var userRoomData = await userRoomResponse.json();
    let numUsers = Object.keys(userRoomData).length;

    let currentUserResponse = await fetch('/username');
    let currentUser = await currentUserResponse.text();

    let roomDetailsContainer = document.getElementById("room-details-container");
    let roomDetailsString = `
    <div>
    <span class="room-details-shopName">${roomDetails.title}</span>
    <br />
    <br />
    
    <span class="room-details-header">Delivering To:</span>
    <br />
    <span class="room-details-value">${roomDetails.deliveryLocation}</span>
    <br />
    <br />
    
    <span class="room-details-header">Number of users in room:</span>
    <br />
    <span class="room-details-value">${numUsers}</span>
    <br />
    <br />
    
    <span class="room-details-header">Delivery Fee: </span>
    <br />
    <span class="room-details-value">$${roomDetails.deliveryFee}</span>
    <br/>
    <br />

    <span class="room-details-header">Minimum Order Fee: </span>
    <br />
    <span class="room-details-value">$${roomDetails.minPrice}</span>
    <br/>
    <br />
    </div>
    `;

    if (roomDetails.creator === currentUser) {
      roomDetailsString += `<div class="room-details-exit">
            <form action="/closeRoom" method="post" >
            <button type="submit" class="btn btn-danger" />Delete & Exit Room</button>
            <input type="hidden" name="roomId" value="${roomID}">
            </form>
            </div>`;
    }

    roomDetailsContainer.innerHTML = roomDetailsString;
}
