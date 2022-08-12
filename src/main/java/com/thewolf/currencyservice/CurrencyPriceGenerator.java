package com.thewolf.currencyservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CurrencyPriceGenerator {
    private final Map<String, List<Purchase>> storage = new HashMap<>();
    public void takeNamesFromJSON(String file) {
        try(FileReader namesFile = new FileReader(file)) {
        ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<Map<String, String>>> typeRef = new TypeReference<>() {
            };
        List<Map<String, String>> namelist = objectMapper.readValue(namesFile, typeRef);
        for(Map<String, String> map:namelist){
            String currencyPair = map.get("currencyPair");
            if (!storage.containsKey(currencyPair)){
                storage.put(currencyPair, new ArrayList<>());
            }
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Purchase> getStoryByCurrencyPair(String name){
        return storage.get(name);
    }
}
