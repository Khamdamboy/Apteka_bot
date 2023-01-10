package com.example.telegrambot.util;

import com.example.telegrambot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Component
public class DeleteMsg {

    public final TelegramBot telegramBot;

    @Autowired
    public DeleteMsg(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void deleteMsg(Long chatId, Integer messageID) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageID);
        telegramBot.send(deleteMessage);
    }


    public void deleteMsgTW(Long chatId, Integer messageID) {

        DeleteMessage deleteMessage1 = new DeleteMessage();
        deleteMessage1.setChatId(chatId);
        deleteMessage1.setMessageId(messageID);
        telegramBot.send(deleteMessage1);

        DeleteMessage deleteMessage2 = new DeleteMessage();
        deleteMessage2.setMessageId(messageID-1);
        deleteMessage2.setChatId(chatId);
        telegramBot.send(deleteMessage2);


    }

}
