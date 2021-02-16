#Address Book API

This API offers search for customers by their surname. 
The API should also be able to return a JSON representation of the customers full details when a customer is specified.

###Running, Building, Testing (CLI)
To run the following commands use Gradle or Gradlew equivalent in the CLI

to clean & build the project:
    
    ./gradlew clean build
    
to run all tests:
    
    ./gradlew test --tests


to run API:
    
    ./gradlew bootRun


the server will run on localhost default port 8080


###Assumptions

As inferred from the specification provided, there is no functionality to create a `Customer` from the API,
However, in place of this i created a two SQL scripts 

`resources/db/schema.sql`
`resources/db/data.sql`

These scripts initialises and populate the H2 in-memory database with dummy data. 
Running the application will produce log messages of responses to the two GET queries

[Search Customer by customer surname](#search-by-surname)

[Search Customer by customer id](#search-by-id)


Another assumption has been made regarding customers having multiple addresses.
This is represented in the `Customer` table as well as `CustomerAddress`.
This is a many-to-many relationship maintained in the join table (`C_A`) of the database.

I would have liked to included more testing around persistence of customers with multiple addresses, however opted
to implement as much completeness of the API that covered the spec, in the time constraint given.      



#search-by-surname

Search Customer by customer surname 

    GET /api/v1/customers/search?surname=SURNAME
    
  | Request param |  Data type    | Description    |
  | :-----------: | :-----------: | :------------: |
  | SURNAME       |   String      |   customer surname |

example:

    http://localhost:8080/api/v1/customers/search?surname=belcher


#search-by-id

Search Customer by customer id


    GET /api/v1/customers/2

| Path variable |  Data type    | Description    |
| :-----------: | :-----------: | :------------: |
| ID            |   Long        |   customer id  |

example:

    http://localhost:8080/api/v1/customers/2