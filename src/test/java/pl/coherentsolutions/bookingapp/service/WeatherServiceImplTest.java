package pl.coherentsolutions.bookingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;
import pl.coherentsolutions.bookingapp.model.entity.WeatherEntity;
import pl.coherentsolutions.bookingapp.repository.WeatherRepository;
import pl.coherentsolutions.bookingapp.service.user.UserService;
import pl.coherentsolutions.bookingapp.service.weather.impl.WeatherServiceImpl;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WeatherServiceImplTest {
    @Autowired
    private WeatherServiceImpl weatherService;

    @MockBean
    private WeatherRepository weatherRepository;

    @MockBean
    private WebClient webClient;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String CITY = "Wroclaw";
    private final String WEATHER_DATA = "Sunny, +30";

    @Test
    public void testShowWeatherByCity_whenDataExists_returnsExistingData_withoutSaving() {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setCity(CITY);
        weatherEntity.setWeatherData(WEATHER_DATA);
        weatherEntity.setLastUpdated(LocalDateTime.now());

        when(weatherRepository.findByCity(CITY)).thenReturn(Optional.of(weatherEntity));

        String result = weatherService.showWeatherByCity(CITY);

        assertEquals(WEATHER_DATA, result);
        verify(weatherRepository, never()).save(any(WeatherEntity.class));
    }

    @Test
    public void testShowWeatherByCity_whenDataNotFound_fetchesAndSavesNewData() {
        when(weatherRepository.findByCity(CITY)).thenReturn(Optional.empty());
        runWebClientMock();

        String result = weatherService.showWeatherByCity(CITY);

        assertEquals(WEATHER_DATA, result);
        verify(weatherRepository).save(any(WeatherEntity.class));
    }

    @Test
    public void testUpdateWeatherDataScheduler() {
        UserEntity user = new UserEntity();
        user.setCity(CITY);

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        runWebClientMock();

        weatherService.updateWeatherDataScheduler();
        verify(weatherRepository).save(any(WeatherEntity.class));
    }

    private void runWebClientMock() {
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(WEATHER_DATA));
    }
}
