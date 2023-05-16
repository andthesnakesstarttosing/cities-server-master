package by.bsu.cities.service;

import by.bsu.cities.dto.CityDto;
import by.bsu.cities.dto.CityEditDto;
import by.bsu.cities.entity.CityEntity;
import by.bsu.cities.exception.RestException;
import by.bsu.cities.mapper.CityMapper;
import by.bsu.cities.repository.CityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    public void testFindPartialMatch() {
        List<CityEntity> cityEntities = Arrays.asList(
                new CityEntity(1, "New York", "image"),
                new CityEntity(2, "Yorkshire", "image")
        );
        Page<CityEntity> page = new PageImpl<>(cityEntities);

        Pageable pageable = PageRequest.of(0, 2);
        when(cityRepository.findByTitleContainingIgnoreCase(eq("york"), eq(pageable))).thenReturn(page);

        Page<CityDto> result = (Page<CityDto>) cityService.find(pageable, "york");
        List<CityDto> cityDtos = StreamSupport.stream(result.spliterator(), false).toList();

        assertEquals(2, cityDtos.size());
        assertTrue(cityDtos.stream().anyMatch(dto -> dto.getTitle().equals("New York")));
        assertTrue(cityDtos.stream().anyMatch(dto -> dto.getTitle().equals("Yorkshire")));
    }

    @Test
    public void testFindNoResults() {
        Page<CityEntity> page = new PageImpl<>(Collections.emptyList());

        Pageable pageable = PageRequest.of(0, 2);
        when(cityRepository.findByTitleContainingIgnoreCase(eq("unknown"), eq(pageable))).thenReturn(page);

        Iterable<CityDto> result = cityService.find(pageable, "unknown");
        List<CityDto> cityDtos = StreamSupport.stream(result.spliterator(), false).toList();

        assertTrue(cityDtos.isEmpty());
    }

    @Test()
    public void testFindInvalidPage() {
        CityService cityService = new CityService(cityRepository);

        Pageable pageable = PageRequest.of(0, 2);

        Assertions.assertThatThrownBy(() -> cityService.find(pageable, "unknown"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void findById() throws RestException {
        long cityId = 1L;
        CityEntity cityEntity = new CityEntity(cityId, "York", "image");
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(cityEntity));

        CityDto cityReturn = cityService.findById(cityId);

        Assertions.assertThat(cityReturn).isNotNull();
    }

    @Test()
    public void testFindByIdNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());
        CityService cityService = new CityService(cityRepository);
        Assertions.assertThatThrownBy(() -> cityService.findById(1L))
                .isInstanceOf(RestException.class);
    }

    @Test
    public void testPatchCitySuccess() throws RestException {
        CityEntity existingCity = new CityEntity(1L, "Initial Title", "https://updated.url");
        CityEditDto updates = new CityEditDto("Updated Title", "https://updated.url");
        CityEntity updatedCity = new CityEntity(1L, "Updated Title", "https://updated.url");

        when(cityRepository.findById(existingCity.getId())).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(updatedCity)).thenReturn(updatedCity);

        CityDto patchedCity = cityService.patchCity(existingCity.getId(), updates);

        assertEquals(updatedCity.getId(), patchedCity.getId());
        assertEquals(updatedCity.getTitle(), patchedCity.getTitle());
        assertEquals(updatedCity.getPhotoUrl(), patchedCity.getPhotoUrl());
    }

    @Test()
    public void testPatchCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> cityService.patchCity(1L, new CityEditDto()))
                .isInstanceOf(RestException.class);
    }

    @Test
    public void testPatchCityNoUpdates() throws RestException {
        CityEntity existingCity = new CityEntity(1L, "Initial Title", "https://updated.url");

        when(cityRepository.findById(existingCity.getId())).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(existingCity)).thenReturn(existingCity);

        CityDto patchedCity = cityService.patchCity(existingCity.getId(), new CityEditDto());

        assertEquals(CityMapper.CITY_MAPPER.toDto(existingCity), patchedCity);
    }
}
