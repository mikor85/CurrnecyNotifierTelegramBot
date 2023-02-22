package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import telran.project.entity.User;

@Getter
@NoArgsConstructor
@Component
public class RateCommand {

    User user;

    String message;

    static final String RATE_START_TEXT = "You are not subscribed to the bot service. Please use /start command.";

    public RateCommand(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getRateStartText() {
        return RATE_START_TEXT;
    }
}