package org.flowant.users.data;

import java.time.LocalDateTime;
import java.util.UUID;

public class Recipe{ //TODO hierarchy
    //TODO UserId
    String title;
    String description;
    String ingredients; //TODO details
    LocalDateTime cookTime; //TODO details, prepair, cook etc.
    String servings;
    String calory; //TODO calculate
    String NutritionFacts; //TODO calculate
    String directions; //TODO details
    //Photos
    //Movies
    String accessLevel; //TODO Enum
    UUID reviewId;
    CRUDZonedTime crudTime = CRUDZonedTime.now();
}
