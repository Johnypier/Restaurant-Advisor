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

import restaurants.information.Restaurant;

@Service
public class RestaurantService {
    private static final String SEARCH_CONST = "searchResultBusiness";
    private static final String LEGACY = "legacyProps";
    private static final String PHONE_REGEX = "class=\" css-1p9ibgf\".+?>([0-9]{3} [0-9]{7,8})</p>";
    private static final String REGEX_FOR_JSON_BEGINNING = "script[type=\"application/json\"]";
    private final List<Restaurant> restaurants;
    private final Restaurant restaurant;

    public RestaurantService() {
        this.restaurants = new ArrayList<>();
        this.restaurant = new Restaurant();
    }

    /**
     * Scraps the website to retrieve the list of the restaurant with the specified type.
     *
     * @param cuisine The type of the restaurants to search.
     * @deprecated Due to some changes on the website used as the source of the application data.
     */
    @Deprecated
    public List<Restaurant> getAllRestaurants(String cuisine) throws IOException, JSONException {
        // Restaurants list should be empty.
        restaurants.clear();

        String url = generateSearchUrlForList(cuisine);
        Document document = Jsoup.connect(url).timeout(30000).followRedirects(true).get();

        String jsonString = document.select(REGEX_FOR_JSON_BEGINNING).toString();
        jsonString = jsonString.substring(jsonString.indexOf("<!--") + 4, jsonString.indexOf("-->"));
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONObject(LEGACY).getJSONObject("searchAppProps")
                                        .getJSONObject("searchPageProps")
                                        .getJSONArray("mainContentComponentsListProps");

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).toString().contains(SEARCH_CONST)) {
                Restaurant temp = new Restaurant();
                temp.setName(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("name"));
                temp.setAddress(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getJSONArray("neighborhoods")
                                         .get(0).toString());
                temp.setPhone(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("phone"));
                temp.setRank(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("rating"));
                temp.setWebsite("https://www.yelp.com" + jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                                  .getString("businessUrl"));
                temp.setPriceRange(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("priceRange"));
                temp.setPreviewImg(jsonArray.getJSONObject(i).getJSONObject("scrollablePhotos")
                                            .getJSONArray("photoList").getJSONObject(0).getString("srcset"));
                temp.setBusinessID(jsonArray.getJSONObject(i).getString("bizId"));
                restaurants.add(temp);
            }
        }

        String nextPageUrl = jsonObject.getJSONObject("legacyProps").getJSONObject("headerProps")
                                       .getJSONObject("pageMetaTagsProps").getString("nextPageUrl");

        if (nextPageUrl != null) {
            restaurants.addAll(getRestaurantsFromNextPage(nextPageUrl));
        }
        return restaurants;
    }

    /**
     * Creates a list of restaurants from the next page of website.
     *
     * @param url The URL to the website with the restaurant.
     * @return List with the Restaurant objects.
     * @deprecated Due to some changes on the website used as the source of the application data.
     */
    @Deprecated
    private List<Restaurant> getRestaurantsFromNextPage(String url) throws IOException, JSONException {
        List<Restaurant> tmplist = new ArrayList<>();
        Document document = Jsoup.connect(url).timeout(30000).followRedirects(true).get();

        String jsonString = document.select(REGEX_FOR_JSON_BEGINNING).toString();
        jsonString = jsonString.substring(jsonString.indexOf("<!--") + 4, jsonString.indexOf("-->"));
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray jsonArray = jsonObject.getJSONObject(LEGACY).getJSONObject("searchAppProps")
                                        .getJSONObject("searchPageProps")
                                        .getJSONArray("mainContentComponentsListProps");

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).toString().contains(SEARCH_CONST)) {
                Restaurant temp = new Restaurant();
                temp.setName(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("name"));
                temp.setAddress(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getJSONArray("neighborhoods")
                                         .get(0).toString());
                temp.setPhone(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("phone"));
                temp.setRank(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("rating"));
                temp.setWebsite("https://www.yelp.com" + jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                                  .getString("businessUrl"));
                temp.setPriceRange(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("priceRange"));
                temp.setPreviewImg(jsonArray.getJSONObject(i).getJSONObject("scrollablePhotos")
                                            .getJSONArray("photoList").getJSONObject(0).getString("srcset"));
                temp.setBusinessID(jsonArray.getJSONObject(i).getString("bizId"));
                tmplist.add(temp);
            }
        }
        return tmplist;
    }

    /**
     * Scraps the website to retrieve the information about the specific restaurant.
     *
     * @param url The URL to the website with the restaurant.
     * @return Restaurant object with all important information.
     * @deprecated Due to some changes on the website used as the source of the application data.
     */
    @Deprecated
    public Restaurant getRestaurant(String url) throws IOException, JSONException {
        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(30000).followRedirects(true).get();

        String html = document.html();

        String json = document.select(REGEX_FOR_JSON_BEGINNING).toString();
        json = json.substring(json.indexOf("<!--") + 4, json.indexOf("-->"));
        JSONObject jsonObject = new JSONObject(json);

        // Get timeList aka working hours of restaurant.
        List<String> timeList = new ArrayList<>();
        for (int i = 0; i < document.select("tr.table-row__09f24__YAU9e").size(); i++) {
            String time = document.select("tr.table-row__09f24__YAU9e").get(i).text();
            timeList.add(time);
        }
        timeList.removeIf(String::isEmpty);
        timeList.replaceAll(s -> s.replaceAll("Open now|Closed now|\\(.*\\)", ""));
        restaurant.setTimeList(timeList);

        // Get list for restaurant images.
        List<String> images = new ArrayList<>();
        for (int i = 0; i < document.select(
                                            "div.photo-header-media__09f24__ojlZt.photo-header-media--overlay__09f24__KwCp5.display--inline-block__09f24__fEDiJ.border-color--default__09f24__NPAKY a.photo-header-media-link__09f24__xmWtR.css-1sie4w0 img.photo-header-media-image__09f24__A1WR_")
                                    .size(); i++) {
            if (i == 3) {
                break;
            }
            images.add(document.select(
                                       "div.photo-header-media__09f24__ojlZt.photo-header-media--overlay__09f24__KwCp5.display--inline-block__09f24__fEDiJ.border-color--default__09f24__NPAKY a.photo-header-media-link__09f24__xmWtR.css-1sie4w0 img.photo-header-media-image__09f24__A1WR_")
                               .get(i).attr("src"));
        }
        restaurant.setImages(images);
        restaurant.setName(jsonObject.getJSONObject(LEGACY).getJSONObject("bizDetailsProps")
                                     .getJSONObject("bizDetailsPageProps").getString("businessName"));
        restaurant.setAddress(document.select("span.raw__09f24__T4Ezm").text());

        String website = document.select(
                                         "div.css-1vhakgw:nth-child(1) > div:nth-child(1) > div:nth-child(1) > p:nth-child(2) > a:nth-child(1)")
                                 .attr("href");
        if (website.isEmpty()) {
            website = url;
        }

        restaurant.setWebsite(website);
        restaurant.setPhone(applyRegex(PHONE_REGEX, html, 1));
        restaurant.setRank(jsonObject.getJSONObject(LEGACY).getJSONObject("bizDetailsProps")
                                     .getJSONObject("gaDimensions").getJSONObject("www").getJSONArray("rating")
                                     .get(1).toString());
        return restaurant;
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

    /**
     * Generates url according to specific filter.
     *
     * @param cuisine Type of the restaurant.
     * @return Proper url for establishing the connection.
     * @deprecated Due to some changes on the website used as the source of the application data.
     */
    @Deprecated
    private static String generateSearchUrlForList(String cuisine) {
        return String.format(
                "https://www.yelp.com/search?find_desc=Restaurants&find_loc=München, Bayern, Germany&cflt=%s&sortby=recommended",
                cuisine);
    }
}
