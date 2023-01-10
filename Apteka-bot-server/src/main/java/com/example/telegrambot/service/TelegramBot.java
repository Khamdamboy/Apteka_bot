package com.example.telegrambot.service;

import com.example.telegrambot.admin.AdminController;
import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.controller.DoctorController;
import com.example.telegrambot.controller.EmployeeDataController;
import com.example.telegrambot.controller.PharmacyController;
import com.example.telegrambot.controller.UserController;
import com.example.telegrambot.contstant.Constant;
import com.example.telegrambot.contstant.Step;
import com.example.telegrambot.entity.Doctor;
import com.example.telegrambot.entity.Users;
import com.example.telegrambot.entity.Worker;
import com.example.telegrambot.model.TelegramUser;
import com.example.telegrambot.repository.DoctorRepository;
import com.example.telegrambot.repository.Locationrepository;
import com.example.telegrambot.repository.PharmacyRepository;
import com.example.telegrambot.repository.WorkerRepository;
import com.example.telegrambot.util.DeleteMsg;
import com.example.telegrambot.util.SendMsg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    List<Users> users = new ArrayList<>();
    public final BotConfig botConfig;
    public final DoctorRepository doctorRepository;
    public final Locationrepository locationrepository;

    public final UserController userController;

    public final DeleteMsg deleteMsg;
    public final PharmacyRepository pharmacyRepository;
    public final WorkerRepository workerRepository;

    public final DoctorController doctorController;

    public final PharmacyController pharmacyController;

    public final EmployeeDataController dataController;
    public final AdminController adminController;
    public final CallbackqueryMessage callbackqueryMessage;
    TelegramUser telegramUser = new TelegramUser();
    TelegramUser order = new TelegramUser();

    Doctor doctor = new Doctor();

    public String area = "";

    public TelegramBot(@Lazy BotConfig botConfig, @Lazy DoctorRepository doctorRepository, @Lazy Locationrepository locationrepository, @Lazy UserController userController, @Lazy DeleteMsg deleteMsg, @Lazy PharmacyRepository pharmacyRepository, @Lazy WorkerRepository workerRepository, @Lazy
    DoctorController doctorController, @Lazy PharmacyController pharmacyController, @Lazy EmployeeDataController dataController, @Lazy AdminController adminController, @Lazy CallbackqueryMessage callbackqueryMessage) {
        this.botConfig = botConfig;
        this.doctorRepository = doctorRepository;
        this.locationrepository = locationrepository;
        this.userController = userController;
        this.deleteMsg = deleteMsg;
        this.pharmacyRepository = pharmacyRepository;
        this.workerRepository = workerRepository;
        this.doctorController = doctorController;
        this.pharmacyController = pharmacyController;
        this.dataController = dataController;
        this.adminController = adminController;
        this.callbackqueryMessage = callbackqueryMessage;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "boshlash"));
        commands.add(new BotCommand("/about", "bot haqida"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {
            Users users = saveUser(update.getMessage());
            Message message = update.getMessage();
            if (message.getChatId() == 627977908|| message.getChatId()==1039240620||message.getChatId()==1030035146){
                adminController.handle(update);
                return;
            }
            if (update.getMessage().hasText()) {
                String text = message.getText();
                switch (text) {
                    case "/start" -> {
                        start(message);
                        users.setStep(Step.checkPassword);
                        return;
                    }
                    case "/about" -> {
                        about(message);
                        return;
                    }

                    case Constant.pharmacy -> {
                        pharmacyController.getPharmacyName(message);
                        users.setStep(Step.getPharmacyPhone);
                        return;
                    }
                    case Constant.claim -> {
                        callbackqueryMessage.claim(message);
                        users.setStep(Step.fullname);
                        return;
                    }
                    case Constant.addDoctor -> {
                        doctorController.setDoctorName(message);
                        users.setStep(Step.setDoctorFullName);
                        return;
                    }
                    case Constant.serchingDoctor -> {
                        doctorController.searchDoctor(message);
                        users.setStep(Step.getDoctorName);

                        return;
                    }
                    case Constant.backMenu -> {
                        callbackqueryMessage.menu(message);
                        return;
                    }

                }


                switch (users.getStep()) {
                    case Step.finishGettingDoctorData -> {
                        doctorController.finishGetDoctorData(message);
                        users.setStep("null");
                    }
                    case Step.setDoctorFullName -> {
                        doctorController.setArea(message);
                        users.setStep(Step.setArea);
                    }
                    case Step.setArea -> {
                        doctorController.setHospitalName(message);
                        users.setStep(Step.getHospitalName);
                    }
                    case Step.getHospitalName -> {
                        doctorController.setDoctorPosition(message);
                        users.setStep(Step.getDoctorPhone);
                    }
                    case Step.fullname -> {
                        telegramUser.setFullName(message.getText());
                        dataController.getPhone(message);
                        users.setStep(Step.getPhone);
                    }
                    case Step.getPhone -> {
                        telegramUser.setPhone(message.getText());
                        dataController.getCompanyName(message);
                        users.setStep(Step.getCompanyName);
                    }
                    case Step.getCompanyName -> {
                        telegramUser.setCompanyName(message.getText());
                        dataController.getLocation(message);
                    }
                    case Step.getDoctorName -> doctorController.getDoctorData(message);
                    case Step.getDoctorPhone -> {
                        doctorController.setDoctorPhoneNumber(message);
                        users.setStep(Step.getStatus);
                    }
                    case Step.getStatus -> {
                        doctorController.setDoctorStatus(message);
                        users.setStep(Step.finishGettingDoctorData);
                    }
                    case Step.getPharmacyPhone -> {
                        pharmacyController.getPharmacyOwnerName(message);
                        users.setStep(Step.getPharmacyOwnerName);
                    }
                    case Step.getPharmacyOwnerName -> {
                        pharmacyController.getPharmacyOwnerPhone(message);
                        users.setStep(Step.getAddress);
                    }
                    case Step.getAddress -> {
                        pharmacyController.getPharmacyAddress(message);
                        users.setStep(Step.getLocation);
                    }
                    case Step.getLocation -> pharmacyController.getPharmacyLocation(message);
                    case Step.checkPassword -> {
                        boolean password = userController.checkPassword(message);
                        if (password) {
                            Optional<Worker> optional = workerRepository.findByPassword(message.getText());
                            if (optional.isPresent()) {
                                Worker worker = optional.get();
                                area = worker.getArea();
                            }
                        }

                    }

                }
            } else if (update.getMessage().hasLocation()) {

                if (users.getStep().equals(Step.getLocation)) {
                    pharmacyController.finishPharmacyData(message);
                } else {
                    dataController.finishClaim(message);
                    sendDataOfUserToAdmin(message, area);
                }


            } else if (update.getMessage().hasContact()) {
                if (users.getStep().equals(Step.getGetPhoneByOrder)) {
                    order.setContact(message.getContact());
                } else {
                    telegramUser.setContact(message.getContact());
                    dataController.getCompanyName(message);
                    users.setStep(Step.getCompanyName);
                }


            }


        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            if (message.getChatId() == 627977908|| message.getChatId()==1039240620||message.getChatId()==1030035146) {
                adminController.handle(update);
                return;
            }
            callbackqueryMessage.handle(callbackQuery);

        } else {
            send(SendMsg.sendMsg(update.getMessage().getChatId(), "Unknown"));
        }

    }

    private Users saveUser(Message message) {

        for (Users user : users) {
            if (user.getChatId().equals(message.getChatId())) {
                return user;
            }
        }
        Users user = new Users();

        user.setChatId(message.getChatId());

        users.add(user);
        return user;
    }


    public void sendDataOfUserToAdmin(Message message, String area) {

        if (telegramUser.getContact() != null) {

            if (message.getFrom().getUserName() != null) {
                send(SendMsg.sendMsgParse(413482033L,
                        "*Ismi va Familiyasi : *" + telegramUser.getFullName()
                                + "\n*Borgan korxona nomi : * " + telegramUser.getCompanyName() +
                                "\n*Telefon nomeri : *" + telegramUser.getContact().getPhoneNumber() + "" +
                                "\n*Telegram manzil : * @" + message.getFrom().getUserName() +
                                "*\nBiriktirilgan hudud :*" + area +
                                "\n*Sana : *" + telegramUser.getLocalDate() +
                                "\n*Vaqt : *" + telegramUser.getLocalTime()));
            } else {
                send(SendMsg.sendMsgParse(413482033L,
                        "*Ismi va Familiyasi : *" + telegramUser.getFullName()
                                + "\n*Borgan korxona nomi : * " + telegramUser.getCompanyName() +
                                "\n*Telefon nomeri : *" + telegramUser.getContact().getPhoneNumber() +
                                "*\nBiriktirilgan hudud :*" + area +
                                "\n*Sana : *" + telegramUser.getLocalDate() +
                                "\n*Vaqt : *" + telegramUser.getLocalTime()));
            }

        } else {
            if (message.getFrom().getUserName() != null) {
                send(SendMsg.sendMsgParse(413482033L,
                        "*Ismi va Familiyasi : *" + telegramUser.getFullName()
                                + "\n*Borgan korxona nomi : * " + telegramUser.getCompanyName() +
                                "\n*Telefon nomeri : *" + telegramUser.getPhone() + "" +
                                "\n*Telegram manzil : * @" + message.getFrom().getUserName() +
                                "*\nBiriktirilgan hudud :*" + area +
                                "\n*Sana : *" + telegramUser.getLocalDate() +
                                "\n*Vaqt : *" + telegramUser.getLocalTime()));
            } else {
                send(SendMsg.sendMsgParse(413482033L,
                        "*Ismi va Familiyasi : *" + telegramUser.getFullName()
                                + "\n*Borgan korxona nomi : * " + telegramUser.getCompanyName() +
                                "\n*Telefon nomeri : *" + telegramUser.getPhone() +
                                "*\nBiriktirilgan hudud :*" + area +
                                "\n*Sana : *" + telegramUser.getLocalDate() +
                                "\n*Vaqt : *" +
                                telegramUser.getLocalTime()));
            }

        }
        send(SendMsg.sendLC(413482033L, message.getLocation().getLatitude(),
                message.getLocation().getLatitude()));

    }


    public void start(Message message) {
        send(SendMsg.sendMsgParse(message.getChatId(),
                "*Iltimos bot dan foydalanishingiz uchun siz ga berilga parol ni kiriting*"));
    }


    public void about(Message message) {
        send(SendMsg.sendMsgParse(message.getChatId(),
                "*Ushbu bot  OOO DR BLESS TRADE kompniyasi uchun Hozirgi kunda Java Backend developer sifatida "
                        + "ECMA companiyasida ishlab kelayotgan " + "Amonov Firdavs Rustamjon o'g'li tomonidan yaratildi !" +
                        " Murojat uchun  telegram manzil : @Java_Backend_Dr* , " + "*Tel nomer :* +99893-223-54-32 "));


    }


    public void deleteMSG(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId());
        deleteMessage.setMessageId(message.getMessageId());
        send(deleteMessage);
    }

    public void deleteMsgTW(Message message) {
        DeleteMessage deleteMessage1 = new DeleteMessage();
        deleteMessage1.setChatId(message.getChatId());
        deleteMessage1.setMessageId(message.getMessageId() - 1);
        send(deleteMessage1);

        DeleteMessage deleteMessage2 = new DeleteMessage();
        deleteMessage2.setChatId(message.getChatId());
        deleteMessage2.setMessageId(message.getMessageId());
        send(deleteMessage2);
    }


    public void send(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendLocation sendLocation) {
        try {
            execute(sendLocation);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }


}
