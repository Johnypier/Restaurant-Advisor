package de.tum.in.ase.eist.view;

import de.tum.in.ase.eist.ClientApplication;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class SelectionScene extends Scene {
        private static Rectangle2D screen = Screen.getPrimary().getBounds();
        private static double boxHeight = screen.getMaxY() - screen.getMaxY() * 0.2;
        private static double boxWidth = screen.getMaxX() - screen.getMaxX() * 0.2;
        private static final String BUTTONSTYLE = "-fx-background-color: transperent;";
        private static final String LABELSTYLE = "-fx-font-size: 15;"
                        + "-fx-font-family: 'Comic Sans MS';"
                        + "-fx-text-fill: rgb(255,255,255);"
                        + "-fx-padding: 17;"
                        + "-fx-font-weight: bolder;"
                        + "-fx-background-color: rgba(54, 69, 79, 0.8);"
                        + "-fx-background-radius: 40;"
                        + "-fx-background-size: 50;";

        /**
         * SelectionScene constructor, creates scene with tiles for different
         * restaurants categories.
         *
         * @param clientApplication - Takes instance of user application to interact
         *                          with it.
         */
        public SelectionScene(ClientApplication clientApplication) {
                super(new VBox(), boxWidth + 150, boxHeight + 100);

                var italianBox = setupRestaurant("italian", "Italian Restaurants", clientApplication);
                italianBox.setBackground(createBackground("https://i.imgur.com/Jd4Uk7s.jpg"));
                var franceBox = setupRestaurant("french", "French Restaurants", clientApplication);
                franceBox.setBackground(createBackground("https://i.imgur.com/nqahm4U.jpg"));
                var chineseBox = setupRestaurant("chinese", "Chinese Restaurants", clientApplication);
                chineseBox.setBackground(createBackground("https://i.imgur.com/nExBmeY.jpg"));
                var japaneseBox = setupRestaurant("japanese", "Japanese Restaurants", clientApplication);
                japaneseBox.setBackground(createBackground("https://i.imgur.com/lylPx9o.gif"));
                var turkishBox = setupRestaurant("turkish", "Turkish Restaurants ", clientApplication);
                turkishBox.setBackground(createBackground("https://i.imgur.com/DcaHavX.gif"));
                var indianBox = setupRestaurant("indpak", "Indian Restaurants", clientApplication);
                indianBox.setBackground(createBackground("https://i.imgur.com/kEkfEFz.jpg"));

                var topHbox = new HBox(italianBox, franceBox, chineseBox);
                var downHbox = new HBox(japaneseBox, turkishBox, indianBox);
                topHbox.setSpacing(5);
                topHbox.setAlignment(Pos.CENTER);
                downHbox.setSpacing(5);
                downHbox.setAlignment(Pos.CENTER);
                var vBox = new VBox(topHbox, downHbox);
                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-background-color: rgb(32, 35, 36);");
                vBox.setSpacing(5);
                setRoot(vBox);
        }

        /**
         * Creates new VBox which will display the box of specific restaurant.
         * Currently this is not final implementation.
         *
         * @param type              - specifies the type of restaurant.
         * @param labelText         - Represents the desired text of VBox label.
         * @param clientApplication - Takes instance of user application to interact
         *                          with it.
         * @return VBox with label and button which shows new scene with restaurants
         *         list.
         */
        private VBox setupRestaurant(String type, String labelText, ClientApplication clientApplication) {
                var label = new Label(labelText);
                label.setStyle(LABELSTYLE);
                var button = new Button();
                button.setOnAction(action -> clientApplication.showRestaurantsScene(type, null));
                button.setPrefSize(50, 50);
                button.setStyle(BUTTONSTYLE);
                button.setGraphic(new ImageView(
                                new Image(getClass().getClassLoader().getResourceAsStream("selectButton.png"), 50, 50,
                                                false,
                                                true)));

                var box = new VBox(label, button);
                box.setAlignment(Pos.CENTER);
                box.setPrefSize(boxWidth / 2.5, boxHeight / 2.5);
                box.setSpacing(box.getPrefHeight() - 100);
                box.setStyle("-fx-border-color: rgba(4,217,255,0.8);" + "-fx-border-width: 2.5;");
                return box;
        }

        /**
         * Creates BackGround for vBox which will be resized dynamically.
         *
         * @param url - url to image stored on internet or in folder
         * @return BackGround with properly configured image
         */
        private Background createBackground(String url) {
                return new Background(new BackgroundImage(
                                new Image(url),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(300, 200, false, false, true, true)));
        }
}
