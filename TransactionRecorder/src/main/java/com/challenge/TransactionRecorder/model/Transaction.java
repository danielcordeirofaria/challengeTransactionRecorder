package com.challenge.TransactionRecorder.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private int idTransaction;

    @Column(name = "description", length = 50, unique = true, nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "puchase_amount", nullable = false)
    private double puchaseAmount;

    public Transaction(int idTransaction, String description, Date date, double puchaseAmount) {
        if (idTransaction <= 0) {
            throw new IllegalArgumentException("IdTransaction must be greater than zero.");
        }
        this.idTransaction = idTransaction;

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (description.length() > 50) {
            throw new IllegalArgumentException("Description cannot exceed 50 characters.");
        }
        this.description = description;
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
        if (puchaseAmount <= 0) {
            throw new IllegalArgumentException("Purchase amount must be greater than zero.");
        }
        this.puchaseAmount = puchaseAmount;
    }


    public Transaction() {}

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPuchaseAmount() {
        return puchaseAmount;
    }

    public void setPuchaseAmount(double puchaseAmount) {
        this.puchaseAmount = puchaseAmount;
    }
}