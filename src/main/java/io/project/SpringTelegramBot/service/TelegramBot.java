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
    private int messageCount = 0;

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
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            long memberId = update.getMessage().getFrom().getId();
            String memberName = update.getMessage().getFrom().getUserName();

            addOneMessageToDB(memberId);

            switch (messageText){
                case "/start", "/start@CountingMessagesBot":
                    startBot(chatId, update.getMessage().getFrom().getFirstName(), memberId);
                    addUserBot(memberId, memberName);
                    log.info(String.valueOf(userRepository.findById(String.valueOf(memberId))));
                    break;
                case "/help", "/help@CountingMessagesBot":
                    sendHelpTest(chatId, HELP_TEXT);
                    break;
                default: log.info("Меседж не підтримується");
            }
        }
    }

    private void addOneMessageToDB(long memberId) {
        log.info(String.valueOf(userRepository.findById(String.valueOf(memberId))));


//        if(!userRepository.findById(String.valueOf(memberId)).isEmpty()){
//            User user = new User();
//            user.setMsg_numb(user.getMsg_numb() + 1);
//            //userRepository.save(user);
//            log.info("Запис користувача змінено: " + user);
//        }
    }

    private void addUserBot(long memberId, String memberName) {
        if(userRepository.findById(String.valueOf(memberId)).isEmpty()){
            User user = new User();
            user.setId(memberId);
            user.setName(memberName);
            user.setMsg_numb(user.getMsg_numb() + 1);

            userRepository.save(user);
            log.info("Користувач доданий до БД: " + user);

        }
    }

    private void startBot(long chatId, String userName, long memberId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привіт, " + chatId + "! Обери в меню ті повідомлення, які слід відобразити.");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Надіслана відповідь користувачу " + userName);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void sendHelpTest(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
