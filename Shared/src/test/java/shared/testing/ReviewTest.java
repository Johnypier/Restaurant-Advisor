package shared.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import restaurants.information.Review;

class ReviewTest {
    @Test
    void testCreateReviewObject01() {
        Review review = new Review("Test", "Test", "4");
        assertEquals("Test", review.getName());
        assertEquals("Test", review.getText());
        assertEquals("4", review.getRank());
    }

    @Test
    void testCreateReviewObject02() {
        Review review = new Review("comment", "comment", "4");
        assertEquals("comment", review.getName());
        assertEquals("comment", review.getText());
        assertEquals("4", review.getRank());
    }
}
