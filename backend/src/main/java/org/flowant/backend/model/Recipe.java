package org.flowant.backend.model;

import java.time.Duration;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Recipe {
    @NonNull
    List<String> ingredients;
    long prepareSeconds;
    long cookSeconds;
    int servings;
    int calory; //TODO calculate with servings
    String nutritionFacts;//TODO calculate with servings

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
