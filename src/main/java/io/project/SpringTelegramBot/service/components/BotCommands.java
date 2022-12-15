package io.project.SpringTelegramBot.service.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/allmessages", "total number of messages"),
            new BotCommand("/mymessages", "number of messages from user"),
            new BotCommand("help", "bot info")
    );

    String HELP_TEXT = "За допомогою цього бота ви можете порахувати кількість повідомлень від кожного учасника чату у заданний період.\n\n" +
            "Використовуйте наступні команди:\n\n" +
            "- /start - запустити бота\n" +
            "- /allmessages - показати загільну кількість повідомлень\n" +
            "- /mymessages - показати кількість власних повідомлень\n" +
            "- /help - меню допомоги";
}
