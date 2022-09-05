package de.tum.in.ase.eist.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.tum.in.ase.eist.Restaurant;
import java.io.IOException;
import java.util.*;

import org.json.*;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
        private static final String SEARCH_CONST = "searchResultBusiness";
        private static final String LEGACY = "legacyProps";
        private static final String PHONE_REGEX = "class=\" css-1p9ibgf\".+?>([0-9]{3} [0-9]{7,8})</p>";
        private List<Restaurant> restaurants;
        private Restaurant restaurant;

        public RestaurantService() {
                this.restaurants = new ArrayList<>();
                this.restaurant = new Restaurant();
        }

        /**
         * Scraps the website to retrieve the list of restaurant with specified type.
         * 
         * @param cuisine - specifies the type of restaurants to search.
         * @throws IOException
         * @throws JSONException
         */
        public List<Restaurant> getAllRestaurants(String cuisine) throws IOException, JSONException {
                restaurants.clear();
                String url = generateSearchUrlForList(cuisine);
                Document doc = Jsoup
                                .connect(url)
                                .timeout(30000)
                                .followRedirects(true)
                                .get();

                String json = doc.select("script[type=\"application/json\"]").toString();
                json = json.substring(json.indexOf("<!--") + 4, json.indexOf("-->"));
                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonArray = jsonObject.getJSONObject(LEGACY).getJSONObject("searchAppProps")
                                .getJSONObject("searchPageProps").getJSONArray("mainContentComponentsListProps");

                for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).toString().contains(SEARCH_CONST)) {
                                Restaurant temp = new Restaurant();
                                temp.setName(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("name"));
                                temp.setAddress(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getJSONArray("neighborhoods").get(0).toString());
                                temp.setPhone(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getString("phone"));
                                temp.setRank(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getString("rating"));
                                temp.setWebsite("https://www.yelp.com"
                                                + jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                                .getString("businessUrl"));
                                temp.setPriceRange(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getString("priceRange"));
                                temp.setPreviewImg(
                                                jsonArray.getJSONObject(i).getJSONObject("scrollablePhotos")
                                                                .getJSONArray("photoList")
                                                                .getJSONObject(0).getString("srcset"));
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
         * 
         * Creates a list of restaurants from the next page of website.
         * 
         * @param url - URL to website with restaurant.
         * @return List with Restaurant objects.
         * @throws IOException
         * @throws JSONException
         */
        private List<Restaurant> getRestaurantsFromNextPage(String url) throws IOException, JSONException {
                List<Restaurant> tmplist = new ArrayList<Restaurant>();
                tmplist.clear();
                Document doc = Jsoup
                                .connect(url)
                                .timeout(30000)
                                .followRedirects(true)
                                .get();

                String json = doc.select("script[type=\"application/json\"]").toString();
                json = json.substring(json.indexOf("<!--") + 4, json.indexOf("-->"));
                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonArray = jsonObject.getJSONObject(LEGACY).getJSONObject("searchAppProps")
                                .getJSONObject("searchPageProps").getJSONArray("mainContentComponentsListProps");

                for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).toString().contains(SEARCH_CONST)) {
                                Restaurant temp = new Restaurant();
                                temp.setName(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST).getString("name"));
                                temp.setAddress(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getJSONArray("neighborhoods").get(0).toString());
                                temp.setPhone(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getString("phone"));
                                temp.setRank(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getString("rating"));
                                temp.setWebsite("https://www.yelp.com"
                                                + jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                                .getString("businessUrl"));
                                temp.setPriceRange(jsonArray.getJSONObject(i).getJSONObject(SEARCH_CONST)
                                                .getString("priceRange"));
                                temp.setPreviewImg(
                                                jsonArray.getJSONObject(i).getJSONObject("scrollablePhotos")
                                                                .getJSONArray("photoList")
                                                                .getJSONObject(0).getString("srcset"));
                                temp.setBusinessID(jsonArray.getJSONObject(i).getString("bizId"));
                                tmplist.add(temp);
                        }
                }
                return tmplist;
        }

        /**
         * Scraps the website to retrieve information about specific restaurant.
         * 
         * @param url - URL to website with restaurant.
         * @throws IOException
         * @throws JSONException
         * @return Restaurant object with all important information.
         */
        public Restaurant getRestaurant(String url) throws IOException, JSONException {
                Document doc = Jsoup
                                .connect(url)
                                .userAgent("Mozilla/5.0")
                                .timeout(30000)
                                .followRedirects(true)
                                .get();

                String html = doc.html();

                String json = doc.select("script[type=\"application/json\"]").toString();
                json = json.substring(json.indexOf("<!--") + 4, json.indexOf("-->"));
                JSONObject jsonObject = new JSONObject(json);

                // Get timeList aka working hours of restaurant.
                List<String> timeList = new ArrayList<>();
                for (int i = 0; i < doc.select("tr.table-row__09f24__YAU9e").size(); i++) {
                        String time = doc.select("tr.table-row__09f24__YAU9e").get(i).text();
                        timeList.add(time);
                }
                timeList.removeIf(String::isEmpty);
                for (int i = 0; i < timeList.size(); i++) {
                        timeList.set(i, timeList.get(i).replaceAll("Open now|Closed now|\\(.*\\)", ""));
                }
                restaurant.setTimeList(timeList);

                // Get list for restaurant images.
                List<String> images = new ArrayList<>();
                for (int i = 0; i < doc.select(
                                "div.photo-header-media__09f24__ojlZt.photo-header-media--overlay__09f24__KwCp5.display--inline-block__09f24__fEDiJ.border-color--default__09f24__NPAKY a.photo-header-media-link__09f24__xmWtR.css-1sie4w0 img.photo-header-media-image__09f24__A1WR_")
                                .size(); i++) {
                        if (i == 3) {
                                break;
                        }
                        images.add(doc.select(
                                        "div.photo-header-media__09f24__ojlZt.photo-header-media--overlay__09f24__KwCp5.display--inline-block__09f24__fEDiJ.border-color--default__09f24__NPAKY a.photo-header-media-link__09f24__xmWtR.css-1sie4w0 img.photo-header-media-image__09f24__A1WR_")
                                        .get(i).attr("src"));
                }
                restaurant.setImages(images);

                restaurant.setName(jsonObject.getJSONObject(LEGACY).getJSONObject("bizDetailsProps")
                                .getJSONObject("bizDetailsPageProps").getString("businessName"));
                restaurant.setAddress(doc.select("span.raw__09f24__T4Ezm").text());

                String website = doc.select(
                                "div.css-1vhakgw:nth-child(1) > div:nth-child(1) > div:nth-child(1) > p:nth-child(2) > a:nth-child(1)")
                                .attr("href");
                if (website.isEmpty()) {
                        website = url;
                }

                restaurant.setWebsite(website);
                restaurant.setPhone(applyRegex(PHONE_REGEX, html, 1));
                restaurant.setRank(jsonObject.getJSONObject(LEGACY).getJSONObject("bizDetailsProps")
                                .getJSONObject("gaDimensions").getJSONObject("www").getJSONArray("rating").get(1)
                                .toString());
                return restaurant;
        }

        /**
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

        /**
         * Generates url with specific filter.
         * 
         * @param cuisine - Type of restaurant.
         * @return Proper url for establishing the connection.
         */
        public static String generateSearchUrlForList(String cuisine) {
                return "https://www.yelp.com/search?find_desc=Restaurants&find_loc=München, Bayern, Germany&cflt="
                                + cuisine
                                + "&sortby=recommended";
        }
}
