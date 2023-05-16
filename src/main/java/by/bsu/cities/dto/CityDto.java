package by.bsu.cities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    public long id;
    public String title;
    public String photoUrl;
}
