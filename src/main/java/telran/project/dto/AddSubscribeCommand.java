package telran.project.dto;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Component
public class AddSubscribeCommand implements Command {
    private Long chatId;
    private Update update;

    public AddSubscribeCommand(Update update) {
        this.update = update;
        execute(update);
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
    }
}
