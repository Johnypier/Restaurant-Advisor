package server.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import restaurants.information.Restaurant;
import server.service.RestaurantService;

@RestController
public class RestaurantServerController {
    private final List<Restaurant> exampleRestaurants = createExampleRestaurants();
    private final RestaurantService restaurantService;  // Deprecated

    public RestaurantServerController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Restaurant>> getAllRestaurants(@RequestParam("type") String type) {
        // This is only an example to show how the application works, since the service is deprecated.
        return ResponseEntity.ok(exampleRestaurants);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> getRestaurant(@RequestParam("restaurantUrl") String restaurantUrl) {
        // This is only an example to show how the application works, since the service is deprecated.
        for (Restaurant restaurant : exampleRestaurants) {
            if (restaurantUrl.equals(restaurant.getWebsite())) {
                return ResponseEntity.ok(restaurant);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private List<Restaurant> createExampleRestaurants() {
        List<String> timeListExample = List.of("Mon 11:30 AM - 08:30 PM", "Tue 09:00 PM - 01:00 AM",
                                               "Wed 08:00 AM - 05:00 PM", "Thu CLOSED", "Fri 10:00 AM - 10:00 PM",
                                               "Sat CLOSED", "Sun CLOSED");
        Restaurant exampleRestaurant1 = new Restaurant("Example 1", "Address 1", "www.example-1.com", "8800",
                                                       "4");
        Restaurant exampleRestaurant2 = new Restaurant("Example 2", "Address 2", "www.example-2.de", "7700",
                                                       "3");
        Restaurant exampleRestaurant3 = new Restaurant("Example 3", "Address 3", "www.example-3.gg", "6600",
                                                       "5");
        exampleRestaurant1.setTimeList(timeListExample);
        exampleRestaurant2.setTimeList(timeListExample);
        exampleRestaurant3.setTimeList(timeListExample);
        return List.of(exampleRestaurant1, exampleRestaurant2, exampleRestaurant3);
    }
}
