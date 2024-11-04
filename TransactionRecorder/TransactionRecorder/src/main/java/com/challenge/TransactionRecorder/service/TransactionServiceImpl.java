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
                        System.out.println(exchangeRateData.getString("record_date"));

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
                return "No valid exchange rate found for " + country + " and " + currency; // Mensagem mais informativa
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    public ResponseEntity<?> saveTransaction(Transaction transaction) {
        try{
            transaction.setDate(new Date());

            if(transactionDAO.existsById(transaction.getIdTransaction())){
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Existent Id."));
            }
            if(transaction.getDescription().length()>50){
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Description is too long. Please, decrease the description."));
            }
            if (transaction.getPuchaseAmount()<=0){
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "The amount can not be smaller or equal than zero."));
            }
            double formattedAmount = Math.round(transaction.getPuchaseAmount() * 100.0) / 100.0;
            transaction.setPuchaseAmount(formattedAmount);

            transactionDAO.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Transaction registered successfully"));

        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred while registering the new transaction.");
        }
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) transactionDAO.findAll();
        return transactions;
    }
}
