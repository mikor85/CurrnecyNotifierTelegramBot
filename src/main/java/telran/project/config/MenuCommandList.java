package telran.project.config;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public class MenuCommandList {
    final List<BotCommand> listOfCommands = List.of(
            new BotCommand("/help", "Info how to use this bot."),
            new BotCommand("/start", "Use the command to start using the service."),
            new BotCommand("/stop", "Use the command to stop using the service."),
            new BotCommand("/rate", "Get current rate."),
            new BotCommand("/subscribe", "Subscribe to the currency pair rate."),
            new BotCommand("/unsubscribe", "Remove the currency pair from subscriptions.")
    );

    public List<BotCommand> getListOfCommands() {
        return listOfCommands;
    }
}