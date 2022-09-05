package de.tum.in.ase.eist;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.tum.in.ase.eist.service.ReviewService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Test
    void testGetReviews() throws JSONException, IOException, URISyntaxException {
        List<Review> temp = reviewService
                .getAllReviews("https://www.yelp.com/biz/taverna-kreta-grill-m%C3%BCnchen?osq=Restaurants");
        Review test = temp.get(0);

        assertEquals(9, temp.size());
        assertEquals("Wong Fook H.", test.getName());
        assertEquals("5", test.getRank());
    }
}
