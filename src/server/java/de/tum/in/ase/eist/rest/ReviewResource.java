package de.tum.in.ase.eist.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import de.tum.in.ase.eist.Review;
import de.tum.in.ase.eist.service.ReviewService;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class ReviewResource {

    private final ReviewService reviewService;

    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam("type") String url) {
        try {
            return ResponseEntity.ok(reviewService.getAllReviews(url));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
