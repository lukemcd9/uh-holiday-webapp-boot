package edu.hawaii.its.holiday.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import edu.hawaii.its.holiday.configuration.SpringBootWebApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootWebApplication.class })
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class HolidayRestControllerTest {

    final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    Charset.forName("utf8"));

    @Autowired
    private HolidayRestController restController;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(context).build();
    }

    @Test
    public void testConstruction() {
        assertNotNull(restController);
    }

    @Test
    public void httpGetHolidays() throws Exception {
        mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", hasSize(140)));
    }

    @Test
    public void httpGetHolidaysById() throws Exception {
        mockMvc.perform(get("/api/holidays/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("data.description").value("New Year's Day"))
                .andExpect(jsonPath("data.observedDateFull").value("January 01, 2013, Tuesday"))
                .andExpect(jsonPath("data.officialDateFull").value("January 01, 2013, Tuesday"))
                .andExpect(jsonPath("data.year").value("2013"))
                .andExpect(jsonPath("data.types", hasSize(3)))
                .andExpect(jsonPath("data.types[0].description").value("Bank"))
                .andExpect(jsonPath("data.types[1].description").value("Federal"))
                .andExpect(jsonPath("data.types[2].description").value("State"));
    }

    @Test
    public void httpGetHolidaysByYear() throws Exception {
        mockMvc.perform(get("/api/holidays/year/2011/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("data", hasSize(14)))
                .andExpect(jsonPath("data[0].description").value("New Year's Day"))
                .andExpect(jsonPath("data[1].description").value("Martin Luther King Jr. Day"))
                .andExpect(jsonPath("data[2].description").value("Presidents' Day"))
                .andExpect(jsonPath("data[3].description").value("Prince Kuhio Day"))
                .andExpect(jsonPath("data[4].description").value("Good Friday"))
                .andExpect(jsonPath("data[5].description").value("Memorial Day"))
                .andExpect(jsonPath("data[6].description").value("King Kamehameha Day"))
                .andExpect(jsonPath("data[7].description").value("Independence Day"))
                .andExpect(jsonPath("data[8].description").value("Statehood Day"))
                .andExpect(jsonPath("data[9].description").value("Labor Day"))
                .andExpect(jsonPath("data[10].description").value("Discoverer's Day"))
                .andExpect(jsonPath("data[11].description").value("Veterans Day"))
                .andExpect(jsonPath("data[12].description").value("Thanksgiving"))
                .andExpect(jsonPath("data[13].description").value("Christmas"));

        // No records.
        mockMvc.perform(get("/api/holidays/year/2010/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("data", hasSize(0)));

    }

    @Test
    public void holidaysByYearParam() throws Exception {
        // rest/inYear?year=2011&type=uh&isObserved=false
        mockMvc.perform(get("/rest/inYear")
                .param("year", "2012")
                .param("type", "uh")
                .param("isObserved", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("data", hasSize(15)))
                .andExpect(jsonPath("data[0].description").value("New Year's Day"))
                .andExpect(jsonPath("data[1].description").value("Martin Luther King Jr. Day"))
                .andExpect(jsonPath("data[2].description").value("Presidents' Day"))
                .andExpect(jsonPath("data[3].description").value("Prince Kuhio Day"))
                .andExpect(jsonPath("data[4].description").value("Good Friday"))
                .andExpect(jsonPath("data[5].description").value("Memorial Day"))
                .andExpect(jsonPath("data[6].description").value("King Kamehameha Day"))
                .andExpect(jsonPath("data[7].description").value("Independence Day"))
                .andExpect(jsonPath("data[8].description").value("Statehood Day"))
                .andExpect(jsonPath("data[9].description").value("Labor Day"))
                .andExpect(jsonPath("data[10].description").value("Discoverer's Day"))
                .andExpect(jsonPath("data[11].description").value("Election Day"))
                .andExpect(jsonPath("data[12].description").value("Veterans Day"))
                .andExpect(jsonPath("data[13].description").value("Thanksgiving"))
                .andExpect(jsonPath("data[14].description").value("Christmas"));
    }

    @Test
    public void httpGetHolidaysWithWrongIdType() throws Exception {
        mockMvc.perform(get("/api/holidays/xxx"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void httpGetTypes() throws Exception {
        mockMvc.perform(get("/api/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", hasSize(4)))
                .andExpect(jsonPath("data[0].description").value("Bank"))
                .andExpect(jsonPath("data[1].description").value("Federal"))
                .andExpect(jsonPath("data[2].description").value("State"))
                .andExpect(jsonPath("data[3].description").value("UH"));
    }

}
