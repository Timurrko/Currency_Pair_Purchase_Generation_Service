package com.thewolf.currencyservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	private final CurrencyPriceGenerator currencyPriceGenerator;

	public RestApiDemoController(@Autowired CurrencyPriceGenerator currencyPriceGenerator, @Value("${currencyPairsFile}") String currencyPairsFile) {
		this.currencyPriceGenerator = currencyPriceGenerator;
		System.out.println(currencyPairsFile);
		currencyPriceGenerator.takeNamesFromJSON(currencyPairsFile);
	}
	@GetMapping("/{id}")
	List<Purchase> getPurchaseStoryByCurrencyPair(@PathVariable String id) {
		System.out.println(id);
		System.out.println(currencyPriceGenerator.getStoryByCurrencyPair(id));
		return currencyPriceGenerator.getStoryByCurrencyPair(id);
	}
}