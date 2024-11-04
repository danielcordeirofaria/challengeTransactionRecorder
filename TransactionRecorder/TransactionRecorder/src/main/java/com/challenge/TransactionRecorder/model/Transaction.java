package com.challenge.TransactionRecorder.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Data
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
        this.idTransaction = idTransaction;
        this.description = description;
        this.date = date;
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