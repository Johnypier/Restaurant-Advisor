package server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import restaurants.information.Review;
import server.service.ReviewService;

@RestController
public class ReviewServerController {
    private final ReviewService reviewService;  // Deprecated

    public ReviewServerController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam("restaurantUrl") String url) {
        // This is only an example to show how the application works, since the service is deprecated.
        Review exampleReview1 = new Review("Example 1", "Amazing place!", "4");
        Review exampleReview2 = new Review("Example 2", "Good food!", "5");
        Review exampleReview3 = new Review("Example 3", "Too expensive!", "2");
        return ResponseEntity.ok(List.of(exampleReview1, exampleReview2, exampleReview3));
    }
}
