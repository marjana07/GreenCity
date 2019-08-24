package greencity.dto.place;

import greencity.dto.location.LocationDto;
import greencity.dto.openingHours.OpeningHoursDto;
import greencity.entity.Place;
import greencity.entity.enums.PlaceStatus;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@RequiredArgsConstructor
public class PlaceAddDto {

    @NotBlank
    @Length(max = 30)
    private String name;

    @NotBlank
    @Length(max = 30)
    private String address;

    @NotNull private LocationDto locationDto;

    @NotNull private Long categoryId;

    @NotNull private Long authorId;

    @NotNull private List<OpeningHoursDto> openingHoursDtoList;

    @NotNull private PlaceStatus placeStatus = PlaceStatus.PROPOSED;

    public PlaceAddDto(Place place) {
        this.name = place.getName();
        this.locationDto = new LocationDto(place.getLocation());
        this.categoryId = place.getCategory().getId();
        this.authorId = place.getAuthor().getId();
        this.openingHoursDtoList =
                place.getOpeningHours().stream()
                        .map(OpeningHoursDto::new)
                        .collect(Collectors.toList());
        this.placeStatus = place.getStatus();
    }
}
