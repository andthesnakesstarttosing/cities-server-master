package by.bsu.cities.service;

import by.bsu.cities.dto.CityDto;
import by.bsu.cities.dto.CityEditDto;
import by.bsu.cities.entity.CityEntity;
import by.bsu.cities.exception.RestException;
import by.bsu.cities.mapper.CityMapper;
import by.bsu.cities.repository.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    public Page<CityDto> find(Pageable page, String name) {
        if (name.isEmpty()) {
            Page<CityEntity> result = this.cityRepository.findAll(page);
            return result.map(CityMapper.CITY_MAPPER::toDto);
        }

        Page<CityEntity> result = this.cityRepository.findByTitleContainingIgnoreCase(name, page);

        if (result.getTotalPages() <= page.getPageNumber() && result.getTotalPages() > 0) {
            int pageNumber = result.getTotalPages() - 1;
            PageRequest pageRequest = PageRequest.of(pageNumber, page.getPageSize());
            result = this.cityRepository.findByTitleContainingIgnoreCase(name, pageRequest);
        }
        return result.map(CityMapper.CITY_MAPPER::toDto);
    }

    public CityDto findById(long id) throws RestException {
        Optional<CityEntity> entity = this.cityRepository.findById(id);
        if (entity.isPresent()) {
            return CityMapper.CITY_MAPPER.toDto(entity.get());
        }
        throw new RestException("No city found with id " + id, HttpStatus.NOT_FOUND, "Not found");
    }

    public CityDto patchCity(long id, CityEditDto updates) throws RestException {
        Optional<CityEntity> cityOptional = cityRepository.findById(id);
        if (cityOptional.isEmpty()) {
            throw new RestException("No city found with id " + id, HttpStatus.NOT_FOUND, "Not found");
        }

        CityEntity existingCity = cityOptional.get();
        String title = updates.getTitle();
        if (title != null && !title.isEmpty()) {
            existingCity.setTitle(title);
        }

        String photoUrl = updates.getPhotoUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            if (!isValidUrl(photoUrl)) {
                throw new RestException("Invalid URL format for photoUrl: " + photoUrl, HttpStatus.BAD_REQUEST, "Bad request");
            }
            existingCity.setPhotoUrl(photoUrl);
        }

        CityEntity result = cityRepository.save(existingCity);
        return CityMapper.CITY_MAPPER.toDto(result);
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
