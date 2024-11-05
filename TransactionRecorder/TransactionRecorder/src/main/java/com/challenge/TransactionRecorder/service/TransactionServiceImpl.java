package com.challenge.TransactionRecorder.service;

import com.challenge.TransactionRecorder.DAO.TransactionDAO;
import com.challenge.TransactionRecorder.model.Transaction;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

@Service
public class TransactionServiceImpl implements ITransactionService{

    @Autowired
    private TransactionDAO transactionDAO;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public String currencyConversion(int idTransaction, String country, String currency) {

        String URL_GET = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?fields=exchange_rate,currency,country,record_date&filter=country:eq:" + country;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .uri(URI.create(URL_GET))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                String jsonResponse = response.body();

                JSONObject jsonObject = new JSONObject(jsonResponse);

                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject exchangeRateData = dataArray.getJSONObject(i);
//                    System.out.println(exchangeRateData);
                    if (exchangeRateData.getString("currency").equalsIgnoreCase(currency) && exchangeRateData.getString("country").equalsIgnoreCase(country)) {

                        String dataString = exchangeRateData.getString("record_date");
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = formato.parse(dataString);

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.MONTH, -6);
                        Date sixMonthsAgo = calendar.getTime();
//                        System.out.println(exchangeRateData.getString("record_date"));

                        if (date.after(sixMonthsAgo)) {
                            double purchaseAmount = transactionDAO.getByIdTransaction(idTransaction).getPuchaseAmount();
                            double exchangeRate = exchangeRateData.getDouble("exchange_rate");
                            double convertingTransaction = exchangeRate * purchaseAmount;
                            logger.info("Converting transaction: " + convertingTransaction);
                            String responseData = String.format("%.2f", convertingTransaction);

                            return "The converted value is: " + responseData + " " + currency;
                        }
                    }
                }

                logger.error("No valid exchange rate found for " + country + " and " + currency);
                return "No valid exchange rate found for " + country + " and " + currency;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    public void validateTransaction(Transaction transaction) {
        if (transaction.getDescription().length() > 50) {
            throw new IllegalArgumentException("Description is too long. Please, decrease the description.");
        }
        if(transactionDAO.existsById(transaction.getIdTransaction())){
            throw new IllegalArgumentException("Existent Id.");
        }

        if (transaction.getPuchaseAmount()<=0){
            throw new IllegalArgumentException("The amount can not be smaller or equal than zero.");
        }

    }

    @Override
    public ResponseEntity<String> saveTransaction(Transaction transaction) {
        try{
            logger.info("Saving transaction: {}", transaction);
            transaction.setDate(new Date());

            double formattedAmount = Math.round(transaction.getPuchaseAmount() * 100.0) / 100.0;
            transaction.setPuchaseAmount(formattedAmount);
            validateTransaction(transaction);
            transactionDAO.save(transaction);
            return ResponseEntity.ok().body("Transaction registered successfully");

        } catch (IllegalArgumentException e) {
            logger.error("Error saving transaction: Description too long", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error saving transaction", e);
            return ResponseEntity.internalServerError().body("An error occurred while registering the new transaction.");
        }
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) transactionDAO.findAll();
        return transactions;
    }
}
