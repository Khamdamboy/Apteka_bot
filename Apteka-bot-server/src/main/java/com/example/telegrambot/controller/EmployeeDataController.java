package com.example.telegrambot.controller;

import com.example.telegrambot.button.Keyboard;
import com.example.telegrambot.service.TelegramBot;
import com.example.telegrambot.util.SendMsg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;

@Controller
public class EmployeeDataController {

    private final TelegramBot telegramBot;

    public EmployeeDataController(@Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void getCompanyName(Message message) {
        telegramBot.send(SendMsg.sendMsgParse(message.getChatId(),
                "Siz borgan korxona nomini yozing \n" +
                        "\n*Masalan OOO Bilol Pharm *"));
    }

    public void getPhone(Message message) {
        telegramBot.send(SendMsg.sendMsgParse(message.getChatId(),
                "Iltimos telefon nomeringizni quyidagi ko'rinishda jo'nating !\n" + "\n *Masalan : +99897-123-45-67*",
                Keyboard.phoneButton()));


    }

    public void finishClaim(Message message) {
        telegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "Muvaffaqqiyatli yurish ! Shunday davom eting",
                Keyboard.backMenu()));
    }

    public void getLocation(Message message) {
        telegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "Iltimos ayni vaqtdagi joylashuvingizni  junating",
                Keyboard.location()));
    }
}
