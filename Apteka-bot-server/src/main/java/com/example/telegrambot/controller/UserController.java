package com.example.telegrambot.controller;

import com.example.telegrambot.button.InlineKeyboard;
import com.example.telegrambot.entity.Worker;
import com.example.telegrambot.repository.WorkerRepository;
import com.example.telegrambot.service.TelegramBot;
import com.example.telegrambot.util.SendMsg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Controller
public class UserController {

    private final TelegramBot telegramBot;
    private final WorkerRepository workerRepository;

    public UserController(@Lazy TelegramBot telegramBot, @Lazy WorkerRepository workerRepository) {
        this.telegramBot = telegramBot;
        this.workerRepository = workerRepository;
    }

    public boolean checkPassword(Message message) {
        boolean password = workerRepository.existsByPassword(message.getText());
        if (!password) {
            telegramBot.send(SendMsg.sendMsgParse(message.getChatId(),
                    "*Parol xato ! Itimos qayta dan urinib ko'ring !*"));
            return false;
        } else {
            startMessage(message);
            return true;
        }
    }

    private void startMessage(Message message) {
        telegramBot.send(SendMsg.sendMsgParse(message.getChatId(),
                "Assalomu alekum " + " *DR BLESS TRADE * korxonasiga tegishli botga xush kelibsiz" +
                        " !" + " Iltimos asosiy saxifaga o'ting",
                InlineKeyboard.goToMenu("Asosiy sahifaga o'tish")));

    }


}
