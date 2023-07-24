package server.service;

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

import restaurants.information.Review;

@Service
public class ReviewService {
    private static final String REVIEWS_REGEX =
            "<script type=\"application/ld\\+json\">(.+?\"@type\":\"Restaurant\".+?)</script>";
    private final List<Review> reviews;

    public ReviewService() {
        this.reviews = new ArrayList<>();
    }

    /**
     * Parses json file extracted from a webpage and creates a List with Review objects.
     *
     * @param url The URL to a yelp webpage with the restaurant information.
     * @return List<Review> with reviews of the specified restaurant.
     * @deprecated Due to some changes on the website used as the source of the application data.
     */
    @Deprecated
    public List<Review> getAllReviews(String url) throws JSONException, IOException {
        this.reviews.clear();
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get();

        String html = doc.html();
        System.out.println(doc.html());

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
     * Applies the regex to the given string.
     *
     * @param regex      Regex pattern
     * @param input      String to match
     * @param groupIndex Index of the required to be matched group
     * @return Matched substring
     * @deprecated Due to some changes on the website used as the source of the application data.
     */
    @Deprecated
    private static String applyRegex(String regex, String input, int groupIndex) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        return matcher.find() ? matcher.group(groupIndex) : "";
    }
}
