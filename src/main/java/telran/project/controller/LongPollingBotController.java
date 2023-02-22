package telran.project.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telran.project.config.BotConfig;
import telran.project.config.MenuCommandList;
import telran.project.dto.*;
import telran.project.entity.RatePair;
import telran.project.entity.User;
import telran.project.repository.RatePairRepository;
import telran.project.repository.UserRepository;
import telran.project.service.BotService;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
@Setter
public class LongPollingBotController extends TelegramLongPollingBot {

    @Autowired
    final BotConfig botConfig;

    @Autowired
    private BotService botService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatePairRepository ratePairRepository;

    static final String UNKNOWN_COMMAND_TEXT = "Sorry, command was not recognized.";

    public LongPollingBotController(BotConfig botConfig) {
        this.botConfig = botConfig;
        MenuCommandList commandList = new MenuCommandList();
        try {
            this.execute(new SetMyCommands(commandList.getListOfCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    public String getBotUsername() {
        return botConfig.getBotName();
    }

    public String getBotToken() {
        return botConfig.getToken();
    }

    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String messageText = message.getText();

            User user = getUser(message);

            // Start Command
            if (messageText.matches("\\s*/start\\s*")) {
                StartCommand startCommand = new StartCommand(user);
                String answerOnStartCommand = botService.onStartCommandReceived(startCommand);
                sendMessage(chatId, answerOnStartCommand);
            } else

                // Stop Command
                if (messageText.matches("\\s*/stop\\s*")) {
                    StopCommand stopCommand = new StopCommand(user);
                    String answerOnStopCommand = botService.onStopCommandReceived(stopCommand);
                    sendMessage(chatId, answerOnStopCommand);
                } else

                    // Help Command
                    if (messageText.matches("\\s*/help\\s*")) {
                        HelpCommand helpCommand = new HelpCommand(user);
                        String answerOnHelpCommand = botService.onHelpCommandReceived(helpCommand);
                        sendMessage(chatId, answerOnHelpCommand);
                    } else

                        // Rate Command
                        if (messageText.startsWith("/rate")) {
                            if ((messageText.matches("/rate\\s+[a-zA-Z]{3}\\s+to\\s+[a-zA-Z]{3}"))) {
                                RateCommand rateCommand = new RateCommand(user, messageText);
                                String answerOnRateCommand = botService.onRateCommandReceived(rateCommand);
                                sendMessage(chatId, answerOnRateCommand);
                            } else sendMessage(chatId, "Please check the format of command: \"/rate EUR to USD\"");
                        } else

                            // Subscribe Command
                            if (messageText.startsWith("/subscribe")) {
                                if ((messageText.matches("/subscribe\\s+[a-zA-Z]{3}\\s+to\\s+[a-zA-Z]{3}"))) {
                                    SubscribeCommand subscribeCommand = new SubscribeCommand(user, messageText);
                                    String answerOnSubscribeCommand = botService.onSubscribeCommandReceived(subscribeCommand);
                                    sendMessage(chatId, answerOnSubscribeCommand);
                                } else
                                    sendMessage(chatId, "Please check the format of command: \"/subscribe EUR to USD\"");
                            } else

                                // Unsubscribe Command
                                if (messageText.startsWith("/unsubscribe")) {
                                    if ((messageText.matches("/unsubscribe\\s+[a-zA-Z]{3}\\s+to\\s+[a-zA-Z]{3}"))) {
                                        UnsubscribeCommand unsubscribeCommand = new UnsubscribeCommand(user, messageText);
                                        String answerOnUnsubscribeCommand = botService.onUnsubscribeCommandReceived(unsubscribeCommand);
                                        sendMessage(chatId, answerOnUnsubscribeCommand);
                                    } else
                                        sendMessage(chatId, "Please check the format of command: \"/unsubscribe EUR to USD\"");
                                } else sendMessage(chatId, UNKNOWN_COMMAND_TEXT);
        }
    }

    // Method for sending Messages to Users
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Replied to User with chatId: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    // Method returns a User object
    private User getUser(Message message) {

        User user = new User();
        user.setChatId(message.getChatId());
        user.setFirstName(message.getChat().getFirstName());
        user.setLastName(message.getChat().getLastName());
        user.setUserName(message.getChat().getUserName());

        return user;
    }

    // 1 Hour Scheduler
    @Scheduled(cron = "${bot.cron.scheduler.1h}")
    private void getRatePairsScheduler1() {

        Iterable<User> userList = userRepository.findAll();
        userList.forEach(user -> {
            List<RatePair> ratePairList = ratePairRepository.findByUser(user);
            long chatId = user.getChatId();
            ratePairList.forEach(ratePair -> {
                String messageTextToSend = "/rate " + ratePair.getFromCurrency() + " to " + ratePair.getToCurrency();
                RateCommand rateCommand = new RateCommand(user, messageTextToSend);
                sendMessage(chatId, botService.onRateCommandReceived(rateCommand));
            });
        });
    }

    // 1 Day Scheduler
    //@Scheduled(cron = "${bot.cron.scheduler.24h}")
    private void getRatePairsScheduler24() {

        Iterable<User> userList = userRepository.findAll();
        userList.forEach(new Consumer<User>() {
            @Override
            public void accept(User user) {
                List<RatePair> ratePairList = ratePairRepository.findByUser(user);
                long chatId = user.getChatId();
                ratePairList.forEach(new Consumer<RatePair>() {
                    @Override
                    public void accept(RatePair ratePair) {
                        String messageTextToSend = "/rate " + ratePair.getFromCurrency() + " to " + ratePair.getToCurrency();
                        RateCommand rateCommand = new RateCommand(user, messageTextToSend);
                        sendMessage(chatId, botService.onRateCommandReceived(rateCommand));
                    }
                });
            }
        });
    }
}