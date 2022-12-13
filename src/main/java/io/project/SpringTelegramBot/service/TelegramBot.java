package io.project.SpringTelegramBot.service;

import io.project.SpringTelegramBot.config.BotConfig;
import io.project.SpringTelegramBot.service.components.BotCommands;
import io.project.SpringTelegramBot.service.components.Buttons;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

            switch (messageText){
                case "/start", "/start@CountingMessagesBot":
                    startBot(chatId, update.getMessage().getFrom().getFirstName(), memberId);

                    break;
                case "/help", "/help@CountingMessagesBot":
                    sendHelpTest(chatId, HELP_TEXT);
                    break;
                default: sendHelpTest(chatId, "Меседж не підтримується");
            }
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
