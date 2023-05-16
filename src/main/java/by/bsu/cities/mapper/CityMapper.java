package by.bsu.cities.mapper;

import by.bsu.cities.dto.CityDto;
import by.bsu.cities.entity.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CityMapper {
    CityMapper CITY_MAPPER = Mappers.getMapper(CityMapper.class);
    CityDto toDto(CityEntity city);
}
