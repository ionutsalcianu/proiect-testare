package ro.acs.ionut.proiecttestare.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PublicHolidayDto {

    private LocalDate date;
    private String name;
}
