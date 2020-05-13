package ro.acs.ionut.proiecttestare.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.acs.ionut.proiecttestare.ProiectTestareApplication;
import ro.acs.ionut.proiecttestare.infrastructure.entity.PublicHoliday;
import ro.acs.ionut.proiecttestare.infrastructure.repository.PublicHolidayRepository;
import ro.acs.ionut.proiecttestare.util.PublicHolidayAlreadyExists;
import ro.acs.ionut.proiecttestare.util.PublicHolidaysNotFound;

import javax.annotation.Resource;
import java.time.LocalDate;

import static org.junit.Assert.*;


@ContextConfiguration(classes = ProiectTestareApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class PublicHolidayServiceTest {

    private LocalDate existingDate =  LocalDate.now().withDayOfYear(1).withYear(2020);
    private LocalDate existingDate2 =  LocalDate.now().withDayOfYear(3).withYear(2020);
    private LocalDate startOfYear2019 =  LocalDate.now().withDayOfYear(1).withYear(2019);
    private LocalDate endOfYear2019 =  LocalDate.now().withDayOfYear(1).withYear(2020).minusDays(1);
    private LocalDate nonExistingDate =  LocalDate.now().withDayOfYear(2).withYear(2020);

    @Resource
    PublicHolidayRepository publicHolidayRepository;

    @Resource
    public PublicHolidayService publicHolidayService;

    @Before
    public void runBefore(){
        publicHolidayRepository.deleteAll();
        PublicHoliday publicHoliday1 = new PublicHoliday();
        publicHoliday1.setName("name");
        publicHoliday1.setDate(existingDate);
        publicHolidayRepository.save(publicHoliday1);
        PublicHoliday publicHoliday2 = new PublicHoliday();
        publicHoliday2.setName("name2");
        publicHoliday2.setDate(existingDate2);
        publicHolidayRepository.save(publicHoliday2);
        PublicHoliday publicHoliday3 = new PublicHoliday();
        publicHoliday3.setName("name3");
        publicHoliday3.setDate(startOfYear2019);
        publicHolidayRepository.save(publicHoliday3);
        PublicHoliday publicHoliday4 = new PublicHoliday();
        publicHoliday4.setName("name4");
        publicHoliday4.setDate(endOfYear2019);
        publicHolidayRepository.save(publicHoliday4);
    }

    @Test
    public void deletePublicHolidayByDate_test_success(){
        publicHolidayService.deletePublicHoliday(existingDate);
        assertNull(publicHolidayRepository.findByDate(existingDate));
    }

    @Test(expected = PublicHolidaysNotFound.class)
    public void deletePublicHolidayByDate_test_fail() {
        publicHolidayService.deletePublicHoliday(nonExistingDate);
    }

    @Test
    public void deletePublicHolidayByYear_test(){
        publicHolidayService.deletePublicHolidayByYear(2020L);
        assertEquals(publicHolidayService.getAllByYear(2020).size(),0);
        assertEquals(publicHolidayService.getAllByYear(2019).size(),2);
    }

    @Test
    public void addPublicHoliday_test_success(){
        LocalDate newPhDate = LocalDate.now();
        String newPhname = "new public holiday";
        assertNull(publicHolidayRepository.findByDate(newPhDate));
        publicHolidayService.addPublicHoliday(newPhDate,newPhname);
        assertNotNull(publicHolidayRepository.findByDate(newPhDate));
    }

    @Test(expected = PublicHolidayAlreadyExists.class)
    public void addPublicHoliday_test_fail(){
        assertNotNull(publicHolidayRepository.findByDate(existingDate));
        publicHolidayService.addPublicHoliday(existingDate,"existing public holiday");
    }

    @Test
    public void getAllByYear_test(){
        assertEquals(publicHolidayService.getAllByYear(2019).size(),2);
    }

}
