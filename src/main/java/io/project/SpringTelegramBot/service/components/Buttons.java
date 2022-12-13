package io.project.SpringTelegramBot.service.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton ALL_MESSAGES_BUTTON = new InlineKeyboardButton("Всі");
    private static final InlineKeyboardButton MY_MESSAGES_BUTTON = new InlineKeyboardButton("Мої");

    public static InlineKeyboardMarkup inlineMarkup() {
        ALL_MESSAGES_BUTTON.setCallbackData("ALL_MESSAGES_BUTTON");
        MY_MESSAGES_BUTTON.setCallbackData("MY_MESSAGES_BUTTON");

        List<InlineKeyboardButton> rowInline = List.of(ALL_MESSAGES_BUTTON, MY_MESSAGES_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
