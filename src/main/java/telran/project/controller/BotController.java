package telran.project.controller;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telran.project.dto.AddSubscribeCommand;
import telran.project.dto.HelpCommand;
import telran.project.dto.StartCommand;
import telran.project.dto.StopCommand;
import telran.project.service.BotConfig;
import telran.project.service.BotService;
import telran.project.service.MenuCommandList;
import telran.project.service.MessageSender;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@Setter
//@NoArgsConstructor

@Service
public class BotController extends TelegramLongPollingBot implements MessageSender {

    @Autowired
    private BotService service;

    @Autowired
    final BotConfig config;

    public BotController(BotConfig config) {
        this.config = config;
        MenuCommandList commandList = new MenuCommandList();

        try {
            this.execute(new SetMyCommands(commandList.getListOfCommands(), new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {

            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    public String getBotUsername() {
        return config.getBotName();
    }


    public String getBotToken() {
        return config.getToken();
    }


    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    String answer = service.onStartCommandReceived(new StartCommand(update));
                    sendMessage(chatId, answer);
                    break;

                case "/stop":
                    answer = service.onStopCommandReceived(new StopCommand(update));
                    sendMessage(chatId, answer);
                    break;

                case "/help":
                    answer = service.onHelpCommandReceived(new HelpCommand(update));
                    sendMessage(chatId,answer);
                    break;
                default:

                case "/addChannelSub":
                    answer = service.onAddSubscribeCommand(new AddSubscribeCommand(update));
                    sendMessage(chatId,answer);
                    break;
            }
        }
    }

    @Override
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}