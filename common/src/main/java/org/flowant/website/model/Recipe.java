package org.flowant.website.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Recipe {
    @NonNull
    List<String> ingredients;
    String prepareTime;
    String cookTime;
    int servings;
    int calory; //TODO calculate with servings
    String nutritionFacts;//TODO calculate with servings
}
