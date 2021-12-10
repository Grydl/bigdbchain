package com.grydl.orange.bigchaindb.domain;

import java.time.ZonedDateTime;
import java.util.Set;

public class Transaction {

    public String id;

    public ZonedDateTime dateTime;

    public String typeTx;

    public String sender;

    public String receiver;

    public String reference;

    public Double amount;

    public Double totalAmount;

    Set<Transaction> listTx;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTypeTx() {
        return typeTx;
    }

    public void setTypeTx(String typeTx) {
        this.typeTx = typeTx;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<Transaction> getListTx() {
        return listTx;
    }

    public void setListTx(Set<Transaction> listTx) {
        this.listTx = listTx;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", dateTime=" + dateTime +
                ", Sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
