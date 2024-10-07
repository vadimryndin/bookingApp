package pl.coherentsolutions.bookingapp.service.hotel;

import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    List<HotelEntity> getAllHotels();
    Optional<HotelEntity> getHotelById(Integer id);
    void saveOrUpdateHotel(HotelEntity hotel);
    void deleteHotel(Integer id);
}
