package com.thewolf.currencyservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;


@Component
@EnableScheduling
@Getter
public class CurrencyPairPurchaseStoryGenerator {
    private double seed = 1417.0;
    private final Map<String, List<Purchase>> storyStorage = new HashMap<>();
    private final Map<String, Double> seeds = new HashMap<>();

    public void loadCurrencyPairNamesFromJSON(String currencyPairNamesFileName) {
        try (FileReader namesFile = new FileReader(currencyPairNamesFileName)) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Map<String, String>>> typeRef = new TypeReference<>() {
            };
            List<Map<String, String>> nameslist = mapper.readValue(namesFile, typeRef);
            for (Map<String, String> map : nameslist) {
                String name = map.get("currencyPair");
                this.addCurrencyPair(name);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Purchase> getPurchaseStoryByCurrencyPairName(String currencyPairName) {
        return storyStorage.get(currencyPairName);
    }

    public void addCurrencyPair(String currencyPairName) {
        if (!this.getCurrencyPairNames().contains(currencyPairName)) {
            List<Purchase> purchaseList = new ArrayList<>();
            purchaseList.add(new Purchase(currencyPairName, 1.0));
            storyStorage.put(currencyPairName, purchaseList);
            seeds.put(currencyPairName, this.seed);
            this.seed += 974.1;

        }
    }

    public Set<String> getCurrencyPairNames() {
        return seeds.keySet();
    }

    public void addPurchaseByCurrencyPairName(String currencyPairName, Purchase purchase) {
        if (this.getCurrencyPairNames().contains(currencyPairName)) {
            List<Purchase> purchaseList = this.getPurchaseStoryByCurrencyPairName(currencyPairName);
            if (purchaseList.size() == 100) {
                purchaseList.remove(0);
            }
            purchaseList.add(purchase);
        }
    }

    public void generateNewPurchaseByCurrencyPairName(String currencyPairName) {

        if (this.getCurrencyPairNames().contains(currencyPairName)) {
            double seed = seeds.get(currencyPairName);
            List<Purchase> purchaseStory = this.getPurchaseStoryByCurrencyPairName(currencyPairName);
            Purchase lastPurchase = purchaseStory.get(purchaseStory.size() - 1);
            double price = lastPurchase.getPrice();
            Date date = new Date();
            long milliseconds = date.getTime();
            double x = milliseconds / seed;
            double dPrice = Math.sin(x) / 30;
            Purchase newPurchase = new Purchase(currencyPairName, abs(price + dPrice));
            if (purchaseStory.size() == 100){
                purchaseStory.remove(0);
            }
            purchaseStory.add(newPurchase);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void updateAllPrices(){
        for (String currencyPairName: this.getCurrencyPairNames()) {
            this.generateNewPurchaseByCurrencyPairName(currencyPairName);
        }
    }
}
