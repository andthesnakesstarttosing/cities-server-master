package by.bsu.cities.repository;

import by.bsu.cities.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void testFindById_WhenCityExists_ThenReturnCity() {
        CityEntity city = new CityEntity(1, "New York", "image");
        cityRepository.save(city);

        Optional<CityEntity> result = cityRepository.findById(city.getId());

        assertTrue(result.isPresent());
        assertEquals(city.getTitle(), result.get().getTitle());
        assertEquals(city.getPhotoUrl(), result.get().getPhotoUrl());
    }

    @Test
    public void testFindById_WhenCityDoesNotExist_ThenReturnEmptyOptional() {
        Optional<CityEntity> result = cityRepository.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSave_WhenCityIsNew_ThenSaveCity() {
        CityEntity city = new CityEntity(1, "New York", "image");
        CityEntity savedCity = cityRepository.save(city);

        assertEquals(city.getTitle(), savedCity.getTitle());
        assertEquals(city.getPhotoUrl(), savedCity.getPhotoUrl());
    }

    @Test
    public void testSave_WhenCityAlreadyExists_ThenUpdateCity() {
        CityEntity city = new CityEntity(1, "New York", "image");
        cityRepository.save(city);

        CityEntity updatedCity = new CityEntity(1, "Yorkshire", "image");
        CityEntity savedCity = cityRepository.save(updatedCity);

        assertEquals(city.getId(), savedCity.getId());
        assertEquals(updatedCity.getTitle(), savedCity.getTitle());
        assertEquals(updatedCity.getPhotoUrl(), savedCity.getPhotoUrl());
    }

    @Test
    public void testFindByTitleContainingIgnoreCase_WhenMatchingTitleExists_ThenReturnMatchingCities() {
        CityEntity city1 = new CityEntity(1, "New York", "image");
        CityEntity city2 = new CityEntity(2, "Yorkshire", "image");
        CityEntity city3 = new CityEntity(3, "Los Angeles", "image");
        cityRepository.saveAll(Arrays.asList(city1, city2, city3));

        Page<CityEntity> result = cityRepository.findByTitleContainingIgnoreCase("york", PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().contains(city1));
        assertTrue(result.getContent().contains(city2));
        assertFalse(result.getContent().contains(city3));
    }

    @Test
    public void testFindByTitleContainingIgnoreCase_WhenNoMatchingTitleExists_ThenReturnEmptyList() {

        CityEntity city1 = new CityEntity(1, "New York", "image");
        CityEntity city2 = new CityEntity(2, "Yorkshire", "image");
        cityRepository.saveAll(Arrays.asList(city1, city2));

        Page<CityEntity> result = cityRepository.findByTitleContainingIgnoreCase("chicago", PageRequest.of(0, 10));

        assertEquals(0, result.getTotalElements());
        assertFalse(result.getContent().contains(city1));
        assertFalse(result.getContent().contains(city2));
    }
}
