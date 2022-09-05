package de.tum.in.ase.eist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import de.tum.in.ase.eist.rest.ReservationResource;
import de.tum.in.ase.eist.service.ReservationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationResource.class)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    void testGetAllReservations() throws Exception {
        List<Reservation> temp = new LinkedList<>();
        temp.add(new Reservation("empty", null, null, null, null, null));
        when(reservationService.getAllReservations("test", "axe", "gg")).thenReturn(temp);

        MvcResult result = mockMvc
                .perform(get("/reservations?type=test&name=axe&restName=gg").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("empty"));
    }

    @Test
    void testAddReservation() throws Exception {
        when(reservationService.addReservation("test", "axe", "4", "gg", "qq", "false", "lol")).thenReturn(true);

        MvcResult result = mockMvc.perform(
                post("/reservations?type=test&name=axe&persons=4&date=gg&hour=qq&confirmation=false&restName=lol")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    void testUpdateReservation() throws Exception {
        when(reservationService.updateReservation("test", "id", "axe", "4", "gg", "qq", "false", "lol"))
                .thenReturn(true);
        MvcResult result = mockMvc.perform(
                put("/reservations/id?type=test&name=axe&persons=4&date=gg&hour=qq&confirmation=false&restName=lol")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }

    @Test
    void testDeleteReservation() throws Exception {
        when(reservationService.deleteReservation("test", "id", "axe", "lol")).thenReturn(true);
        MvcResult result = mockMvc.perform(
                delete("/reservations/id?type=test&name=axe&restName=lol")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("true"));
    }
}
