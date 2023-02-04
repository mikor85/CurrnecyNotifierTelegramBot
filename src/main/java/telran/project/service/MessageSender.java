package telran.project.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telran.project.controller.BotController;


public interface MessageSender {

    void sendMessage(long chatId, String textToSend);

}

//    @Autowired
//    private final BotController bot;

//    public void sendMessage(long chatId, String textToSend) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(textToSend);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
////            log.error("Error occurred: " + e.getMessage());
//        }
//    }
//}
