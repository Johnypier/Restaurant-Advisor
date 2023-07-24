package client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import restaurants.information.Review;

public class ReviewClientController {
    private final WebClient webClient;
    private final List<Review> reviews;

    public ReviewClientController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.reviews = new ArrayList<>();
    }

    public void getAllReviews(Consumer<List<Review>> reviewConsumer, String url) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reviews")
                        .queryParam("restaurantUrl", url)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Review>>() {
                })
                .onErrorStop()
                .subscribe(newReviews -> {
                    reviews.clear();
                    reviews.addAll(newReviews);
                    reviewConsumer.accept(reviews);
                });
    }
}
