package telran.project.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class MenuCommandList {
    final List<BotCommand> listOfCommands = List.of(
            new BotCommand("/start", "use the command to start the bot"),
            new BotCommand("/stop", "use the command to stop the bot"),
            new BotCommand("/help", "info how to use this bot"),
            new BotCommand("/addChannelSub", "adds a channel to your bot")
//                    (new BotCommand("/mydata", "get your data stored")),
//                    (new BotCommand("/deletedata", "get your data stored")),
//
//                    (new BotCommand("/settings", "set your preferences")),
//                    (new BotCommand("/getUpdates", "get updates"))
    );

    public List<BotCommand> getListOfCommands() {
        return listOfCommands;
    }
}
