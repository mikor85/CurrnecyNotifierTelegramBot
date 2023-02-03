package telran.project.dto;

import org.telegram.telegrambots.meta.api.objects.Update;


public interface Command {

    void execute(Update update);

//    Long getChatId();

}
