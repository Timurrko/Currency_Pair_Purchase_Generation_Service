package com.thewolf.currencyservice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import java.util.Date;

import java.util.UUID;

@Getter
@Value
public class Purchase {
    String id;
    String currencyPair;
    Double price;
    Date date;
    public Purchase(String currencyPair, Double price){
        this.id = UUID.randomUUID().toString();
        this.currencyPair = currencyPair;
        this.price = price;
        this.date = new Date();
    }
}
