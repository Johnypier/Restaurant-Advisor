package de.tum.in.ase.eist;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.tum.in.ase.eist.service.RestaurantService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Test
    void testGetAllRestaurants() throws IOException, JSONException {
        List<Restaurant> temp = restaurantService.getAllRestaurants("italian");
        assertEquals(20, temp.size());
    }

    @Test
    void testGetOpeningHours() throws IOException, JSONException {
        Restaurant temp = restaurantService
                .getRestaurant("https://www.yelp.com/biz/taverna-kreta-grill-m%C3%BCnchen?osq=Restaurants");

        assertEquals("Taverna Kreta Grill", temp.getName());
        assertEquals("4.5", temp.getRank());
    }

}
