# buy-01
## Development
### Latest merges
#### Spring cloud init
##### Overview
Here we initialised the Spring Cloud API gateway which allows us to have a main endpoint for making HTTP requests to different microservices, this also serves as centralised security for the entire application (we can add particular ones for microservices if needed) as well as handled token management.
In addition there has been a rework of the auth service. It now handles account creation, whereas the user service will be handling more user profile matters, so no credentials etc..
This was the most streamlined way, for the scenario where we want to hold session data in the auth database, for example for analytics or for making advanced token/account related operations.
The main motive however for this solution was to be able to handle tokens of e.g. deleted accounts as smoothly as possible. The main drawback with this architecture is that there will be a lot of HTTP requests made towards the auth service to check that the JWT represents an account that is currently valid.
Other strategies could be token blacklisting, which would reek the most benefits of the JWT statelessness, however I did not manage to understand it properly yet in order to implement it, so this is what we got so far but we can look into that in the future.
##### Problems and visions
- Implement blacklisting tokens to reduce HTTP calls that are just to check if token represents a real account.
- Hide keys in secrets or handle this in another way
- Add logging :D
- Not sure at which level to implement validations (gateway or individual services?)
  - At gateway reduces unnecessary requests to other microservices
  - Individual services more control and also works for Kafka driven changes
  - So maybe both :D
#### Basic CRUD endpoints
##### Overview
Here we complete all the basic CRUD operations for the main microservices i.e. auth, product, user, and media along with restrictions based on authentication and role.
##### Problems and visions
- Same as previous.
- Next add microservices ~~for orders,~~ ~~notifications~~, and messages.
- Media make request to hosting service in backend (includes validation).
- ~~Delete user should delete associated products.~~
- ~~Delete product should delete associated media.~~
- ~~This was done pretty badly so improve ^.~~
#### Notification service init
##### Overview
Here we initialise the basic setup for the notifications service, including CRUD and we also introduce delete cascades accross all services.
##### Problems and visions
- ~~Implement asynchronous communication in services to send notifications using Kafka (mayeb after all services and CRUDs are setup).~~
- ~~Implement SecurityConfig in each MS that parses the JWT and populates the SecurityContext (this will allow us to use principal in @PreAuthorize).~~
#### Order service init
##### Overview
Here we initialize the order microservice and client discoverable.
The CRUD endpoints needed: every user gets an order entity at account creation and it has different possible statuses. For now order can be cancelled (emptied) and we can add or remove items.
Kafka: When a user orders a quantity of a product, the product inventory is asynchronously upadted with a kafka message.
##### Problems and visions
- ~~Kafka messages to notify, for example: about a product available quantity reaching 0 (to the user who posted the product). What else can we come up with? This is just for fun I guess lol but would be nice to have a somewhat complete e-commerce page at the end of this.~~
- ~~We can't yet complete order.~~
- ~~Currently order is created at user creation, but a user should be able to keep completed orders in history, so: add ability to create a new order if there are no status.PENDING orders.~~
#### Improvements
##### Overview
Improvements here and there, such as delete cascade async, and controllers not performing operations just calling the service.
##### Problems and visions
- Would be nice to implement Service interface + implementation.
- Would be nice to handle more operations asynchronously either with kafka when it comes to MS communication, or with reactive programming, which is awesome but have to get used to it.
