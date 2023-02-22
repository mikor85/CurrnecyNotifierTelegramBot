package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import telran.project.entity.User;

@Getter
@NoArgsConstructor
@Component
public class SubscribeCommand {

    User user;

    String message;

    static final String SUB_START_TEXT = "You are not subscribed to the bot service. Please use /start command.";
    static final String SUB_INFO_TEXT = "You subscribed to the specified currency pair.";
    static final String SUB_ERROR_TEXT = "Something went wrong, please try again later.";
    static final String SUB_ALREADY_TEXT = "You have been already subscribed to the specified currency pair.";

    public SubscribeCommand(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getSubStartText() {
        return SUB_START_TEXT;
    }

    public String getSubInfoText() {
        return SUB_INFO_TEXT;
    }

    public String getSubErrorText() {
        return SUB_ERROR_TEXT;
    }

    public String getSubAlreadyText() {
        return SUB_ALREADY_TEXT;
    }
}