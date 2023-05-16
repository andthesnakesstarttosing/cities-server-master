package by.bsu.cities.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CityEntity {
    @Id
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String photoUrl;
}
