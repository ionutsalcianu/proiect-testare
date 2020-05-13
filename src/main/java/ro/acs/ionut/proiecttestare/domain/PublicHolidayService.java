package ro.acs.ionut.proiecttestare.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.acs.ionut.proiecttestare.infrastructure.entity.PublicHoliday;
import ro.acs.ionut.proiecttestare.infrastructure.repository.PublicHolidayRepository;
import ro.acs.ionut.proiecttestare.util.EasterCalculator;
import ro.acs.ionut.proiecttestare.util.PublicHolidayAlreadyExists;
import ro.acs.ionut.proiecttestare.util.PublicHolidaysNotFound;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Slf4j
public class PublicHolidayService {

    @Autowired
    PublicHolidayRepository publicHolidayRepository;

    public List<PublicHoliday> getAllByYear(int year){
        LocalDate startDate = LocalDate.now().withYear(year).withDayOfYear(1);
        LocalDate endDate = LocalDate.now().withYear(year).with(TemporalAdjusters.lastDayOfYear());
        return publicHolidayRepository.findAllByDateBetweenOrderByDate(startDate,endDate);
    }


    public void addPublicHoliday(LocalDate date,String name){
        PublicHoliday publicHoliday = publicHolidayRepository.findByDate(date);
        if(publicHoliday != null){
            throw new PublicHolidayAlreadyExists("Sarbatorea legala de pe data de "+date+" exista deja in baza de date");
        }
        PublicHoliday newPublicHoliday = new PublicHoliday();
        newPublicHoliday.setDate(date);
        newPublicHoliday.setName(name);
        publicHolidayRepository.save(newPublicHoliday);
    }

    public void deletePublicHolidayByYear(Long year){
        List<PublicHoliday> publicHolidaysByYear = this.getAllByYear(year.intValue());
        log.info("Vor fi sterse {} sarbatori legale din anul {}",publicHolidaysByYear.size(),year);
        publicHolidayRepository.deleteInBatch(publicHolidaysByYear);
    }

    public void deletePublicHoliday(LocalDate date){
        PublicHoliday publicHoliday = publicHolidayRepository.findByDate(date);
        if(publicHoliday == null){
            throw new PublicHolidaysNotFound("Sarbatoarea legala de pe data de "+date+" nu exista in baza de date");
        }
        log.info("Se sterge sarbatoarea legala {} de pe data de {}",publicHoliday.getName(), publicHoliday.getDate());
        publicHolidayRepository.delete(publicHoliday);
    }

    public LocalDate getEasterSundayDate(int year){
        return EasterCalculator.getEasterSundayDate(year);
    }
}
