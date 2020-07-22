async function getMyRooms() {
    var response = await fetch("/myRooms");
    var responseText = await response.text();
    console.log(responseText);
    var entries = responseText.split(" ");
    let childHtmlString = "";
    for (var i = 0; i < entries.length; i++) {
        let room = entries[i];
        await firebase.database().ref('rooms/' + room).once('value', function(snapshot) {
            var snap = snapshot.val();
            console.log(snap);
        childHtmlString += `<div class="shadow-sm p-3 mb-5 bg-white rounded listing-card">
      <div class="row form-group">
        <div class="col-md-6 mb-3 mb-md-0">
          <span class="card-heading title">${snap.title}</span>
          <br />
          <span class="card-field">Postal Code: </span>
          <span class="card-value postal-code-value">
            ${snap.deliveryLocation}
          </span>
          <br />
          <span class="card-description">
            ${snap.description}
          </span>
        </div>
        <div class="col-md-6 text-right">
          <span class="card-field">Category: </span>
          <span class="card-value category-value">${snap.category}</span>
          <br />
          <span class="card-field">Delivery fee: </span>
          <span class="card-value">$${snap.deliveryFee}</span>
          <br />
          <span class="card-field">$ current orders value: </span>
          <span class="card-value">$${snap.ordersValue}</span>
          <br />
          <br /><button id="action" class="btn btn-chat" onclick="toChat('${room}')">Chat</button>
      </div>
    </div>
  </div>`;
        });
    }
    document.getElementById("listings-card-container").innerHTML = childHtmlString;
}

function toChat(roomId) {
    window.location.href = `/roomChat.html?${roomId}`;
}
