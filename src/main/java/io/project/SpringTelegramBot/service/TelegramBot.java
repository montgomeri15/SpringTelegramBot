package io.project.SpringTelegramBot.service;

import io.project.SpringTelegramBot.config.BotConfig;
import io.project.SpringTelegramBot.database.User;
import io.project.SpringTelegramBot.database.UserRepository;
import io.project.SpringTelegramBot.service.components.BotCommands;
import io.project.SpringTelegramBot.service.components.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component //авто-створення екземпляру
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {

    @Autowired //внутрішньо використовує ін'єкцію сетера або конструктора
    private UserRepository userRepository;
    final BotConfig config;
    public TelegramBot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName);
        }

        if(chatId == Long.valueOf(config.getChatId())){
            updateDB(userId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        switch (receivedMessage){
            case "/start", "/start@CountingMessagesBot":
                startBot(chatId, userName);
                break;
            case "/help", "/help@CountingMessagesBot":
                sendHelpTest(chatId, HELP_TEXT, userName);
                break;
            default: break;
        }
    }

    private void updateDB(long userId, String userName) {
        if(userRepository.findById(userId).isEmpty()){
            User user = new User();
            user.setId(userId);
            user.setName(userName);
            user.setMsg_numb(1);

            userRepository.save(user);
            log.info("Користувач доданий до БД: " + userRepository.findById(userId));
        } else {
            userRepository.updateMsgNumberByUserId(userId);
            log.info("К-ть повідомлень +1: " + userRepository.findById(userId));
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привіт, " + userName + "! Я вдало запустився. Що робимо?");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Надіслана відповідь на /start користувачу " + userName);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void sendHelpTest(long chatId, String textToSend, String userName){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Надіслана відповідь на /help користувачу " + userName);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
