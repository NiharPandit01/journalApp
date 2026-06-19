package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private String resolvedAddress;
    private String address;
    private String timezone;
    private String description;
    private CurrentConditions currentConditions;
    private List<Day> days;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentConditions {
        private String datetime;
        private double temp;
        private double feelslike;
        private double humidity;
        private double windspeed;
        private double pressure;
        private double visibility;
        private double uvindex;
        private String conditions;
        private String icon;
        private String sunrise;
        private String sunset;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Day {
        private String datetime;
        private double tempmax;
        private double tempmin;
        private double temp;
        private double feelslike;
        private double humidity;
        private double precipprob;
        private double windspeed;
        private double uvindex;
        private String conditions;
        private String description;
        private String icon;
    }
}