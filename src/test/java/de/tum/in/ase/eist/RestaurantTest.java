package de.tum.in.ase.eist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = Restaurant.class)
class RestaurantTest {

    @Test
    void testCreateRestaurantObject01() {
        Restaurant restaurant = new Restaurant("Test", "Test", "Test", "123", "4.5");
        assertEquals("Test", restaurant.getName());
        assertEquals("Test", restaurant.getAddress());
        assertEquals("Test", restaurant.getWebsite());
        assertEquals("123", restaurant.getPhone());
        assertEquals("4.5", restaurant.getRank());
    }

    @Test
    void testCreateRestaurantObject02() {
        Restaurant restaurant = new Restaurant("Test", "Test", "Test", "123", "4.5");
        restaurant.setName("Test2");
        restaurant.setAddress("Test2");
        restaurant.setWebsite("Test2");
        restaurant.setPhone("456");
        restaurant.setRank("5.5");
        assertEquals("Test2", restaurant.getName());
        assertEquals("Test2", restaurant.getAddress());
        assertEquals("Test2", restaurant.getWebsite());
        assertEquals("456", restaurant.getPhone());
        assertEquals("5.5", restaurant.getRank());
    }

}