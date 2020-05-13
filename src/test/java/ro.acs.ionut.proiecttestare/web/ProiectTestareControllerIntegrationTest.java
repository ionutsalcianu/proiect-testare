package ro.acs.ionut.proiecttestare.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ro.acs.ionut.proiecttestare.ProiectTestareApplication;
import ro.acs.ionut.proiecttestare.domain.PublicHolidayService;
import ro.acs.ionut.proiecttestare.infrastructure.entity.PublicHoliday;
import ro.acs.ionut.proiecttestare.infrastructure.repository.PublicHolidayRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProiectTestareApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class ProiectTestareControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PublicHolidayRepository repository;
    @Autowired
    private PublicHolidayService service;

    private final String HOLIDAY_STRING = "First One";
    private final LocalDate HOLIDAY_DATE = LocalDate.now().withMonth(11).withDayOfMonth(11).withYear(2020);
    private final LocalDate SARBATOARE_INEXISTENTA = LocalDate.now().withMonth(12).withDayOfMonth(12).withYear(2020);
    private final LocalDate easterSunday2020 = LocalDate.now().withYear(2020).withMonth(4).withDayOfMonth(19);


    @Before
    public void createTestPublicHoliday(){
        repository.deleteAll();
        PublicHoliday publicHoliday = new PublicHoliday();
        publicHoliday.setDate(HOLIDAY_DATE );
        publicHoliday.setName(HOLIDAY_STRING);
        repository.save(publicHoliday);
    }

    @Test
    public void getPublicHoliday_test_success()
            throws Exception {

        ResultActions resultActions = mvc.perform(get("/api/v1/sarbatori-legale")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year",String.valueOf(HOLIDAY_DATE.getYear())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$",hasSize(1)));

        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"name\":\""+HOLIDAY_STRING+"\""));
        assertTrue(content.contains("\"date\":\"" + HOLIDAY_DATE.getYear() + "-" + HOLIDAY_DATE.getMonthValue() +"-"+ HOLIDAY_DATE.getDayOfMonth()+"\""));
    }

    @Test
    public void getZeroPublicHoliday_test_success()
            throws Exception {

        mvc.perform(get("/api/v1/sarbatori-legale")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year",String.valueOf(HOLIDAY_DATE.getYear()-20)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    public void getEaster_test() throws Exception {
        mvc.perform(get("/api/v1/sarbatori-legale/Paste")
                .param("year",String.valueOf(easterSunday2020.getYear()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(3)));
    }

    @Test
    public void addPublicHoliday_test_success() throws Exception {
        String holidayName = "Sarbatoare";

        ResultActions resultActions =
                mvc.perform(post("/api/v1/sarbatori-legale")
                        .param("date", SARBATOARE_INEXISTENTA.toString())
                        .param("name", holidayName)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addPublicHoliday_test_fail() throws Exception {
        String holidayName = "Sarbatoare Existenta";

        ResultActions resultActions =
                mvc.perform(post("/api/v1/sarbatori-legale")
                        .param("date",HOLIDAY_DATE.toString())
                        .param("name", holidayName)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        assertTrue(content.contains("\"errorCode\":\"PUBLIC_HOLIDAY_ALREADY_EXISTS\""));
        assertTrue(content.contains("\"exception\":\"Sarbatorea legala de pe data de "+HOLIDAY_DATE.toString()+" exista deja in baza de date\""));
    }

    @Test
    public void deletePublicHoliday_test_success() throws Exception {
        mvc.perform(delete("/api/v1/sarbatori-legale")
                .param("date",HOLIDAY_DATE.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deletePublicHoliday_test_fail() throws Exception {
        ResultActions resultActions =
                mvc.perform(delete("/api/v1/sarbatori-legale")
                        .param("date", SARBATOARE_INEXISTENTA.toString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andDo(print())
                        .andExpect(status().isNotFound());

        String content = resultActions.andReturn().getResponse().getContentAsString();
        assertTrue(content.contains("\"errorCode\":\"PUBLIC_HOLIDAYS_NOT_FOUND\""));
        assertTrue(content.contains("\"exception\":\"Sarbatoarea legala de pe data de "+SARBATOARE_INEXISTENTA.toString()+" nu exista in baza de date\""));
    }

    @Test
    public void deletePublicHolidayByYear_test_success() throws Exception {
        assertEquals(service.getAllByYear(HOLIDAY_DATE.getYear()).size(),1);
        mvc.perform(delete("/api/v1/sarbatori-legale/by-year")
                .param("year",String.valueOf(HOLIDAY_DATE.getYear()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(service.getAllByYear(HOLIDAY_DATE.getYear()).size(),0);
    }

}
