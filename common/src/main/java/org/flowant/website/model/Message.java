package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
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

    // container means receiver
    @NonNull
    @PrimaryKey
    @Column("ic")
    IdCid idCid;

    // author means sender
    @NonNull
    @Column("ai")
    UUID authorId;

    @NonNull
    @Column("an")
    String authorName;

    @NonNull
    @Column("s")
    String sentences;

    @Column("ir")
    boolean isRead = false;

    public static Message fromUser(UUID receiver, User sender, String msg) {
        return of(IdCid.random(receiver), sender.getIdentity(), sender.getDisplayName(), msg);
    }

}
