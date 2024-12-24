package com.example.ertebatfardaboarding.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Cocktail {
    @JsonProperty("idDrink")
    private String idDrink;

    @JsonProperty("strDrink")
    private String drinkName;

    @JsonProperty("strCategory")
    private String category;

    @JsonProperty("strGlass")
    private String glass;

    @JsonProperty("strInstructions")
    private String instructions;

    @JsonProperty("strDrinkThumb")
    private String drinkThumb;

    @JsonProperty("strIngredient1")
    private String ingredient1;

    @JsonProperty("strIngredient2")
    private String ingredient2;

    @JsonProperty("strMeasure1")
    private String measure1;

    @JsonProperty("strMeasure2")
    private String measure2;

    // Getters and Setters
    // toString() method (optional, useful for debugging)
}
