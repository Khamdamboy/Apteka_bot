package com.example.telegrambot.service;

import com.example.telegrambot.button.Keyboard;
import com.example.telegrambot.util.SendMsg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CallbackqueryMessage {

    public final TelegramBot telegramBot;

    public CallbackqueryMessage(@Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    public void handle(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();

        if ("go to menu".equals(data)) {
            menu(message);
        }

    }


    public void menu(Message message) {
        telegramBot.deleteMSG(message);
        telegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "Siz asosiy sahifadasiz ! Iltimos o'zingizga tegishli bo'lgan ro'yhatni tanlang ! ",
                Keyboard.mainMenuButton()));
    }


    public void claim(Message message) {
        telegramBot.deleteMsgTW(message);
        telegramBot.send(SendMsg.sendMsgParse(message.getChatId(),
                "Iltimos to'liq ism familyangizni jo'nating" +
                        "\n* Masalan : Ali aliyev *"));

    }


}

