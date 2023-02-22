package telran.project.currencynotifiertelegrambot;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import telran.project.dto.*;
import telran.project.entity.User;
import telran.project.repository.RatePairRepository;
import telran.project.repository.UserRepository;
import telran.project.service.BotService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatePairRepository ratePairRepository;

    @Autowired
    BotService botService;

    @After
    public void dropAllData() {
        ratePairRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testOnStartCommandReceivedGreetings() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);

        assertEquals("Hello, Ivan, nice to meet you! \uD83D\uDE0A",
                botService.onStartCommandReceived(new StartCommand(testUserIvan)));
    }

    @Test
    public void testOnStartCommandReceivedAlreadyRegistered() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));

        assertEquals("Ivan, you have been already registered.",
                botService.onStartCommandReceived(new StartCommand(testUserIvan)));
    }

    @Test
    public void testOnStopCommandReceivedNotRegisteredMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);

        assertEquals("You are not registered to the service.",
                botService.onStopCommandReceived(new StopCommand(testUserIvan)));
    }

    @Test
    public void testOnStopCommandReceivedByeMessageUserRep() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));

        assertEquals("""
                        Bye Ivan, you have finished communicating with this bot.

                        All your data and subscriptions have been deleted.""",
                botService.onStopCommandReceived(new StopCommand(testUserIvan)));
    }

    @Test
    public void testOnStopCommandReceivedByeMessageUserAndRatePairRep() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));
        botService.onSubscribeCommandReceived(new SubscribeCommand(testUserIvan, "/subscribe EUR to USD"));
        botService.onSubscribeCommandReceived(new SubscribeCommand(testUserIvan, "/subscribe EUR to RUB"));

        assertEquals("""
                        Bye Ivan, you have finished communicating with this bot.

                        All your data and subscriptions have been deleted.""",
                botService.onStopCommandReceived(new StopCommand(testUserIvan)));
    }

    @Test
    public void testOnHelpCommandReceivedGetStartCommandMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);

        if (userRepository.findById(testUserIvan.getChatId()).isEmpty()) {
            assertEquals("To start chatting with this bot, please use the \"/start\" command.",
                    botService.onHelpCommandReceived(new HelpCommand(testUserIvan))
            );
        }
    }

    @Test
    public void testOnHelpCommandReceivedGetHelpMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));

        assertEquals("""
                        This bot is created to show the currency rates.

                        You can execute commands from the main menu on the left or by typing command:
                                    
                        Type /start to see a welcome message;
                                    
                        Type /rate to make a request for currency rates;
                                    
                        Type /subscribe to receive currency rate for the subscribed pairs;
                                                
                        Type /unsubscribe to stop receiving currency rate for the interested pair;
                                    
                        Type /help to see this message again.
                        """,
                botService.onHelpCommandReceived(new HelpCommand(testUserIvan))
        );
    }

    @Test
    public void testOnRateCommandReceivedGetNotRegisteredMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);

        assertEquals(
                "You are not subscribed to the bot service. Please use /start command.",
                botService.onRateCommandReceived(new RateCommand(testUserIvan, "/rate eur to rub"))
        );
    }

    @Test
    public void testOnRateCommandReceivedGetActualRate() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));
        // 1 EUR = 81.773905154778 RUB
        assertEquals(
                "1 EUR = 81.773905154778 RUB",
                botService.onRateCommandReceived(new RateCommand(testUserIvan, "/rate eur to rub"))
        );
    }

    @Test
    public void testOnSubscribeCommandReceivedUnregisteredUser() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        // You are not subscribed to the bot service. Please use /start command.
        assertEquals(
                "You are not subscribed to the bot service. Please use /start command.",
                botService.onSubscribeCommandReceived(
                        new SubscribeCommand(testUserIvan, "/subscribe eur to rub")
                )
        );
    }

    @Test
    public void testOnSubscribeCommandReceivedGetSubMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));

        assertEquals(
                "You subscribed to the specified currency pair.",
                botService.onSubscribeCommandReceived(
                        new SubscribeCommand(testUserIvan, "/subscribe eur to rub")
                ));
    }

    @Test
    public void testOnSubscribeCommandReceivedGetSubAlreadyMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));
        botService.onSubscribeCommandReceived(
                new SubscribeCommand(testUserIvan, "/subscribe eur to rub"));

        assertEquals(
                "You have been already subscribed to the specified currency pair.",
                botService.onSubscribeCommandReceived(
                        new SubscribeCommand(testUserIvan, "/subscribe eur to rub")
                ));
    }

    @Test
    public void testOnUnsubscribeCommandReceivedGetNotRegisteredMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);

        assertEquals(
                "You are not subscribed to the bot service. Please use /start command.",
                botService.onUnsubscribeCommandReceived(
                        new UnsubscribeCommand(testUserIvan, "/rate eur to rub")
                ));
    }

    @Test
    public void testOnUnsubscribeCommandReceivedGetNotSubscribedMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));

        // You are not subscribed to the specified currency pair.
        assertEquals(
                "You are not subscribed to the specified currency pair.",
                botService.onUnsubscribeCommandReceived(
                        new UnsubscribeCommand(testUserIvan, "/unsubscribe eur to rub")
                )
        );
    }

    @Test
    public void testOnUnsubscribeCommandReceivedGetUnsubscribedMessage() {

        User testUserIvan = new User(100000L, "Ivan", "Ivanov", null, false);
        botService.onStartCommandReceived(new StartCommand(testUserIvan));
        botService.onSubscribeCommandReceived(new SubscribeCommand(testUserIvan, "/subscribe eur to rub"));

        assertEquals(
                "You have unsubscribed from the specified currency pair.",
                botService.onUnsubscribeCommandReceived(
                        new UnsubscribeCommand(testUserIvan, "/unsubscribe eur to rub")
                )
        );
    }
}