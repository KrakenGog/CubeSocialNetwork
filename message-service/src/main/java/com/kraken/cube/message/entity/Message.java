package com.kraken.cube.message.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @Column(name = "message_id")
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "payload")
    private String payload;
    @Column(name = "author_id")
    private Long authorId;
}
