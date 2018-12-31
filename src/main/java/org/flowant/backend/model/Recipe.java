package org.flowant.backend.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data(staticConstructor = "of")
@UserDefinedType
public class Recipe {
    List<String> ingredients = new ArrayList<>();
    long prepareSeconds;
    long cookSeconds;
    int servings = 1; // The default value is 1.
    int calory; //TODO calculate with servings
    String NutritionFacts;//TODO calculate with servings

    public void setPrepareDuration(Duration d) {
        prepareSeconds = d.getSeconds();
    }

    public Duration getPrepareDuration() {
        return Duration.ofSeconds(prepareSeconds);
    }

    public void setCookDuration(Duration d) {
        cookSeconds = d.getSeconds();
    }

    public Duration getCookDuration() {
        return Duration.ofSeconds(cookSeconds);
    }
}
