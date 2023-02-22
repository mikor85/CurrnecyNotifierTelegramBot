package telran.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "pairs")
@Getter
@ToString
public class RatePair {

    @Id
    String ratePairId;

    String fromCurrency;

    String toCurrency;

    // Link table
    @ManyToOne(
            fetch = FetchType.LAZY
            //cascade = {CascadeType.REFRESH}
    )
    // для создания колонки, в которой будет храниться chaId пользователя, тип Long - взят из User.
    @JoinColumn(name = "chat_id"/*, nullable = false*/)
    @JsonIgnore
    private User user;

    public RatePair() {
        ratePairId = "";
    }

    public RatePair(String fromCurrency, String toCurrency, User user) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.user = user;
        generateRatePairId();
    }

    public void setRatePairId(String ratePairId) {
        this.ratePairId = ratePairId;
        generateRatePairId();
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
        generateRatePairId();
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
        generateRatePairId();
    }

    public void setUser(User user) {
        this.user = user;
        generateRatePairId();
    }

    private void generateRatePairId() {

        ratePairId = (user == null ? "" : user.getChatId()) + "|" + fromCurrency + "|" + toCurrency;
    }
}