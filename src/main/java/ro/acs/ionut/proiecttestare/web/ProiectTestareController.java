package ro.acs.ionut.proiecttestare.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ro.acs.ionut.proiecttestare.domain.PublicHolidayService;
import ro.acs.ionut.proiecttestare.infrastructure.entity.PublicHoliday;
import ro.acs.ionut.proiecttestare.util.ErrorResponse;
import ro.acs.ionut.proiecttestare.web.dto.ErrorDto;
import ro.acs.ionut.proiecttestare.web.dto.PublicHolidayDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(tags = "v1", description = "Endpoint-uri utilizate pentru a gestiona sarbatorile legale")
public class ProiectTestareController {

    @Autowired
    PublicHolidayService publicHolidayService;

    @ApiOperation(value = "Intoarce toate sarbatorile legale dintr-un an calendaristic", response = PublicHolidayDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data was successfully read", response = PublicHolidayDto.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping(path = "/sarbatori-legale")
    public List<PublicHolidayDto> getPublicHolidaysByYear(@RequestParam Long year) {
        log.info("Afisare sarbatori legale pentru anul {}.", year);
        List<PublicHolidayDto> publicHolidayDtos = publicHolidayService.getAllByYear(year.intValue()).stream()
                .map(this::convertPublicHolidayToDto)
                .collect(Collectors.toList());
        log.info("Au fost gasite {} sarbatori legale in anul {}.", publicHolidayDtos.size(),year);
        return publicHolidayDtos;
    }

    @ApiOperation(value = "Intoarce data calendaristica a Pastelui", response = PublicHolidayDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data was successfully read", response = PublicHolidayDto.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @GetMapping(path = "/sarbatori-legale/Paste")
    public List<LocalDate> getEasterDates(@RequestParam int year) {
        LocalDate easterSunday = publicHolidayService.getEasterSundayDate(year);
        List<LocalDate> easterDates = new ArrayList<>();
        easterDates.add(easterSunday);
        easterDates.add(easterSunday.plusDays(1));
        easterDates.add(easterSunday.plusDays(2));
        return easterDates;
    }

    @ApiOperation(value = "Adauga sarbatoare legala")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data was successfully wrote", response = void.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @PostMapping(path = "/sarbatori-legale")
    public void addPublicHoliday(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                 @RequestParam("name") String name) {
        log.info("Adaugare sarbatoare legala {} pe data de {}.", name, date);
        publicHolidayService.addPublicHoliday(date,name);
    }

    @ApiOperation(value = "Stergere sarbatoare legala")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data was successfully deleted", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @DeleteMapping(path = "/sarbatori-legale")
    public void deletePublicHoliday(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        publicHolidayService.deletePublicHoliday(date);
    }

    @ApiOperation(value = "Sterge toate sarbatorile legale dintr-un anumit an")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data was successfully deleted", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDto.class)})
    @DeleteMapping(path = "/sarbatori-legale/by-year")
    public void deletePublicHolidayByYear(@RequestParam("year") Long year) {
        publicHolidayService.deletePublicHolidayByYear(year);
    }

    private PublicHolidayDto convertPublicHolidayToDto(PublicHoliday publicHoliday){
        return PublicHolidayDto.builder()
                .date(publicHoliday.getDate())
                .name(publicHoliday.getName())
                .build();
    }
}
