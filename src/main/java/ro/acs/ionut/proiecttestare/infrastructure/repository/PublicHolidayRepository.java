package ro.acs.ionut.proiecttestare.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.acs.ionut.proiecttestare.infrastructure.entity.PublicHoliday;

import java.time.LocalDate;
import java.util.List;

public interface PublicHolidayRepository extends JpaRepository<PublicHoliday,Long> {

    List<PublicHoliday> findAllByDateBetweenOrderByDate(LocalDate startDate, LocalDate endDate);

    PublicHoliday findByDate(LocalDate date);
}
