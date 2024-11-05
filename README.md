This is a simple API.

To test it, use the URL bellow:

http://localhost:8080/transactions/{idTransiction}/{country}/{currency}

You have to write the coutry because some currencies have the same name, like Dollar.

Example: 

http://localhost:8080/transactions/1/Brazil/Real

http://localhost:8080/transactions/2/Canada/Dollar

If the country or currency are not found, the API will respond "No valid exchange rate found for Canadal and Dollar"
This record date of exchange rate is more than 6 months so the conversion won't work.
