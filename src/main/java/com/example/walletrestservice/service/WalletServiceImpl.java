package com.example.walletrestservice.service;

import com.example.walletrestservice.model.Operations;
import com.example.walletrestservice.model.Wallet;
import com.example.walletrestservice.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public long getBalance(int id){
        Wallet wallet = walletRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Wallet with id " + id + " was not found") );
        return wallet.getBalance();
    }

    @Transactional
    public Wallet execute(int id, String operation, long amount){
        if (amount < 0) throw new IllegalArgumentException("Illegal amount " + amount);
        if (operation == null) throw new IllegalArgumentException("Illegal operation");

        Operations action;
        try {
        action = Operations.valueOf(operation);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Illegal operation");
        }
        Wallet wallet = walletRepository.findWalletOptionalById(id).orElseThrow(() ->
                new IllegalArgumentException("Wallet with id " + id + " was not found") );
        return switch (action) {
            case DEPOSIT -> {
                wallet.deposit(amount);
                yield walletRepository.save(wallet);
            }
            case WITHDRAW -> {
                wallet.withdraw(amount);
                yield walletRepository.save(wallet);
            }
        };
    }
}
