package telran.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.project.dto.*;
import telran.project.dto.RateObject;
import telran.project.entity.RatePair;
import telran.project.entity.User;
import telran.project.repository.RatePairRepository;
import telran.project.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@NoArgsConstructor
public class BotService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatePairRepository ratePairRepository;

    // StartCommand Processing
    public String onStartCommandReceived(StartCommand startCommand) {

        User user = startCommand.getUser();

        if (userRepository.findById(user.getChatId()).isEmpty()) {

            userRepository.save(user);
            log.info(user + " has been saved.");

            return EmojiParser.parseToUnicode(
                    "Hello, " + user.getFirstName() + ", nice to meet you!" + " :blush:");
        }
        return user.getFirstName() + ", you have been already registered.";
    }

    // StopCommand Processing
    public String onStopCommandReceived(StopCommand stopCommand) {
        User user = stopCommand.getUser();
        var chatId = user.getChatId();
        String name = user.getFirstName();

        if (userRepository.findById(chatId).isEmpty()) {
            log.info("Bot doesn't know User: " + user);
            return stopCommand.getStopTextInfo();
        }

        // удаляем из бд все данные
//        List<RatePair> ratePairList = ratePairRepository.findByUser(user);
//        ratePairRepository.deleteAll(ratePairList);
        //userRepository.deleteById(chatId);
        userRepository.delete(user);

        return "Bye " + name + ", you have finished communicating with this bot.\n\n" +
                "All your data and subscriptions have been deleted.";
    }

    // HelpCommand Processing
    public String onHelpCommandReceived(HelpCommand helpCommand) {
        var chatId = helpCommand.getUser().getChatId();

        if (userRepository.findById(chatId).isEmpty()) {
            return helpCommand.getHelpStartCommandText();
        }

        return helpCommand.getHelpText();
    }

    // RateCommand Processing
    public String onRateCommandReceived(RateCommand rateCommand) {

        // Check if User is registered to the bot service
        if (userRepository.findById(rateCommand.getUser().getChatId()).isEmpty()){
            return rateCommand.getRateStartText();
        }

        String fromCurrency = rateCommand.getMessage().toLowerCase().substring(6, 9);
        String toCurrency = rateCommand.getMessage().toLowerCase().substring(13, 16);

        // Read a JSON
        // http://www.floatrates.com/daily/eur.json
        Double rateValue = getRateValue(fromCurrency, toCurrency);

        return "1 " + fromCurrency.toUpperCase() + " = " + rateValue + " " + toCurrency.toUpperCase();
    }

    // Method for forming a URL, getting JSON and returning the value of the exchange rate
    private Double getRateValue(String fromCurrency, String toCurrency) {
        String urlAddress = "http://www.floatrates.com/daily/" + fromCurrency + ".json";
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());

            // process json
            return getRateFromJSONRateObject(json, toCurrency);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1D;
    }

    // Method for processing the received json object,
    // generating and searching for the required RateObject, returning the rate
    private Double getRateFromJSONRateObject(JSONObject json, String toCurrency) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, RateObject> stringRateObjectMap = null;
        try {
            stringRateObjectMap = mapper.readValue(json.toString(), new TypeReference<>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (stringRateObjectMap == null) {
            return -1D;
        }
        RateObject rateObject = stringRateObjectMap.get(toCurrency);
        return rateObject.getRate();
    }

    // SubscribeCommand Processing
    public String onSubscribeCommandReceived(SubscribeCommand subscribeCommand) {

        // Check if User is registered to the bot service
        if (userRepository.findById(subscribeCommand.getUser().getChatId()).isEmpty()){
            return subscribeCommand.getSubStartText();
        }

        String fromCurrency = subscribeCommand.getMessage().toLowerCase().substring(11, 14);
        String toCurrency = subscribeCommand.getMessage().toLowerCase().substring(18, 21);
        RatePair ratePair = getRatePair(subscribeCommand.getUser(), fromCurrency, toCurrency);

        // добавить в БД пару к соответствующему пользователю
        if (ratePairRepository.findById(ratePair.getRatePairId()).isEmpty()) {
            ratePairRepository.save(ratePair);
            // изменяем поле isActive на true
            User user = subscribeCommand.getUser();
            user.setIsActive(true);
            userRepository.save(user);
            return subscribeCommand.getSubInfoText();
        } else return subscribeCommand.getSubAlreadyText();
    }

    // UnsubscribeCommand Processing
    public String onUnsubscribeCommandReceived(UnsubscribeCommand unsubscribeCommand) {

        // Check if User is registered to the bot service
        if (userRepository.findById(unsubscribeCommand.getUser().getChatId()).isEmpty()){
            return unsubscribeCommand.getUnsubStartText();
        }

        String fromCurrency = unsubscribeCommand.getMessage().toLowerCase().substring(13, 16);
        String toCurrency = unsubscribeCommand.getMessage().toLowerCase().substring(20, 23);
        RatePair ratePair = getRatePair(unsubscribeCommand.getUser(), fromCurrency, toCurrency);

        // проверить, есть ли такая пара в базе данных
        if (ratePairRepository.findById(ratePair.getRatePairId()).isEmpty()) {
            return unsubscribeCommand.getUnsubNoInfo();
        }
        // удалить из базы данных пару
        ratePairRepository.delete(ratePair);
        // проверяем, если у пользователя кол-во подписок > 0,
        // если < 0, изменить поле isActive на false
        User user = unsubscribeCommand.getUser();
        List<RatePair> ratePairList = ratePairRepository.findByUser(user);
        if (ratePairList.size() == 0) {
            user.setIsActive(false);
            userRepository.save(user);
        }
        return unsubscribeCommand.getUnsubInfo();
    }

    private RatePair getRatePair(User user, String fromCurrency, String toCurrency) {

        RatePair ratePair = new RatePair();
        ratePair.setFromCurrency(fromCurrency);
        ratePair.setToCurrency(toCurrency);
        ratePair.setUser(user);

        return ratePair;
    }
}