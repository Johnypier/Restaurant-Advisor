package testing.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import restaurants.information.Restaurant;
import server.ServerApplication;
import server.controller.RestaurantServerController;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

@WebMvcTest(RestaurantServerController.class)
@ContextConfiguration(classes = {ServerApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SuppressWarnings("UnusedDeclaration")
class RestaurantControllerTest {
    private final List<Restaurant> exampleRestaurants = createExampleRestaurants();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get all restaurants test")
    void testGetRestaurants() throws Exception {
        // Example testing.
        MvcResult result = mockMvc.perform(get("/restaurants?type=test")
                                                   .accept(MediaType.APPLICATION_JSON)
                                                   .contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        List<Restaurant> content = objectMapper.readValue(result.getResponse().getContentAsString(),
                                                          new TypeReference<List<Restaurant>>() {
                                                          });
        assertEquals(3, content.size(),
                     "The response from the server should include the example list with 3 restaurants for the GET /restaurants endpoint.");
        assertEquals(3, content.stream().filter(restaurant -> {
                         for (Restaurant exampleRestaurant : exampleRestaurants) {
                             if (exampleRestaurant.getName().equals(restaurant.getName())) {
                                 return true;
                             }
                         }
                         return false;
                     }).toList().size(),
                     "The response from the server did not contain the correct Restaurant objects for the GET /restaurants endpoint.");
    }

    @Test
    @DisplayName("Get single restaurant test")
    void testGetRestaurant() throws Exception {
        // Example testing.
        MvcResult resultAction = mockMvc.perform(get("/info?restaurantUrl=www.example-1.com")
                                                         .accept(MediaType.APPLICATION_JSON)
                                                         .contentType(MediaType.APPLICATION_JSON))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andReturn();

        Restaurant content = objectMapper.readValue(resultAction.getResponse().getContentAsString(), Restaurant.class);
        assertEquals(exampleRestaurants.get(0).getName(), content.getName(),
                     "The server did not return the correct restaurant for the GET /info endpoint request.");
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
