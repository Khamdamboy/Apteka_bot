package com.example.telegrambot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor




public class Users {

    private Long chatId;
    @Column
    private String username;

    private String step;
}
