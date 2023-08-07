# SIBS Tecnical chalange

## Pre requisites
To run the application you can choose your preferred way of running a Java application but there are a few pre-requisites. 
You should have postgress installed and create a db called test_db with the username being postgress and the password being miaumiau. You can chose to change this setting in the application.yml file.
All the tables and initial inserts will be done by liquibase. After that, all you need to do is call the endpoints. All the endpoints are self-explanatory but for the more "complex" ones follows some instructions. 

You can of course customize all the fields
### Create item
To create an item call the endpoint localhost:5000/api/item/createItem with a POST REQUEST. 
Send in the body a JSON with the following format:

{
"name": "potato"
}

### Create user
To create an item call the endpoint localhost:5000/api/user/createUser with a POST REQUEST.
Send in the body a JSON with the following format:

{
"name": "John Doe",
"email": "john.doe@example.com"
}

### Create order
To create an item call the endpoint localhost:5000/api/order/createOrder with a POST REQUEST.
Send in the body a JSON with the following format:

{
    "itemDto": {
        "id": 1
    },
    "quantity": 200,
    "userWhoCreatedOrderDto": {
        "id": 1
    }
}

quantity is the amount of items you want to order

### Create stock movement
To create an item call the endpoint localhost:5000/api/stockMovement/createStockMovement with a POST REQUEST.
Send in the body a JSON with the following format:

{
    "item": {
        "id": 1
    },
    "originalQuantity": 300
}

originalQuantity is the amount of stock being moved. You will receive as a response the updated entity with the current quantity already being less if any order has been fulfilled 