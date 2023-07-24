package client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import restaurants.information.Restaurant;

public class RestaurantClientController {
    private final WebClient webClient;
    private final List<Restaurant> restaurants;
    private Restaurant restaurant;

    public RestaurantClientController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.restaurants = new ArrayList<>();
        this.restaurant = new Restaurant();
    }

    public void getAllRestaurants(Consumer<List<Restaurant>> restaurantsConsumer, String path) {
        webClient.get().uri(uriBuilder -> uriBuilder
                .path("/restaurants")
                .queryParam("type", path)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Restaurant>>() {
                })
                .onErrorStop()
                .subscribe(newRestaurants -> {
                    restaurants.clear();
                    restaurants.addAll(newRestaurants);
                    restaurantsConsumer.accept(restaurants);
                });
    }

    public void getRestaurant(Consumer<Restaurant> openingHoursConsumer, String url) {
        webClient.get().uri(uriBuilder -> uriBuilder
                .path("/info")
                .queryParam("restaurantUrl", url)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Restaurant>() {
                })
                .onErrorStop()
                .subscribe(newRestaurant -> {
                    restaurant = newRestaurant;
                    openingHoursConsumer.accept(restaurant);
                });
    }
}
