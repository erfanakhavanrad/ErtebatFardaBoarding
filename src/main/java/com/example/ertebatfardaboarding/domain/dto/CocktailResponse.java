package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class CocktailResponse {
    private List<Cocktail> drinks;

    // Getters and Setters
    public List<Cocktail> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Cocktail> drinks) {
        this.drinks = drinks;
    }
}
