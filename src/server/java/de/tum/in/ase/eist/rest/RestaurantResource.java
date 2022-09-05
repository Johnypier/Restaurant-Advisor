package de.tum.in.ase.eist.rest;

import java.io.IOException;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import de.tum.in.ase.eist.Restaurant;
import de.tum.in.ase.eist.service.RestaurantService;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class RestaurantResource {

    private final RestaurantService restaurantService;

    public RestaurantResource(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(@RequestParam("type") String path) {
        try {
            return ResponseEntity.ok(restaurantService.getAllRestaurants(path));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/info")
    public ResponseEntity<Restaurant> getRestaurant(@RequestParam("type") String cuisine) {

        try {
            return ResponseEntity.ok(restaurantService.getRestaurant(cuisine));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
