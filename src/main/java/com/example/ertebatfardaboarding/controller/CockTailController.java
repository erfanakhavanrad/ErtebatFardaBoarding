package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.dto.Cocktail;
import com.example.ertebatfardaboarding.domain.dto.CocktailResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@RequestMapping("/cocktail")
@RestController
public class CockTailController {
    private RestTemplate restTemplate;
    private final static String COCKTAIL_API = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

    public CockTailController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @CircuitBreaker(name = "randomDrink", fallbackMethod = "fallBackDrink")
    public CocktailResponse getRandomCocktailRecipe() {
        ResponseEntity<CocktailResponse> forEntity = restTemplate.getForEntity(COCKTAIL_API, CocktailResponse.class);
        CocktailResponse cocktail = forEntity.getBody();
        log.info("Cocktail received: " + cocktail.getDrinks().get(0).getDrinkName());
        return cocktail;
    }

    public CocktailResponse fallBackDrink(Throwable throwable) {
        CocktailResponse cocktailResponse = new CocktailResponse();
        Cocktail cocktail = new Cocktail();
        cocktail.setDrinkName("Martini");
        cocktailResponse.setDrinks(Collections.singletonList(cocktail));
        return cocktailResponse;
    }


}
