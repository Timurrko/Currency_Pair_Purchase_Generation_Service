package com.thewolf.currencyservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@EnableScheduling
@SpringBootApplication
public class CurrencyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyServiceApplication.class, args);
    }

}

@RestController
@RequestMapping("/purchases")
class RestApiDemoController {

    private final CurrencyPairPurchaseStoryGenerator currencyPairPurchaseStoryGenerator;

    public RestApiDemoController(@Autowired CurrencyPairPurchaseStoryGenerator currencyPairPurchaseStoryGenerator, @Value("${currencyPairsFile}") String currencyPairsFile, @Value("${spareCurrencyPairsFile}") String spareCurrencyPairsFile) {
        this.currencyPairPurchaseStoryGenerator = currencyPairPurchaseStoryGenerator;
        try{
            currencyPairPurchaseStoryGenerator.loadCurrencyPairNamesFromJSON(currencyPairsFile);
        }
        catch (RuntimeException exception){
            currencyPairPurchaseStoryGenerator.loadCurrencyPairNamesFromJSON(spareCurrencyPairsFile);
        }
        finally {
            currencyPairPurchaseStoryGenerator.addCurrencyPair("AAAAAA");
        }
    }

    @GetMapping("")
    Map<String, List<Purchase>> getAllPurchaseStories() {
        return this.currencyPairPurchaseStoryGenerator.getStoryStorage();
    }

    @GetMapping("/{currencyPairName}")
    List<Purchase> getPurchaseStoryByCurrencyPair(@PathVariable String currencyPairName) {
        return currencyPairPurchaseStoryGenerator.getPurchaseStoryByCurrencyPairName(currencyPairName);
    }
}