package telran.project.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import telran.project.dto.AddSubscribeCommand;
import telran.project.dto.HelpCommand;
import telran.project.dto.StartCommand;
import telran.project.dto.StopCommand;
import telran.project.entity.User;
import telran.project.repository.UserRepository;


import java.util.Date;

@Service
//@Component
// @Setter
// @RequiredArgsConstructor
public class BotService {

    private MessageSender sender;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void setMessageSender(@Lazy MessageSender sender) {
        this.sender = sender;
    }


    public String onStartCommandReceived(StartCommand startCommand) // StartCommand - DTO
    {
        var chatId = startCommand.getChatId();
        var chat = startCommand.getChat();
        String name = chat.getFirstName();

        if (userRepository.findById(startCommand.getChatId()).isEmpty()) {
            User user = new User();
            user.setChatId(chatId);
            user.setName(name);
            user.setRegisteredTime(new Date(System.currentTimeMillis()));
            userRepository.save(user);
            return "Hello " + name + ", You have been registered in the service";
        }

        return name + ", You are already registered in this bot";

    }

    public String onStopCommandReceived(StopCommand stopCommand) {
        var chatId = stopCommand.getChatId();
        var chat = stopCommand.getChat();
        String name = chat.getFirstName();
        userRepository.deleteById(chatId);
        return "Bye " + name + ", You have finished communicating with this bot";
    }

    public String onHelpCommandReceived(HelpCommand helpCommand) {
        var chatId = helpCommand.getChatId();
        if(userRepository.findById(chatId).isEmpty())
            return "To start chatting with this bot, please use the start command";
        return helpCommand.getTextToSend();
    }


    public String onAddSubscribeCommand(AddSubscribeCommand addSubscribeCommand) {
        var chatId = addSubscribeCommand.getChatId();
        if(userRepository.findById(chatId).isEmpty())
            return "To start chatting with this bot, please use the start command";
 //       if(addSubscribeCommand.getUpdate().getMessage().isCommand()){
         //TODO
 //       }
//        messageSender.send(chatId, "Write the name of the channel you want to subscribe to");
//        textProcessor.addListener(chatId, text -> subscribe(chatId, text));
    }



//        public Result onUnsubscribeCommandReceived (RemoveSubscribeCommand command) // UnsubscribeCommand - DTO
//        {
//            // command.userdid
//            // command.chanelid
//            // TODO update repositories
//            return null;
//        }


//    private void list(long chatId) {
//        Chat chat = chatService.getByTelegramId(chatId);
//        List<String> channels = subscriptionService.getBySubscriber(chat).stream()
//                .map(SubscriptionAsSubscriber::getChannel)
//                .map(ChannelAsSubscriber::getName)
//                .collect(Collectors.toList());
//        if (channels.isEmpty()) {
//            messageSender.send(chatId, "You currently aren't subscribed to any channel");
//        } else {
//            messageSender.send(chatId, "You are subscribed to:\n * " +
//                    String.join("\n * ", channels));
//        }
//    }



//    private void subscribe(long chatId, String channelName) {
//        Chat chat = chatService.getByTelegramId(chatId);
//        Channel channel = channelService.getByName(channelName);
//        subscriptionService.subscribe(new SubscriptionCreate(chat, channel));
//        messageSender.send(chatId, "You have subscribed to channel " + channel.getName());
//    }

//    private void unsubscribe(long chatId) {
//        messageSender.send(chatId, "Write the name of the channel you want to unsubscribe from");
//        textProcessor.addListener(chatId, text -> unsubscribe(chatId, text));
//    }

//    private void unsubscribe(long chatId, String channelName) {
//        Chat chat = chatService.getByTelegramId(chatId);
//        Channel channel = channelService.getByName(channelName);
//        subscriptionService.unsubscribe(chat, channel);
//        messageSender.send(chatId, "You have unsubscribed from channel " + channel.getName());
//    }

//    private void create(long chatId) {
//        messageSender.send(chatId, "Write the name of the channel you want to create");
//        textProcessor.addListener(chatId, text -> create(chatId, text));
//    }

//    private void create(long chatId, String channelName) {
//        Chat chat = chatService.getByTelegramId(chatId);
//        ChannelAsAdmin channel = channelService.create(new ChannelCreate(channelName, chat, false));
//        messageSender.send(chatId, "You have successfully created channel\n" + formatChannel(channel));
//    }

//    private void admin(long chatId) {
//        Chat chat = chatService.getByTelegramId(chatId);
//        List<String> channels = channelService.getByAdmin(chat).stream()
//                .map(this::formatChannel)
//                .collect(Collectors.toList());
//        if (channels.isEmpty()) {
//            messageSender.send(chatId, "You haven't created any channel yet");
//        } else {
//            messageSender.send(chatId, "Your channels:\n\n" + String.join("\n\n", channels));
//        }
//    }

//    private String formatChannel(ChannelAsAdmin channel) {
//        return String.format("%s\ntoken: %s\nsubscription code: %s", channel.getName(),
//                channel.getToken(), channel.getSubscriptionCode() == null ? "<public>" : channel.getSubscriptionCode());
//    }


}