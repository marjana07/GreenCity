package greencity.controller;

import greencity.dto.PageableDto;
import greencity.dto.favoriteplace.FavoritePlaceDto;
import greencity.dto.filter.FilterPlaceDto;
import greencity.dto.place.*;
import greencity.entity.Place;
import greencity.entity.enums.PlaceStatus;
import greencity.service.FavoritePlaceService;
import greencity.service.PlaceService;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/place")
@AllArgsConstructor
public class PlaceController {
    private final FavoritePlaceService favoritePlaceService;
    /**
     * Autowired PlaceService instance.
     */
    private PlaceService placeService;
    private ModelMapper modelMapper;

    /**
     * The controller which returns new proposed {@code Place} from user.
     *
     * @param dto - Place dto for adding with all parameters.
     * @return new {@code Place}.
     * @author Kateryna Horokh
     */
    @PostMapping("/propose")
    public ResponseEntity<PlaceWithUserDto> proposePlace(
        @Valid @RequestBody PlaceAddDto dto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                modelMapper.map(
                    placeService.save(dto, principal.getName()),
                    PlaceWithUserDto.class));
    }

    /**
     * The controller which returns new updated {@code Place}.
     *
     * @param dto - Place dto for updating with all parameters.
     * @return new {@code Place}.
     * @author Kateryna Horokh
     */
    @PutMapping("/update")
    public ResponseEntity<PlaceWithUserDto> updatePlace(
        @Valid @RequestBody PlaceUpdateDto dto, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(modelMapper.map(placeService.update(dto, principal.getName()), PlaceWithUserDto.class));
    }

    /**
     * Controller to get place info.
     *
     * @param id place
     * @return info about place
     */
    @GetMapping("/Info/{id}")
    public ResponseEntity<?> getInfo(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getInfoById(id));
    }

    /**
     * Controller to get favorite place as place info.
     *
     * @param id favorite place
     * @return info about place with name as in favorite place
     */
    @GetMapping("/info/favorite/{id}")
    public ResponseEntity<PlaceInfoDto> getFavoritePlaceInfo(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(favoritePlaceService.getInfoFavoritePlace(id));
    }

    /**
     * Controller to save place to user's favorite list.
     *
     * @return favorite place name and place id
     */
    @PostMapping("/save/favorite/")
    public ResponseEntity<FavoritePlaceDto> saveAsFavoritePlace(
        @Valid @RequestBody FavoritePlaceDto favoritePlaceDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(favoritePlaceService.save(favoritePlaceDto, principal.getName()));
    }

    /**
     * The method which return a list {@code PlaceByBoundsDto} with information about place,
     * location depends on the map bounds.
     *
     * @param filterPlaceDto Contains South-West and North-East bounds of map .
     * @return a list of {@code PlaceByBoundsDto}
     * @author Marian Milian
     */
    @PostMapping("/getListPlaceLocationByMapsBounds")
    public ResponseEntity<List<PlaceByBoundsDto>> getListPlaceLocationByMapsBounds(
        @Valid @RequestBody FilterPlaceDto filterPlaceDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(placeService.findPlacesByMapsBounds(filterPlaceDto));
    }

    /**
     * The method parse the string param to PlaceStatus value.
     *
     * @param status   a string represents {@link PlaceStatus} enum value.
     * @param pageable pageable configuration.
     * @return response {@link PageableDto} object. Contains a list of {@link AdminPlaceDto}.
     * @author Roman Zahorui
     */
    @GetMapping("/{status}")
    public ResponseEntity<PageableDto> getPlacesByStatus(
        @PathVariable String status, Pageable pageable) {
        PlaceStatus placeStatus = PlaceStatus.valueOf(status.toUpperCase());
        return ResponseEntity.status(HttpStatus.OK)
            .body(placeService.getPlacesByStatus(placeStatus, pageable));
    }

    /**
     * The method which return a list {@code PlaceByBoundsDto} filtered by values
     * contained in the incoming {@link FilterPlaceDto} object.
     *
     * @param filterDto contains all information about the filtering of the list.
     * @return a list of {@code PlaceByBoundsDto}
     * @author Roman Zahorui
     */
    @PostMapping("/filter")
    public ResponseEntity<List<PlaceByBoundsDto>> getFilteredPlaces(
        @Valid @RequestBody FilterPlaceDto filterDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(placeService.getPlacesByFilter(filterDto));
    }

    /**
     * The method which update {@link Place} status.
     *
     * @param dto - {@link UpdatePlaceStatusDto} with place id and updated {@link PlaceStatus}.
     * @return response object with {@link UpdatePlaceStatusDto} and OK status if everything is ok.
     * @author Nazar Vladyka
     */
    @PatchMapping("/status")
    public ResponseEntity updateStatus(@Valid @RequestBody UpdatePlaceStatusDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(placeService.updateStatus(dto.getId(), dto.getStatus()));
    }

    /**
     * The method which return a list {@code PlaceByBoundsDto} filtered by values
     * contained in the incoming {@link FilterPlaceDto} object.
     *
     * @param filterDto contains all information about the filtering of the list.
     * @param pageable pageable configuration.
     * @return a list of {@code PlaceByBoundsDto}
     * @author Roman Zahorui
     */
    @PostMapping("/filter/predicate")
    public ResponseEntity<PageableDto> filterPlaceBySearchPredicate(
        @Valid @RequestBody FilterPlaceDto filterDto, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(placeService.filterPlaceBySearchPredicate(filterDto, pageable));
    }

    /**
     * Controller to get place info.
     *
     * @param id place
     * @return  response {@link PlaceUpdateDto} object.
     */
    @GetMapping("/about/{id}")
    public ResponseEntity<PlaceUpdateDto> getPlaceById(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(placeService.getInfoForUpdatingById(id));
    }

    /**
     * The method which update array of {@link Place}'s from DB.
     *
     * @param dto - {@link BulkUpdatePlaceStatusDto} with places id's and updated {@link PlaceStatus}
     * @return list of {@link UpdatePlaceStatusDto} with updated places and {@link PlaceStatus}'s
     * @author Nazar Vladyka
     */
    @PatchMapping("/statuses")
    public ResponseEntity bulkUpdateStatuses(@Valid @RequestBody BulkUpdatePlaceStatusDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(
            placeService.updateStatuses(dto));
    }

    /**
     * The method which return array of {@link PlaceStatus}.
     *
     * @return array of statuses
     * @author Nazar Vladyka
     */
    @GetMapping("/statuses")
    public ResponseEntity getStatuses() {
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getStatuses());
    }

    /**
     * The method which delete {@link Place} from DB(change {@link PlaceStatus} to DELETED).
     *
     * @param id - {@link Place} id
     * @return {@link UpdatePlaceStatusDto} of deleted {@link Place} and OK status if everything is ok.
     * @author Nazar Vladyka
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(placeService.updateStatus(id, PlaceStatus.DELETED));
    }

    /**
     * The method which delete array of {@link Place}'s from DB(change {@link PlaceStatus} to DELETED).
     *
     * @param ids - id's of {@link Place}'s which need to be deleted
     * @return list of {@link UpdatePlaceStatusDto} with deleted places
     * @author Nazar Vladyka
     */
    @DeleteMapping
    public ResponseEntity bulkDelete(@RequestParam String ids) {
        return ResponseEntity.status(HttpStatus.OK).body(
            placeService.updateStatuses(new BulkUpdatePlaceStatusDto(
                Arrays.stream(ids.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList()), PlaceStatus.DELETED)));
    }
}
