package de.tum.in.ase.eist.view;

import java.util.List;

import de.tum.in.ase.eist.ClientApplication;
import de.tum.in.ase.eist.Restaurant;
import de.tum.in.ase.eist.controller.RestaurantController;
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

public class RestaurantScene extends Scene {
    private final ObservableList<Restaurant> restaurantList;
    private TableView<Restaurant> table;
    private static final String BUTTON = "-fx-background-color: transperent;" + "-fx-text-fill: white;"
            + "-fx-font-size: 20;";
    private static final String BUTTONBOX = "-fx-background-color: #202324;";

    /**
     * RestaurantScene constructor, creates scene with tiles for different
     * restaurants categories.
     *
     * @param restaurantController - Takes instance of restaurant controller to
     *                             interact with it.
     * @param clientApplication    - Takes instance of user application to interact
     *                             with it.
     * @param pathRest             - Path to the desired type of restaurants json
     *                             file.
     * @param pathRev              - Path to the desired type of restaurants'
     *                             reviews json file.
     * @param type                 - specifies the type of restaurant.
     */
    public RestaurantScene(RestaurantController restaurantController, ClientApplication clientApplication,
            ObservableList<Restaurant> restaurants,
            String type) {
        super(new VBox(), 800, 600);
        if (restaurants == null || restaurants.isEmpty()) {
            this.restaurantList = FXCollections.observableArrayList();
        } else {
            this.restaurantList = restaurants;
        }

        this.table = new TableView<>(restaurantList);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Restaurant, String> colName = new TableColumn<>("Restaurant");
        TableColumn<Restaurant, String> colAddress = new TableColumn<>("Location");
        TableColumn<Restaurant, String> colRank = new TableColumn<>("Rating");

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        configurator(colName);
        configurator(colAddress);
        configurator(colRank);

        var back = new Button();
        back.setGraphic(new ImageView(
                new Image(getClass().getClassLoader().getResourceAsStream("left.png"), 46.5, 46.5, true,
                        true)));
        back.setOnAction(action -> clientApplication.showSelectionScene());
        back.setStyle(BUTTON);
        var backButtonLabe = new Label("Back");
        backButtonLabe.setStyle(BUTTON);
        var backButtonBox = new VBox(backButtonLabe, back);
        backButtonBox.setAlignment(Pos.CENTER);
        backButtonBox.setSpacing(5);
        backButtonBox.setStyle(BUTTONBOX);

        var select = new Button();
        select.setGraphic(new ImageView(
                new Image(getClass().getClassLoader().getResourceAsStream("selectButton.png"), 50, 50, true,
                        true)));
        select.setOnAction(action -> {
            if (table.getSelectionModel().getSelectedIndex() != -1) {
                show(this.table, clientApplication, type);
            }
        });
        select.setStyle(BUTTON);
        var selectLabel = new Label("Select");
        selectLabel.setStyle(BUTTON);
        var selectionButtonBox = new VBox(selectLabel, select);
        selectionButtonBox.setAlignment(Pos.CENTER);
        selectionButtonBox.setSpacing(5);
        selectionButtonBox.setStyle(BUTTONBOX);

        var hBox = new HBox(backButtonBox, selectionButtonBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(40);
        hBox.setPadding(new Insets(5));
        hBox.setStyle(BUTTONBOX);

        table.getStylesheets().add(getClass().getClassLoader().getResource("restaurantTable.css").toExternalForm());
        table.getColumns().add(colName);
        table.getColumns().add(colAddress);
        table.getColumns().add(colRank);

        var vBox = new VBox(table, hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(2);
        setRoot(vBox);

        if (restaurantList.isEmpty()) {
            restaurantController.getAllRestaurants(this::setRestaurants, type);
        }
    }

    /**
     * Shows the scene with the specified restaurants types.
     *
     * @param table             - current tableView instance.
     * @param clientApplication - current clientApplication instance.
     * @param type              - specifies the restaurant's type.
     */
    private void show(TableView<Restaurant> table, ClientApplication clientApplication, String type) {
        clientApplication
                .showDetailsScene(restaurantList.get(table.getSelectionModel().getSelectedIndex()).getWebsite(),
                        restaurantList, type, null);
    }

    /**
     * Represents the method for consumer interface.
     *
     * @param restaurants - List which will be passed to consumer.
     */
    private void setRestaurants(List<Restaurant> restaurants) {
        Platform.runLater(() -> restaurantList.setAll(restaurants));
    }

    /**
     * Sets the column properties.
     *
     * @param column - Column to configure.
     */
    private void configurator(TableColumn<Restaurant, String> column) {
        column.setSortable(true);
        column.setPrefWidth(620 / 4D);
    }
}
