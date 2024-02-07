package com.example.walletrestservice.model;

import jakarta.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private long balance = 0;

    public Wallet() {
    }

    public Wallet(long balance) {
        this.balance = balance;
    }

    public void deposit(long amount){
        this.balance+=amount;
    }

    public void withdraw(long amount){
        if (this.balance - amount < 0) throw new IllegalStateException("Insufficient funds");
        this.balance-=amount;
    }

    public long getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
