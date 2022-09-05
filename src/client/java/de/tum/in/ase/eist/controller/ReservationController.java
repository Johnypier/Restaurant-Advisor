package de.tum.in.ase.eist.controller;

import java.util.*;
import java.util.function.Consumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

import de.tum.in.ase.eist.*;

public class ReservationController {
        private final WebClient webClient;
        private final List<Reservation> reservations;
        private static final String TYPE = "type";
        private static final String NAME = "name";
        private static final String RESTNAME = "restName";

        public ReservationController() {
                this.webClient = WebClient.builder()
                                .baseUrl("http://localhost:8080/")
                                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .build();
                this.reservations = new ArrayList<>();
        }

        public void getAllReservations(Consumer<List<Reservation>> reservationConsumer, String type, String name,
                        String restaurantName) {
                webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/reservations")
                                                .queryParam(TYPE, type)
                                                .queryParam(NAME, name)
                                                .queryParam(RESTNAME, restaurantName)
                                                .build())
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<List<Reservation>>() {
                                })
                                .onErrorStop()
                                .subscribe(newReservations -> {
                                        reservations.clear();
                                        reservations.addAll(newReservations);
                                        reservationConsumer.accept(reservations);
                                });
        }

        public void createReservation(String type, String name,
                        String restaurantName, String persons, String date, String hour, String confirmation) {
                webClient.post()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/reservations")
                                                .queryParam(TYPE, type)
                                                .queryParam(NAME, name)
                                                .queryParam(RESTNAME, restaurantName)
                                                .queryParam("persons", persons)
                                                .queryParam("date", date)
                                                .queryParam("hour", hour)
                                                .queryParam("confirmation", confirmation)
                                                .build())
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Boolean>() {
                                })
                                .onErrorStop()
                                .subscribe();
        }

        public void updateReservation(String id, String type, String name,
                        String restaurantName, String persons, String date, String hour, String confirmation) {
                webClient.put()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/reservations/" + id)
                                                .queryParam(TYPE, type)
                                                .queryParam(NAME, name)
                                                .queryParam(RESTNAME, restaurantName)
                                                .queryParam("persons", persons)
                                                .queryParam("date", date)
                                                .queryParam("hour", hour)
                                                .queryParam("confirmation", confirmation)
                                                .build())
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Boolean>() {
                                })
                                .onErrorStop()
                                .subscribe();
        }

        public void deleteReservation(String id, String type, String name,
                        String restaurantName) {
                webClient.delete()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/reservations/" + id)
                                                .queryParam(TYPE, type)
                                                .queryParam(NAME, name)
                                                .queryParam(RESTNAME, restaurantName)
                                                .build())
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Boolean>() {
                                })
                                .onErrorStop()
                                .subscribe();
        }
}
