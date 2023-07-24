package client.view;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import client.app.ClientApplication;
import client.controller.RestaurantClientController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import restaurants.information.Restaurant;

import static client.utility.RestaurantSceneConstants.*;

public class RestaurantScene extends Scene {
    private final ObservableList<Restaurant> restaurantList;
    private final TableView<Restaurant> table;

    /**
     * RestaurantScene constructor, creates a scene with the tiles for the different
     * restaurants categories.
     *
     * @param restaurantClientController The instance of the restaurant controller to
     *                                   interact with it.
     * @param clientApplication          The instance of the user application to interact
     *                                   with it.
     * @param restaurants                The list of restaurants to display.
     * @param type                       The type of the restaurant.
     */
    public RestaurantScene(RestaurantClientController restaurantClientController, ClientApplication clientApplication,
                           ObservableList<Restaurant> restaurants, String type) {
        super(new VBox(), SCENE_WIDTH, SCENE_HEIGHT);
        this.restaurantList =
                (restaurants == null || restaurants.isEmpty()) ? FXCollections.observableArrayList() : restaurants;
        this.table = new TableView<>(restaurantList);

        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Restaurant, String> colName = columnConfigurator("Restaurant", "name");
        TableColumn<Restaurant, String> colAddress = columnConfigurator("Location", "address");
        TableColumn<Restaurant, String> colRank = columnConfigurator("Rating", "rank");
        table.getColumns().add(colName);
        table.getColumns().add(colAddress);
        table.getColumns().add(colRank);
        table.getStylesheets().add(
                Objects.requireNonNull(getClass().getClassLoader().getResource("restaurantTable.css"))
                       .toExternalForm());

        var backButton = new Button();
        backButton.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("left.png")),
                          BACK_BUTTON_SIZE, BACK_BUTTON_SIZE, true, true)));
        backButton.setOnAction(action -> clientApplication.showSelectionScene());
        backButton.setStyle(BUTTON_STYLE);
        var backButtonLabel = new Label("Back");
        backButtonLabel.setStyle(BUTTON_STYLE);

        var selectButton = new Button();
        selectButton.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("selectButton.png")),
                          SELECT_BUTTON_SIZE, SELECT_BUTTON_SIZE, true, true)));
        selectButton.setOnAction(action -> {
            if (table.getSelectionModel().getSelectedIndex() != -1) {
                showRestaurantDetails(this.table, clientApplication, type);
            }
        });
        selectButton.setStyle(BUTTON_STYLE);
        var selectLabel = new Label("Select");
        selectLabel.setStyle(BUTTON_STYLE);

        var backButtonBox = new VBox(backButtonLabel, backButton);
        backButtonBox.setAlignment(Pos.CENTER);
        backButtonBox.setSpacing(BUTTONS_BOX_SPACING);
        backButtonBox.setStyle(BUTTONS_BOX_STYLE);

        var selectionButtonBox = new VBox(selectLabel, selectButton);
        selectionButtonBox.setAlignment(Pos.CENTER);
        selectionButtonBox.setSpacing(BUTTONS_BOX_SPACING);
        selectionButtonBox.setStyle(BUTTONS_BOX_STYLE);

        var controlsBox = new HBox(backButtonBox, selectionButtonBox);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setSpacing(CONTROLS_BOX_SPACING);
        controlsBox.setPadding(new Insets(BUTTONS_BOX_SPACING));
        controlsBox.setStyle(BUTTONS_BOX_STYLE);

        var mainBox = new VBox(table, controlsBox);
        mainBox.setAlignment(Pos.CENTER);
        setRoot(mainBox);

        // Retrieve the list of the restaurants from the server.
        if (restaurantList.isEmpty()) {
            Consumer<List<Restaurant>> restaurantsConsumer =
                    response -> Platform.runLater(() -> restaurantList.setAll(response));
            restaurantClientController.getAllRestaurants(restaurantsConsumer, type);
        }
    }

    /**
     * Shows the scene with the detailed information of the selected restaurant.
     *
     * @param table             Current tableView instance.
     * @param clientApplication Current clientApplication instance.
     * @param type              The restaurant's type.
     */
    private void showRestaurantDetails(TableView<Restaurant> table, ClientApplication clientApplication, String type) {
        clientApplication.showDetailsScene(
                restaurantList.get(table.getSelectionModel().getSelectedIndex()).getWebsite(),
                restaurantList, type, null);
    }

    /**
     * Sets the column properties.
     *
     * @param columnName Name of the column
     * @param valueName  Name of the column values property
     */
    private TableColumn<Restaurant, String> columnConfigurator(String columnName, String valueName) {
        TableColumn<Restaurant, String> column = new TableColumn<>(columnName);
        column.setSortable(true);
        column.setPrefWidth(TABLE_COLUMN_WIDTH);
        column.setCellValueFactory(new PropertyValueFactory<>(valueName));
        return column;
    }
}
