package ro.acs.ionut.proiecttestare.util;

import java.time.LocalDate;

public class EasterCalculator {

    public static LocalDate getEasterSundayDate(int year){
        int e = 10;

        if (year > 1600) {
            int y2 = (int) Math.floor(year / 100);
            e = 10 + y2 - 16 - (int) Math.floor((y2 - 16) / 4);
        }

        int G = year % 19;
        int I = (19 * G + 15) % 30;
        int J = (year + (int) Math.floor(year / 4) + I) % 7;
        int L = I - J;
        int p = L + e;
        int d = 1 + (p + 27 + (int) Math.floor((p + 6) / 40)) % 31;
        int m = 3 + (int) Math.floor((p + 26) / 30) - 1;
        return LocalDate.now().withYear(year).withMonth(m + 1).withDayOfMonth(d);
    }
}

