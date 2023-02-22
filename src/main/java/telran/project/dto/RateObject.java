package telran.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RateObject {

    private String code;
    private String alphaCode;
    private Integer numericCode;
    private String name;
    private Double rate;
    private String date;
    private Double inverseRate;
}