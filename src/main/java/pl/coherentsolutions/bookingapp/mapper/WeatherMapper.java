package pl.coherentsolutions.bookingapp.mapper;

import org.mapstruct.Mapper;
import pl.coherentsolutions.bookingapp.model.dto.WeatherDto;
import pl.coherentsolutions.bookingapp.model.entity.WeatherEntity;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    WeatherDto weatherEntityToDto(WeatherEntity weatherEntity);
    WeatherEntity weatherDtoToEntity(WeatherDto weatherDto);
}
