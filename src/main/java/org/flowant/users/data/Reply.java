package org.flowant.users.data;

import java.util.UUID;

public class Reply {
    UUID to;
    String content;
    int rating;
    int liked;
    int disliked;
    CRUDZonedTime crudTime = CRUDZonedTime.now();
}
