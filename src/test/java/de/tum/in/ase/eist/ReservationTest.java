package de.tum.in.ase.eist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = Reservation.class)
public class ReservationTest {

    @Test
    void testCreateReservationObject() {
        Reservation test = new Reservation("100", "Bob", "4", "22.04.2030", "20:30", "true");
        assertEquals("100", test.getReservationId());
        assertEquals("Bob", test.getCustomer());
        assertEquals("4", test.getNumberOfPeople());
        assertEquals("22.04.2030", test.getDate());
        assertEquals("20:30", test.getTime());
        assertEquals("true", test.getConfirmation());
    }

}
