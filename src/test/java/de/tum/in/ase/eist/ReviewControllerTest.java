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

import de.tum.in.ase.eist.rest.ReviewResource;
import de.tum.in.ase.eist.service.ReviewService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewResource.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    void testGetReviews() throws Exception {
        List<Review> testList = new LinkedList<>();
        Review testReview = new Review("Ivan Kuzmin", "I love Java", "1");
        testList.add(testReview);

        when(reviewService.getAllReviews("test")).thenReturn(testList);

        MvcResult result = mockMvc.perform(get("/reviews?type=test")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Ivan Kuzmin"));
        assertTrue(content.contains("I love Java"));
        assertTrue(content.contains("1"));
    }

}
