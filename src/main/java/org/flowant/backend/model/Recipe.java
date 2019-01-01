package org.flowant.backend.model;

import java.time.Duration;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class Recipe {
    @NonNull
    List<String> ingredients;
    long prepareSeconds;
    long cookSeconds;
    int servings;
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
