package com.example.telegrambot.model;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {
    private String fullName;
    private String phone;
    private String companyName;
    private LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Tashkent"));
    private LocalTime localTime = LocalTime.now(Clock.tickSeconds(ZoneId.of("Asia/Tashkent")));
    private Contact contact;
}
