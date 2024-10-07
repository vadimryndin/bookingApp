package pl.coherentsolutions.bookingapp.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.dto.WeatherDto;
import pl.coherentsolutions.bookingapp.model.entity.WeatherEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T14:54:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class WeatherMapperImpl implements WeatherMapper {

    @Override
    public WeatherDto weatherEntityToDto(WeatherEntity weatherEntity) {
        if ( weatherEntity == null ) {
            return null;
        }

        WeatherDto weatherDto = new WeatherDto();

        weatherDto.setId( weatherEntity.getId() );
        weatherDto.setCity( weatherEntity.getCity() );
        weatherDto.setWeatherData( weatherEntity.getWeatherData() );
        weatherDto.setLastUpdated( weatherEntity.getLastUpdated() );

        return weatherDto;
    }

    @Override
    public WeatherEntity weatherDtoToEntity(WeatherDto weatherDto) {
        if ( weatherDto == null ) {
            return null;
        }

        WeatherEntity weatherEntity = new WeatherEntity();

        weatherEntity.setId( weatherDto.getId() );
        weatherEntity.setCity( weatherDto.getCity() );
        weatherEntity.setWeatherData( weatherDto.getWeatherData() );
        weatherEntity.setLastUpdated( weatherDto.getLastUpdated() );

        return weatherEntity;
    }
}
