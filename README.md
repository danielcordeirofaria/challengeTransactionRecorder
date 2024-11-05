This simple API gets the exchange rate and converts the purchase amount.

To test it, use the URL below:

http://localhost:8080/transactions/{idTransiction}/{country}/{currency}

You need to write the country because some currencies have the same name, like Dollar.

Example: 

http://localhost:8080/transactions/1/Brazil/Real

http://localhost:8080/transactions/2/Canada/Dollar

If the country or currency is not found, the API will respond "No valid exchange rate found for Canadal and Dollar"
This record date of exchange rate is more than 6 months so the conversion won't work.

To send a transaction, You need to write a json like this:

{
  "description": "Paying for lunch",
  "puchaseAmount": 15.35
}

sending a post call to: http://localhost:8080/transactions

To get all transactions send a get call to: http://localhost:8080/transactions
