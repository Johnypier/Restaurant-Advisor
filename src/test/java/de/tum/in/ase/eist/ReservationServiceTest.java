package de.tum.in.ase.eist;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import de.tum.in.ase.eist.service.ReservationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
public class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Test
    void testGetAllReservationsWhenArrayIsEmpty() throws JSONException, IOException, URISyntaxException {
        assertEquals("empty",
                reservationService.getAllReservations("test", "JoJo", "Adventure").get(0).getReservationId());
    }

    @Test
    void testAddAndUpdateReservation() throws JSONException, IOException, URISyntaxException {
        assertTrue(reservationService.addReservation("test", "JoJo", "2", "22.07.2022", "15:00", "true", "Adventure"));
        var test = reservationService.getAllReservations("test", "JoJo", "Adventure");
        assertTrue(reservationService.updateReservation("test", test.get(0).getReservationId(), "JoJo", "2",
                "25.07.2022", "18:00",
                "true", "Adventure"));
        var updated = reservationService.getAllReservations("test", "JoJo", "Adventure");
        assertEquals(1, updated.size());
        assertEquals("18:00", updated.get(0).getTime());
        assertEquals("25.07.2022", updated.get(0).getDate());
    }

    @Test
    void testDeleteReservation() throws JSONException, IOException, URISyntaxException {
        var test = reservationService.getAllReservations("test", "JoJo", "Adventure");
        assertTrue(reservationService.deleteReservation("test", test.get(0).getReservationId(), "JoJo", "Adventure"));
        assertEquals("empty",
                reservationService.getAllReservations("test", "JoJo", "Adventure").get(0).getReservationId());
    }

}
