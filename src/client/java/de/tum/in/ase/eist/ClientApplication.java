package de.tum.in.ase.eist;

import de.tum.in.ase.eist.controller.ReservationController;
import de.tum.in.ase.eist.controller.RestaurantController;
import de.tum.in.ase.eist.controller.ReviewController;
import de.tum.in.ase.eist.view.DetailsScene;
import de.tum.in.ase.eist.view.LoginScene;
import de.tum.in.ase.eist.view.ReservationScene;
import de.tum.in.ase.eist.view.RestaurantScene;
import de.tum.in.ase.eist.view.SelectionScene;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientApplication extends Application {
    private RestaurantController restaurantController = new RestaurantController();
    private ReviewController reviewController = new ReviewController();
    private ReservationController reservationController = new ReservationController();
    private String firstName = "";
    private String lastName = "";
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("Restaurant Advisor");
        this.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("profile.png")));

        primaryStage.setScene(new LoginScene(this));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Shows the scene where user can select specific type of restaurant.
     */
    public void showSelectionScene() {
        this.stage.setScene(new SelectionScene(this));
        this.stage.setResizable(true);
    }

    /**
     * Shows the scene with list of available restaurants of the selected type.
     * 
     * @param pathRest - Path to the desired type of restaurants json
     *                 file.
     * @param pathRev  - Path to the desired type of restaurants'
     *                 reviews json file.
     * @param type     - indicates the type of the restaurant.
     */
    public void showRestaurantsScene(String type, ObservableList<Restaurant> restaurants) {
        this.stage.setScene(
                new RestaurantScene(restaurantController, this, restaurants, type));
    }

    /**
     * Shows the scene with detailed information about specific restaurant.
     * 
     * @param restaurant - object storing restaurant's information.
     * @param reviews    - set of the reviews for this restaurant.
     * @param paths      - list containing paths to photos of the restaurant.
     * @param pathRest   - Path to the desired type of restaurants json
     *                   file.
     * @param pathRev    - Path to the desired type of restaurants'
     *                   reviews json file.
     * @param type       - indicates the type of the restaurant.
     */
    public void showDetailsScene(String url, ObservableList<Restaurant> restaurants, String type,
            Restaurant restaurant) {
        this.stage
                .setScene(new DetailsScene(this, restaurantController, restaurants, reviewController, url, type,
                        restaurant));
    }

    public void showReservationScene(Restaurant restaurant, String type, ObservableList<Restaurant> restaurants) {
        this.stage.setScene(new ReservationScene(this, restaurant, type, restaurants, reservationController));
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
