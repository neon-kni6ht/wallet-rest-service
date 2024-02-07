package com.example.walletrestservice.controller;

import com.example.walletrestservice.model.RequestDTO;
import com.example.walletrestservice.model.Wallet;
import com.example.walletrestservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<Long> getWallet(@PathVariable int id){
        return new ResponseEntity<>(walletService.getBalance(id), HttpStatus.OK);
    }

    @PostMapping("/wallet")
    public ResponseEntity<Wallet> execute(@RequestBody RequestDTO request){
        return new ResponseEntity<>(walletService.execute(request.id(), request.operation(), request.amount()), HttpStatus.OK);
    }
}
