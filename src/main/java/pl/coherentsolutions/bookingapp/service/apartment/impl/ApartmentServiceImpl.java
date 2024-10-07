package pl.coherentsolutions.bookingapp.service.apartment.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.repository.ApartmentRepository;
import pl.coherentsolutions.bookingapp.service.apartment.ApartmentService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;

    @Override
    @Transactional
    public void saveOrUpdateApartment(ApartmentEntity apartment) {
        apartmentRepository.save(apartment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApartmentEntity> getApartmentById(Integer id) {
        return apartmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApartmentEntity> getAllApartments() {
        return (List<ApartmentEntity>) apartmentRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteApartment(Integer id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ApartmentEntity findExistingApartment(OrderDto orderDto) {
        return getApartmentById(orderDto.getApartmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Apartment whit id " + orderDto.getApartmentId() + " not found"));
    }
}
