package testing.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import restaurants.information.Review;
import server.ServerApplication;
import server.controller.ReviewServerController;

@WebMvcTest(ReviewServerController.class)
@ContextConfiguration(classes = {ServerApplication.class})
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get all reviews test")
    void testGetReviews() throws Exception {
        Review exampleReview1 = new Review("Example 1", "Amazing place!", "4");
        Review exampleReview2 = new Review("Example 2", "Good food!", "5");
        Review exampleReview3 = new Review("Example 3", "Too expensive!", "2");
        List<Review> exampleReviews = List.of(exampleReview1, exampleReview2, exampleReview3);

        MvcResult result = mockMvc.perform(get("/reviews?restaurantUrl=test")
                                                   .accept(MediaType.APPLICATION_JSON)
                                                   .contentType(MediaType.APPLICATION_JSON))
                                  .andDo(print())
                                  .andExpect(status().isOk())
                                  .andReturn();

        List<Review> content = objectMapper.readValue(result.getResponse().getContentAsString(),
                                                      new TypeReference<List<Review>>() {
                                                      });
        assertEquals(3, content.size(),
                     "The response from the server should include the example list with 3 reviews for the GET /reviews endpoint.");
        assertEquals(3, content.stream().filter(review -> {
                         for (Review exampleReview : exampleReviews) {
                             if (exampleReview.getName().equals(review.getName())) {
                                 return true;
                             }
                         }
                         return false;
                     }).toList().size(),
                     "The response from the server did not contain the correct Review objects for the GET /reviews endpoint.");
    }
}
