package pl.coherentsolutions.bookingapp.service.weather;

public interface WeatherService {
    String showWeatherByCity(String city);
    void updateWeatherDataScheduler();
}
