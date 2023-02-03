package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Component
public class HelpCommand implements Command {

    private final String textToSend = "This bot is used to demonstrate the compatibility of telegrams with Spring Java";
    private Long chatId;
    private Update update;

    public String getTextToSend() {
        return textToSend;
    }

    public HelpCommand(Update update) {
        this.update = update;
        execute(update);
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
    }
}
