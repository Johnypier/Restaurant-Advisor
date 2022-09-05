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

import de.tum.in.ase.eist.rest.RestaurantResource;
import de.tum.in.ase.eist.service.RestaurantService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestaurantResource.class)
public class RestaurantControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RestaurantService restaurantService;

        @Test
        void testGetRestaurants() throws Exception {
                List<Restaurant> temp = List.of(new Restaurant("Ivan", "Nigger", "www.4moshnik.de", "100", "2"),
                                new Restaurant("Ivan", "Degenerate", "www.hotboys.ru", "69", "1"));

                when(restaurantService.getAllRestaurants("test")).thenReturn(temp);

                MvcResult resultAction = mockMvc
                                .perform(get("/restaurants?type=test")
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn();

                String content = resultAction.getResponse().getContentAsString();
                assertTrue(content.contains(temp.get(0).getName()));
                assertTrue(content.contains(temp.get(0).getAddress()));
                assertTrue(content.contains(temp.get(0).getWebsite()));
                assertTrue(content.contains(temp.get(1).getName()));
                assertTrue(content.contains(temp.get(1).getAddress()));
                assertTrue(content.contains(temp.get(1).getWebsite()));
        }

        @Test
        void testGetRestaurant() throws Exception {
                Restaurant testing = new Restaurant();
                List<String> test = new LinkedList<>();
                test.add("Monday");
                test.add("Tuesday");
                test.add("Wednesday");
                test.add("Thursday");
                test.add("Friday");
                test.add("Saturday");
                test.add("Sunday");
                testing.setTimeList(test);
                testing.setName("Testing Restaurant");
                testing.setRank("5");

                when(restaurantService.getRestaurant(
                                "test"))
                                .thenReturn(testing);

                MvcResult resultAction = mockMvc.perform(get("/info?type=test")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn();

                String content = resultAction.getResponse().getContentAsString();
                assertTrue(content.contains("Testing Restaurant"));
                assertTrue(content.contains("5"));
                assertTrue(content.contains("Monday"));
                assertTrue(content.contains("Tuesday"));
                assertTrue(content.contains("Wednesday"));
                assertTrue(content.contains("Friday"));
                assertTrue(content.contains("Thursday"));
                assertTrue(content.contains("Sunday"));
        }

}
