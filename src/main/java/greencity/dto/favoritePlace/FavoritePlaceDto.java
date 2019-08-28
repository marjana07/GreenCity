package greencity.dto.favoritePlace;

import greencity.dto.place.PlaceIdDto;
import greencity.dto.user.UserEmailAndIdDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FavoritePlaceDto {
    @NotBlank
    @Length(max = 30)
    private String name;

    private PlaceIdDto place;

    private UserEmailAndIdDto user;
}
