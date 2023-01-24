package dto;

public class StartCommand {
    private Long chatId;
    private String userName;

    public StartCommand(Long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }
}