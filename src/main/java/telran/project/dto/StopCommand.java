package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import telran.project.entity.User;

@Getter
@NoArgsConstructor
@Component
public class StopCommand {

    User user;

    static final String STOP_TEXT_INFO = "You are not registered to the service.";

    public StopCommand(User user) {
        this.user = user;
    }

    public String getStopTextInfo() {
        return STOP_TEXT_INFO;
    }
}