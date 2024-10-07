package pl.coherentsolutions.bookingapp.service.hotel.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;
import pl.coherentsolutions.bookingapp.repository.HotelRepository;
import pl.coherentsolutions.bookingapp.service.hotel.HotelService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HotelEntity> getAllHotels() {
        return (List<HotelEntity>) hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelEntity> getHotelById(Integer id) {
        return hotelRepository.findById(id);
    }

    @Override
    @Transactional
    public void saveOrUpdateHotel(HotelEntity hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public void deleteHotel(Integer id) {
        hotelRepository.deleteById(id);
    }
}
