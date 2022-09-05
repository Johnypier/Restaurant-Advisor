package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.Reservation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ReservationService {
    private static final String JSON_STRING = ".json";
    private static final String RESERVATION_STRING = "reservations";
    private static final String RESTAURANT_STRING = "restaurant";
    private static final String ID_STRING = "id";
    private static final String CUSTOMER_STRING = "customer";
    private static final String PERSONS_STRING = "persons";
    private static final String DATE_STRING = "date";
    private static final String HOUR_STRING = "hour";
    private static final String CONFIRMATION_STRING = "confirmation";
    private List<Reservation> reservations;

    public ReservationService() {
        this.reservations = new ArrayList<>();
    }

    /**
     * 
     * Find all reservations of given person for specific restaurant.
     * 
     * @param type           - Type of restaurant, required to find proper json
     *                       file.
     * @param name           - Name of the customer.
     * @param restaurantName - Restaurant name.
     * @return List with Reservation objects for specified customer.
     * @throws JSONException
     * @throws IOException
     * @throws URISyntaxException
     */
    public List<Reservation> getAllReservations(String type, String name, String restaurantName)
            throws JSONException, IOException, URISyntaxException {
        this.reservations.clear();
        URI jsonPath = getClass().getClassLoader().getResource(type + JSON_STRING).toURI();
        JSONArray jsonarray = new JSONArray(readFileAsString(jsonPath));
        var status = false;
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject mainJsonObject = jsonarray.getJSONObject(i);
            if (mainJsonObject.getString(RESTAURANT_STRING).equals(restaurantName)) {
                status = true;
                JSONArray reservationsArray = mainJsonObject.getJSONArray(RESERVATION_STRING);
                for (int j = 0; j < reservationsArray.length(); j++) {
                    JSONObject jsonObject = reservationsArray.getJSONObject(j);
                    if (jsonObject.getString(CUSTOMER_STRING).equals(name)) {
                        this.reservations.add(new Reservation(
                                jsonObject.getString(ID_STRING),
                                jsonObject.getString(CUSTOMER_STRING),
                                jsonObject.getString(PERSONS_STRING),
                                jsonObject.getString(DATE_STRING),
                                jsonObject.getString(HOUR_STRING),
                                jsonObject.getString(CONFIRMATION_STRING)));
                    }
                }
                break;
            }
        }
        if (!status) {
            generateReservationObject(type, restaurantName);
        }
        if (this.reservations.isEmpty()) {
            this.reservations.add(new Reservation("empty", null, null, null, null, null));
        }
        return this.reservations;
    }

    /**
     * 
     * Deletes reservation with given id from specific restaurant.
     * 
     * @param type           - Type of restaurant, required to find proper json
     *                       file.
     * @param id             - ID of the reservation.
     * @param name           - Name of the customer.
     * @param restaurantName - Restaurant name.
     * @throws JSONException
     * @throws IOException
     * @throws URISyntaxException
     */
    public boolean deleteReservation(String type, String id, String name, String restaurantName)
            throws JSONException, IOException, URISyntaxException {
        URI jsonPath = getClass().getClassLoader().getResource(type + JSON_STRING).toURI();
        JSONArray jsonArray = new JSONArray(readFileAsString(jsonPath));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject mainJsonObject = jsonArray.getJSONObject(i);
            if (mainJsonObject.getString(RESTAURANT_STRING).equals(restaurantName)) {
                JSONArray reservationsArray = mainJsonObject.getJSONArray(RESERVATION_STRING);
                for (int j = 0; j < reservationsArray.length(); j++) {
                    JSONObject jsonObject = reservationsArray.getJSONObject(j);
                    if (jsonObject.getString(ID_STRING).equals(id)
                            && jsonObject.getString(CUSTOMER_STRING).equals(name)) {
                        reservationsArray.remove(j);
                        try (PrintWriter out = new PrintWriter(new FileWriter(jsonPath.getPath()))) {
                            out.write(jsonArray.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * Creates reservation object and adds it in to specific restaurant.
     * 
     * @param type           - Type of restaurant, required to find proper json
     *                       file.
     * @param name           - Name of the customer.
     * @param persons        - Amount of people for the reservation.
     * @param date           - Exact day:month:year of the reservation.
     * @param hour           - Exact time of the reservation.
     * @param confirmation   - Confirmation status of the reservation.
     * @param restaurantName - Restaurant name.
     * @throws JSONException
     * @throws IOException
     * @throws URISyntaxException
     */
    public boolean addReservation(String type, String name, String persons, String date, String hour,
            String confirmation, String restaurantName) throws JSONException, IOException, URISyntaxException {
        URI jsonPath = getClass().getClassLoader().getResource(type + JSON_STRING).toURI();
        JSONArray jsonArray = new JSONArray(readFileAsString(jsonPath));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject mainJsonObject = jsonArray.getJSONObject(i);
            if (mainJsonObject.getString(RESTAURANT_STRING).equals(restaurantName)) {
                JSONArray reservationsArray = mainJsonObject.getJSONArray(RESERVATION_STRING);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(ID_STRING, String.valueOf(ThreadLocalRandom.current().nextInt(0, 50000)));
                jsonObject.put(CUSTOMER_STRING, name);
                jsonObject.put(PERSONS_STRING, persons);
                jsonObject.put(DATE_STRING, date);
                jsonObject.put(HOUR_STRING, hour);
                jsonObject.put(CONFIRMATION_STRING, confirmation);
                reservationsArray.put(jsonObject);
                try (PrintWriter out = new PrintWriter(new FileWriter(jsonPath.getPath()))) {
                    out.write(jsonArray.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * Updates the Reservation object in specific restaurant.
     *
     * @param type           - Type of restaurant, required to find proper json
     *                       file.
     * @param id             - ID of the reservation.
     * @param name           - Name of the customer.
     * @param persons        - Amount of people for the reservation.
     * @param date           - Exact day:month:year of the reservation.
     * @param hour           - Exact time of the reservation.
     * @param confirmation   - Confirmation status of the reservation.
     * @param restaurantName - Restaurant name.
     * @throws JSONException
     * @throws IOException
     * @throws URISyntaxException
     */
    public boolean updateReservation(String type, String id, String name, String persons, String date, String hour,
            String confirmation, String restaurantName) throws JSONException, IOException, URISyntaxException {
        URI jsonPath = getClass().getClassLoader().getResource(type + JSON_STRING).toURI();
        JSONArray jsonArray = new JSONArray(readFileAsString(jsonPath));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject mainJsonObject = jsonArray.getJSONObject(i);
            if (mainJsonObject.getString(RESTAURANT_STRING).equals(restaurantName)) {
                JSONArray reservationsArray = mainJsonObject.getJSONArray(RESERVATION_STRING);
                for (int j = 0; j < reservationsArray.length(); j++) {
                    JSONObject jsonObject = reservationsArray.getJSONObject(j);
                    if (jsonObject.getString(ID_STRING).equals(id)
                            && jsonObject.getString(CUSTOMER_STRING).equals(name)) {
                        jsonObject.remove(CUSTOMER_STRING);
                        jsonObject.remove(PERSONS_STRING);
                        jsonObject.remove(DATE_STRING);
                        jsonObject.remove(HOUR_STRING);
                        jsonObject.remove(CONFIRMATION_STRING);
                        jsonObject.put(CUSTOMER_STRING, name);
                        jsonObject.put(PERSONS_STRING, persons);
                        jsonObject.put(DATE_STRING, date);
                        jsonObject.put(HOUR_STRING, hour);
                        jsonObject.put(CONFIRMATION_STRING, confirmation);
                        try (PrintWriter out = new PrintWriter(new FileWriter(jsonPath.getPath()))) {
                            out.write(jsonArray.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * If restaurant doesn't exist in the json file, creates new object of it and
     * adds it.
     * 
     * @param type           - Type of restaurant, required to find proper json
     *                       file.
     * @param restaurantName - Restaurant name.
     * @throws JSONException
     * @throws IOException
     * @throws URISyntaxException
     */
    private void generateReservationObject(String type, String restaurantName)
            throws JSONException, IOException, URISyntaxException {
        URI path = getClass().getClassLoader().getResource(type + JSON_STRING).toURI();
        JSONArray jsonArray = new JSONArray(readFileAsString(path));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RESTAURANT_STRING, restaurantName);
        jsonObject.put(RESERVATION_STRING, new JSONArray());
        jsonArray.put(jsonObject);
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getPath()))) {
            out.write(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Creates string representation of the file.
     * 
     * @param path - Path to the file.
     * @return - String representation of json file.
     * @throws IOException
     */
    private static String readFileAsString(URI path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
