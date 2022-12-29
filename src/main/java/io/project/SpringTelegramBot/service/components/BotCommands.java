package io.project.SpringTelegramBot.service.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info")
    );
    String HELP_TEXT = "Я рахую повідомлення в чаті. Запускай мене раз на 24 години, щоб я оновив кількість повідомлень у своїй базі даних.\n\n" +
            "Доступні команди:\n\n" +
            "- /start - запустити бота\n" +
            "- /help - меню допомоги";
}
