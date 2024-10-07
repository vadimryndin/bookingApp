package pl.coherentsolutions.bookingapp.service.weather.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;
import pl.coherentsolutions.bookingapp.model.entity.WeatherEntity;
import pl.coherentsolutions.bookingapp.repository.WeatherRepository;
import pl.coherentsolutions.bookingapp.service.user.UserService;
import pl.coherentsolutions.bookingapp.service.weather.WeatherService;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WebClient webClient;
    private final WeatherRepository weatherRepository;
    private final UserService userService;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Override
    public String showWeatherByCity(String city) {
        Optional<WeatherEntity> weatherEntity = weatherRepository.findByCity(city);

        if (weatherEntity.isPresent()) {
            WeatherEntity weatherEntityData = weatherEntity.get();
            LocalDateTime lastUpdated = weatherEntityData.getLastUpdated();
            if (lastUpdated != null && ChronoUnit.HOURS.between(lastUpdated, LocalDateTime.now()) < 1) {
                return weatherEntityData.getWeatherData();
            } else {
                String newWeatherData = showCurrentWeather(city).block();
                storeWeatherData(city, newWeatherData);
                return newWeatherData;
            }
        } else {
            String newWeatherData = showCurrentWeather(city).block();
            storeWeatherData(city, newWeatherData);
            return newWeatherData;
        }
    }

    @Scheduled(fixedRateString = "${openweathermap.update.interval}")
    public void updateWeatherDataScheduler() {
        for (UserEntity user : userService.getAllUsers()) {
            String city = user.getCity();
            String weatherData = showCurrentWeather(city).block();
            storeWeatherData(city, weatherData);
        }
    }

    private Mono<String> showCurrentWeather(String city) {
        return webClient.get()
                .uri(String.format("%s?q=%s&appid=%s", apiUrl, city, apiKey))
                .retrieve()
                .bodyToMono(String.class);
    }

    private void storeWeatherData(String city, String weatherData) {
        WeatherEntity weatherEntity = weatherRepository.findByCity(city)
                .orElse(new WeatherEntity());

        weatherEntity.setWeatherData(weatherData);
        weatherEntity.setCity(city);
        weatherEntity.setLastUpdated(LocalDateTime.now());

        weatherRepository.save(weatherEntity);
    }
}
