package de.tum.in.ase.eist.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import de.tum.in.ase.eist.Reservation;
import de.tum.in.ase.eist.service.ReservationService;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations(@RequestParam("type") String type,
            @RequestParam("name") String name, @RequestParam("restName") String restaurantName)
            throws URISyntaxException {
        try {
            return ResponseEntity.ok(reservationService.getAllReservations(type, name, restaurantName));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Boolean> createReservation(@RequestParam("type") String type,
            @RequestParam("name") String name,
            @RequestParam("restName") String restaurantName,
            @RequestParam("persons") String persons,
            @RequestParam("date") String date,
            @RequestParam("hour") String hour,
            @RequestParam("confirmation") String confirmation) throws URISyntaxException {
        try {
            return ResponseEntity.ok(
                    reservationService.addReservation(type, name, persons, date, hour, confirmation, restaurantName));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(false);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Boolean> deleteReservation(@PathVariable("reservationId") String id,
            @RequestParam("type") String type,
            @RequestParam("name") String name,
            @RequestParam("restName") String restaurantName) throws URISyntaxException {
        try {
            return ResponseEntity.ok(reservationService.deleteReservation(type, id, name, restaurantName));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<Boolean> updateReservation(@PathVariable("reservationId") String id,
            @RequestParam("type") String type,
            @RequestParam("name") String name,
            @RequestParam("restName") String restaurantName,
            @RequestParam("persons") String persons,
            @RequestParam("date") String date,
            @RequestParam("hour") String hour,
            @RequestParam("confirmation") String confirmation) throws URISyntaxException {
        try {
            return ResponseEntity.ok(reservationService.updateReservation(type, id, name, persons, date, hour,
                    confirmation, restaurantName));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(false);
    }
}
