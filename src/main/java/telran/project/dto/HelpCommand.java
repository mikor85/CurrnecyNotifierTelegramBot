package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import telran.project.entity.User;

@Getter
@NoArgsConstructor
@Component
public class HelpCommand {

    User user;

    static final String HELP_TEXT = """
            This bot is created to show the currency rates.

            You can execute commands from the main menu on the left or by typing command:
                        
            Type /start to see a welcome message;
                        
            Type /rate to make a request for currency rates;
            
            Type /subscribe to receive currency rate for the subscribed pairs;
            
            Type /unsubscribe to stop receiving currency rate for the interested pair;
                        
            Type /help to see this message again.
            """;

    static final String HELP_START_COMMAND_TEXT = "To start chatting with this bot, please use the \"/start\" command.";

    public HelpCommand(User user) {
        this.user = user;
    }

    public String getHelpText() {
        return HELP_TEXT;
    }

    public String getHelpStartCommandText() {
        return HELP_START_COMMAND_TEXT;
    }
}