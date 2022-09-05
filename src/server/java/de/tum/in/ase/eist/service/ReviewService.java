package de.tum.in.ase.eist.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import de.tum.in.ase.eist.Review;

@Service
public class ReviewService {
    private static final String REVIEWS_REGEX = "<script type=\"application/ld\\+json\">(.+?\"@type\":\"Restaurant\".+?)</script>";
    private List<Review> reviews;

    public ReviewService() {
        this.reviews = new ArrayList<>();
    }

    /**
     * Parses json file to List with Review objects.
     * Gets jsonarray to parse from html document.
     * 
     * @param url - URL to website with restaurant.
     * @throws URISyntaxException
     * @return List<Review> with reviews of specified restaurant.
     **/
    public List<Review> getAllReviews(String url) throws JSONException, IOException {
        this.reviews.clear();
        Document doc = Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(5000)
                .get();

        String html = doc.html();

        String json = applyRegex(REVIEWS_REGEX, html, 1);
        JSONObject jsonObject = new JSONObject(json);

        JSONArray rev = jsonObject.getJSONArray("review");

        for (int i = 0; i < rev.length(); i++) {
            JSONObject review = rev.getJSONObject(i);
            Review tmpReview = new Review();
            tmpReview.setName(review.getString("author"));
            tmpReview.setText(review.getString("description"));
            tmpReview.setRank(review.getJSONObject("reviewRating").getString("ratingValue"));
            this.reviews.add(tmpReview);
        }
        return this.reviews;
    }

    /**
     * 
     * Applies regex to given string.
     * 
     * @param regex      - regex pattern.
     * @param input      - string to change.
     * @param groupIndex - index to match.
     * @return String with matched groups.
     */
    public static String applyRegex(String regex, String input, int groupIndex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return "";
    }
}
