### JAX-RS web app demo

To run:
1) `mvn jetty:run` starts standalone Jetty server with app on localhost:8080
2. `mvn package` will produce WAR file in `target` directory which can be deployed in servlet container (Servlet 3.X support required)

There is `data.json` file containing app database created on startup in the current directory (where the app is started)

Exemplary requests:

1. Create an account

POST `localhost:8080/api/v1/accounts/account`

- `Content-Type`: `application/json`
- `Accept`: `application/json`

~~~~
{
    "name": "Daniel",
    "currency": "USD",
    "money": 100
}
~~~~

Response: `201 Created`

~~~~
{
    "id": 1,
    "name": "Daniel",
    "currency": "USD",
    "money": 100
}
~~~~


2. Query existing account

 GET `localhost:8080/api/v1/accounts/account/1`
- `Accept`: `application/json`

Response: `200 OK`

~~~~
{
    "id": 1,
    "name": "Daniel",
    "currency": "USD",
    "money": 100
}
~~~~


3. Modify account

 PATCH `localhost:8080/api/v1/accounts/account/1`

- `Content-Type`: `application/json-patch+json`
- `Accept`: `application/json`

~~~~
[
	{
		"op": "replace",
		"path": "/name",
		"value": "Alex"
	},
	{
		"op": "test",
		"path": "/currency",
		"value": "USD"
	},
	{
		"op": "replace",
		"path": "/money",
		"value": 245.88
	}
]
~~~~

Response: `200 OK`
~~~~
{
    "id": 1,
    "name": "Alex",
    "currency": "USD",
    "money": 245.88
}
~~~~


3. Try to modify account with `test` patch operation which evaluates to false

 PATCH `localhost:8080/api/v1/accounts/account/2`

- `Content-Type`: `application/json-patch+json`
- `Accept`: `application/json`

~~~~
[
	{
		"op": "replace",
		"path": "/name",
		"value": "Alex"
	},
	{
		"op": "test",
		"path": "/currency",
		"value": "USD"
	},
	{
		"op": "replace",
		"path": "/money",
		"value": 245.88
	}
]
~~~~

Response: `409 Conflict`
~~~~
{
    "id": 2,
    "name": "Ania",
    "currency": "AUD",
    "money": 3000
}
~~~~


4. Query all existing accounts

GET `localhost:8080/api/v1/accounts/account/`

- `Accept`: `application/json`

Response: `200 OK`
~~~~
[
    {
        "id": 1,
        "name": "Alex",
        "currency": "USD",
        "money": 200
    },
    {
        "id": 2,
        "name": "Ania",
        "currency": "AUD",
        "money": 3000
    }
]
~~~~
