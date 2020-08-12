var parameters = window.location.search.substr(1);
var roomID = parameters.split('=')[1];
window.onload = function() {
    parameters = window.location.search.substr(1);
    roomID = parameters.split('=')[1];
    getRoomDetails(roomID);
    getAllOrders();
    getHeaderLinks();
}

async function getAllOrders() {
    let response = await fetch(`https://summer20-sps-47.firebaseio.com/orders.json?orderBy=%22roomId%22&equalTo=%22${roomID}%22`)
    let data = await response.json();
    let allOrderItems = Object.values(data);
    
    let roomResponse = await fetch(`https://summer20-sps-47.firebaseio.com/rooms/${roomID}.json`)
    let myRoomDetails = await roomResponse.json();
    
    let allOrdersContainer = document.getElementById("all-orders-container");
    if (allOrderItems.length <= 0) {
        allOrdersContainer.innerHTML = "Add an item now!";
        return;
    }
    let myOrderString = `
        <table class="table">
        <thead class="thead-light">
        <tr>
        <th scope="col">#</th>
        <th scope="col">Product</th>
        <th scope="col">Quantity</th>
        <th scope="col">$ / Quantity</th>
        <th scope="col">Total</th>
        <th scope="col"></th>
        </tr>
        </thead>
        <tbody>`;
    let total = 0;
    for (let i = 0; i < allOrderItems.length; i++) {
        productTotal = allOrderItems[i].quantity * allOrderItems[i].unitPrice;
        total += productTotal;
        myOrderString += `
    <tr>
    <form action="/myOrder" method="delete">
    <th scope="row">${i + 1}</th>
    <td>${allOrderItems[i].product}</td>
    <td>${allOrderItems[i].quantity}</td>
    <td>${allOrderItems[i].unitPrice}</td>
    <td>${productTotal}</td>
    </form>
    </tr>`;
    }
    myDeliveryFee = (
        myRoomDetails.deliveryFee
    ).toFixed(2);
    let grandTotal = total + parseFloat(myDeliveryFee);
    myOrderString += `</tbody></table>`;
    myOrderString += `
    <div class="col-12 text-center">
    <hr />  
    <span class = "my-order-delivery-fee-header">Total Order Value: </span>
    <span class = "my-order-delivery-fee-value" id = "order-value">$${total}</span><br/>
    <span class = "my-order-delivery-fee-header">Delivery fee: </span>
    <span class = "my-order-delivery-fee-value">$${myDeliveryFee}</span>
    <br />
    <hr />
    <span class = "my-order-grand-total-header">Grand Total: </span>
    <span class = "my-order-grand-total-value">$${grandTotal}</span>
    </div>`;
    allOrdersContainer.innerHTML = myOrderString;

    if (total < myRoomDetails.minPrice) {
        document.querySelector("#order-value").style.color = "red";
    } else {
        document.querySelector("#order-value").style.color = "green";
    }
}

function getHeaderLinks() {
    document.getElementById('chat-link').href = '/roomChat?roomId=' + roomID;
    document.getElementById('my-order-link').href = '/roomMyOrder?roomId=' + roomID;
}
