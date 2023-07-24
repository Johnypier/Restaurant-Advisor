package client.app;

import client.controller.RestaurantClientController;
import client.controller.ReviewClientController;
import client.view.DetailsScene;
import client.view.LoginScene;
import client.view.RestaurantScene;
import client.view.SelectionScene;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import restaurants.information.Restaurant;

import java.util.Objects;

public class ClientApplication extends Application {
    private final RestaurantClientController restaurantClientController = new RestaurantClientController();
    private final ReviewClientController reviewClientController = new ReviewClientController();
    private String firstName = "";
    private String lastName = "";
    private Stage stage;

    // Used to launch the application.
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("Restaurant Advisor");
        this.stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("profile.png"))));

        primaryStage.setScene(new LoginScene(this));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Shows the scene where the user can select the type of the restaurant.
     */
    public void showSelectionScene() {
        this.stage.setScene(new SelectionScene(this));
        this.stage.setResizable(true);
    }

    /**
     * Shows the scene with a list of available restaurants for the selected type.
     *
     * @param type        Indicates the type of the restaurant.
     * @param restaurants Cached list of the restaurants.
     */
    public void showRestaurantsScene(String type, ObservableList<Restaurant> restaurants) {
        this.stage.setScene(
                new RestaurantScene(restaurantClientController, this, restaurants, type));
    }

    /**
     * Shows the scene with the restaurant's details.
     *
     * @param restaurants Cached list of the restaurants.
     * @param type        Indicates the type of the restaurant.
     * @param restaurant  Object containing restaurant's information.
     */
    public void showDetailsScene(String url, ObservableList<Restaurant> restaurants, String type,
                                 Restaurant restaurant) {
        this.stage.setScene(new DetailsScene(this, restaurantClientController, restaurants, reviewClientController,
                                             url, type, restaurant));
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String value) {
        this.firstName = value;
    }

    public void setLastName(String value) {
        this.lastName = value;
    }
}
