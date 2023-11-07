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
