package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import telran.project.entity.User;

@Getter
@NoArgsConstructor
@Component
public class UnsubscribeCommand {

    User user;

    String message;

    static final String UNSUB_START_TEXT = "You are not subscribed to the bot service. Please use /start command.";
    static final String UNSUB_NO_INFO = "You are not subscribed to the specified currency pair.";
    static final String UNSUB_INFO = "You have unsubscribed from the specified currency pair.";

    public UnsubscribeCommand(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUnsubStartText() {
        return UNSUB_START_TEXT;
    }

    public String getUnsubNoInfo() {
        return UNSUB_NO_INFO;
    }

    public String getUnsubInfo() {
        return UNSUB_INFO;
    }
}