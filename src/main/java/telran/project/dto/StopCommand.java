package telran.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Component
public class StopCommand implements Command {

    private Long chatId;
    private Chat chat;
    private Update update;

    public StopCommand(Update update) {
        this.update = update;
        execute(update);
    }

    @Override
    public void execute(Update update) {
        chatId = update.getMessage().getChatId();
        chat = update.getMessage().getChat();
    }
}

//    @Override
//    public Long getChatId() {
//        return chatId;
//    }
