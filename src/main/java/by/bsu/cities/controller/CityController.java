package by.bsu.cities.controller;

import by.bsu.cities.dto.CityDto;
import by.bsu.cities.dto.CityEditDto;
import by.bsu.cities.exception.RestException;
import by.bsu.cities.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/cities")
@RestController
@Slf4j
public class CityController {
    private final CityService cityService;
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping()
    @Operation(
            description = "Get Cities",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully get cities",
                            content =  @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"id\": 1, \"name\": CityName, \"photoUrl\": link.com/url}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error processing REST request with status code 400 and message: Bad Request",
                            content =  @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "Error processing REST request with status code 400 and message: Bad Request"
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<Page<CityDto>> getCities(
            Pageable page,
            @RequestParam(name = "name", defaultValue = "") String name) {
        log.info("GET /cities?page={}&name={}", page, name);
        return ResponseEntity.ok(
                this.cityService.find(page, name)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCity(@PathVariable long id) throws RestException {
        log.info("GET /cities/{}", id);
        CityDto cityDto = this.cityService.findById(id);
        log.info("Found city: {}", cityDto);
        return ResponseEntity.ok(cityDto);
    }

    @PreAuthorize("hasRole('ALLOW_EDIT')")
    @PatchMapping("/{id}")
    public ResponseEntity<CityDto> patchCity(@PathVariable("id") long id, @RequestBody CityEditDto editDto) throws RestException {
        log.info("PATCH /cities/{} with body {}", id, editDto);
        CityDto patchedCity = this.cityService.patchCity(id, editDto);
        log.info("Patched city: {}", patchedCity);
        return ResponseEntity.ok(patchedCity);
    }
}
