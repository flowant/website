package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
@Table
public class Message implements HasIdCid, HasAuthor<Message> {

    // containerId means receiverId
    @NonNull
    @PrimaryKey
    @Column("ic")
    IdCid idCid;

    @NonNull
    @Column("rn")
    String receiverName;

    // author means sender
    @NonNull
    @Indexed
    @Column("ai")
    UUID authorId;

    @NonNull
    @Column("an")
    String authorName;

    @NonNull
    @Column("s")
    String sentences;

    @Column("mr")
    boolean markedAsRead = false;

    @NonNull
    @Column("t")
    ZonedTime time;

    public static Message fromUser(User receiver, User sender, String msg) {
        return of(IdCid.random(receiver.getIdentity()), receiver.getDisplayName(),
                sender.getIdentity(), sender.getDisplayName(), msg, ZonedTime.now());
    }

}
