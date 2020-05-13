package ro.acs.ionut.proiecttestare.util;

import org.junit.Test;
import ro.acs.ionut.proiecttestare.util.EasterCalculator;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class EasterCalculatorTest {

    private LocalDate easterSunday2015 = LocalDate.now().withYear(2015).withMonth(4).withDayOfMonth(12);
    private LocalDate easterSunday2020 = LocalDate.now().withYear(2020).withMonth(4).withDayOfMonth(19);
    private LocalDate easterSunday2027 = LocalDate.now().withYear(2027).withMonth(5).withDayOfMonth(2);

    @Test
    public void getEasterSundayDate(){
        LocalDate easterSunday20 = EasterCalculator.getEasterSundayDate(2020);
        assertEquals(easterSunday20,easterSunday2020);
        LocalDate easterSunday27 = EasterCalculator.getEasterSundayDate(2027);
        assertEquals(easterSunday27,easterSunday2027);
        LocalDate easterSunday15 = EasterCalculator.getEasterSundayDate(2015);
        assertEquals(easterSunday15,easterSunday2015);
    }
}
