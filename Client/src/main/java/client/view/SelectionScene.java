package client.view;

import client.app.ClientApplication;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.Objects;

import static client.utility.SelectionSceneConstants.*;

public class SelectionScene extends Scene {
    // Configuration constants which require JavaFX thread.
    private static final Rectangle2D SCREEN = Screen.getPrimary().getBounds();
    private static final double ELEMENT_HEIGHT = SCREEN.getMaxY() - SCREEN.getMaxY() * 0.2;
    private static final double ELEMENT_WIDTH = SCREEN.getMaxX() - SCREEN.getMaxX() * 0.2;

    /**
     * SelectionScene constructor, creates scene with different tiles,
     * where each include a restaurant category.
     *
     * @param clientApplication Instance of the user application to interact
     *                          with it.
     */
    public SelectionScene(ClientApplication clientApplication) {
        super(new VBox(), ELEMENT_WIDTH + 150, ELEMENT_HEIGHT + 100);

        var italianBox = setupRestaurantTile("italian", "Italian Restaurants", clientApplication,
                                             "https://i.imgur.com/Jd4Uk7s.jpg");
        var franceBox = setupRestaurantTile("french", "French Restaurants", clientApplication,
                                            "https://i.imgur.com/nqahm4U.jpg");
        var chineseBox = setupRestaurantTile("chinese", "Chinese Restaurants", clientApplication,
                                             "https://i.imgur.com/nExBmeY.jpg");
        var japaneseBox = setupRestaurantTile("japanese", "Japanese Restaurants", clientApplication,
                                              "https://i.imgur.com/lylPx9o.gif");
        var turkishBox = setupRestaurantTile("turkish", "Turkish Restaurants ", clientApplication,
                                             "https://i.imgur.com/DcaHavX.gif");
        var indianBox = setupRestaurantTile("indpak", "Indian Restaurants", clientApplication,
                                            "https://i.imgur.com/kEkfEFz.jpg");

        var topHbox = new HBox(italianBox, franceBox, chineseBox);
        var downHbox = new HBox(japaneseBox, turkishBox, indianBox);
        topHbox.setSpacing(BOX_SPACING);
        topHbox.setAlignment(Pos.CENTER);
        downHbox.setSpacing(BOX_SPACING);
        downHbox.setAlignment(Pos.CENTER);
        var mainBox = new VBox(topHbox, downHbox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle(MAIN_BOX_STYLE);
        mainBox.setSpacing(BOX_SPACING);
        setRoot(mainBox);
    }

    /**
     * Creates a new tile for the specified restaurant type.
     *
     * @param type              Type of the restaurant.
     * @param labelText         Text of the VBox label.
     * @param clientApplication Instance of the user application to interact
     *                          with it.
     * @param url               The URL of the image
     * @return VBox with label and button.
     */
    private VBox setupRestaurantTile(String type, String labelText, ClientApplication clientApplication, String url) {
        var label = new Label(labelText);
        label.setStyle(LABEL_STYLE);

        var button = new Button();
        button.setOnAction(action -> clientApplication.showRestaurantsScene(type, null));
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setStyle(BUTTON_STYLE);
        button.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("selectButton.png")),
                          BUTTON_SIZE, BUTTON_SIZE, false, true)));

        var temp = new VBox(label, button);
        temp.setAlignment(Pos.CENTER);
        temp.setPrefSize(ELEMENT_WIDTH / 2.5, ELEMENT_HEIGHT / 2.5);
        temp.setSpacing(temp.getPrefHeight() - 100);
        temp.setStyle("-fx-border-color: rgba(4,217,255,0.8);" + "-fx-border-width: 2.5;");
        temp.setBackground(createBackground(url));
        return temp;
    }

    /**
     * Creates a new BackGround for any BOX object
     * which will be resized according to the user's screen.
     *
     * @param url The URL of the image
     * @return BackGround with a properly configured image
     */
    private Background createBackground(String url) {
        return new Background(new BackgroundImage(
                new Image(url), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(TILE_BACKGROUND_WIDTH, TILE_BACKGROUND_HEIGHT, false, false, true, true)));
    }
}
