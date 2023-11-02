
├── ms-auth
│   ├── compose.yaml
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── buy01
│   │   │   │       └── authservice
│   │   │   │           ├── AuthserviceApplication.java
│   │   │   │           ├── config
│   │   │   │           │   ├── ApplicationConfig.java
│   │   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │   │           │   ├── JwtService.java
│   │   │   │           │   └── SecurityConfig.java
│   │   │   │           ├── controllers
│   │   │   │           │   ├── AuthenticationController.java
│   │   │   │           │   └── HomeController.java
│   │   │   │           ├── enums
│   │   │   │           │   └── Role.java
│   │   │   │           ├── exceptions
│   │   │   │           │   ├── CustomAuthenticationException.java
│   │   │   │           │   ├── DuplicateUserException.java
│   │   │   │           │   ├── ExpiredJwtException.java
│   │   │   │           │   ├── GlobalExceptionHandler.java
│   │   │   │           │   ├── IncorrectPasswordException.java
│   │   │   │           │   ├── InvalidJwtTokenException.java
│   │   │   │           │   ├── MissingJwtTokenException.java
│   │   │   │           │   └── ResourceNotFoundException.java
│   │   │   │           ├── models
│   │   │   │           │   ├── client
│   │   │   │           │   │   ├── ClientAuthenticationRequest.java
│   │   │   │           │   │   ├── ClientAuthenticationResponse.java
│   │   │   │           │   │   └── ClientRegistrationRequest.java
│   │   │   │           │   └── user
│   │   │   │           │       ├── UserAuthenticationRequest.java
│   │   │   │           │       ├── UserAuthenticationResponse.java
│   │   │   │           │       ├── UserRegistrationRequest.java
│   │   │   │           │       └── UserRegistrationResponse.java
│   │   │   │           └── services
│   │   │   │               ├── AuthenticationService.java
│   │   │   │               ├── KafkaConsumerService.java
│   │   │   │               └── KafkaProducerService.java
│   │   │   └── resources
│   │   │       ├── META-INF
│   │   │       │   └── additional-spring-configuration-metadata.json
│   │   │       └── application.properties
│   │   └── test
│   │       └── java
│   │           └── buy01
│   │               └── authservice
│   │                   └── AuthserviceApplicationTests.java
│   └── target
│       ├── classes
│       │   ├── META-INF
│       │   └── buy01
│       │       └── authservice
│       │           ├── config
│       │           ├── controllers
│       │           ├── enums
│       │           ├── exceptions
│       │           ├── models
│       │           │   ├── client
│       │           │   └── user
│       │           └── services
│       ├── generated-sources
│       │   └── annotations
│       ├── generated-test-sources
│       │   └── test-annotations
│       └── test-classes
│           └── buy01
│               └── authservice
├── ms-media
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── buy01
│   │   │   │       └── mediaservice
│   │   │   │           ├── config
│   │   │   │           ├── controllers
│   │   │   │           ├── enums
│   │   │   │           ├── exceptions
│   │   │   │           ├── models
│   │   │   │           ├── repositories
│   │   │   │           └── services
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   │           └── buy01
│   │               └── mediaservice
│   └── target
│       ├── classes
│       │   └── buy01
│       │       └── mediaservice
│       │           ├── config
│       │           ├── controllers
│       │           ├── enums
│       │           ├── exceptions
│       │           ├── models
│       │           ├── repositories
│       │           └── services
│       ├── generated-sources
│       │   └── annotations
│       ├── generated-test-sources
│       │   └── test-annotations
│       └── test-classes
│           └── buy01
│               └── mediaservice
├── ms-product
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── buy01
│   │   │   │       └── productservice
│   │   │   │           ├── config
│   │   │   │           ├── controllers
│   │   │   │           ├── enums
│   │   │   │           ├── exceptions
│   │   │   │           ├── models
│   │   │   │           ├── repositories
│   │   │   │           └── services
│   │   │   └── resources
│   │   └── test
│   │       └── java
│   │           └── buy01
│   │               └── productservice
│   └── target
│       ├── classes
│       │   └── buy01
│       │       └── productservice
│       │           ├── config
│       │           ├── controllers
│       │           ├── enums
│       │           ├── exceptions
│       │           ├── models
│       │           ├── repositories
│       │           └── services
│       ├── generated-sources
│       │   └── annotations
│       ├── generated-test-sources
│       │   └── test-annotations
│       └── test-classes
│           └── buy01
│               └── productservice
└── ms-user
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── buy01
    │   │   │       └── userservice
    │   │   │           ├── config
    │   │   │           ├── controllers
    │   │   │           ├── enums
    │   │   │           ├── exceptions
    │   │   │           ├── models
    │   │   │           │   ├── auth
    │   │   │           │   └── client
    │   │   │           ├── repository
    │   │   │           └── services
    │   │   └── resources
    │   │       └── META-INF
    │   └── test
    │       └── java
    │           └── buy01
    │               └── userservice
    └── target
        ├── classes
        │   ├── META-INF
        │   └── buy01
        │       └── userservice
        │           ├── config
        │           ├── controllers
        │           ├── enums
        │           ├── exceptions
        │           ├── models
        │           │   ├── auth
        │           │   └── client
        │           ├── repository
        │           └── services
        ├── generated-sources
        │   └── annotations
        ├── generated-test-sources
        │   └── test-annotations
        └── test-classes
            └── buy01
                └── userservice
