package com.example.walletrestservice.service;

import com.example.walletrestservice.model.Wallet;

public interface WalletService {

    long getBalance(int id);

    Wallet execute(int id, String operation, long amount);

}
