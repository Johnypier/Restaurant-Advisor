package de.tum.in.ase.eist.view;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import de.tum.in.ase.eist.ClientApplication;
import de.tum.in.ase.eist.Restaurant;
import de.tum.in.ase.eist.Review;
import de.tum.in.ase.eist.controller.RestaurantController;
import de.tum.in.ase.eist.controller.ReviewController;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Screen;

public class DetailsScene extends Scene {
        private static Rectangle2D screen = Screen.getPrimary().getBounds();
        private static double boxHeight = screen.getMaxY() - screen.getMaxY() * 0.2;
        private static double boxWidth = screen.getMaxX() - screen.getMaxX() * 0.2;
        private Image first;
        private Image second;
        private Image third;
        private Restaurant restaurant;
        private static final String FONT_FAMILY = "-fx-font-family: 'Yu Gothic';";
        private static final String WHITE = "-fx-text-fill: white;";
        private static final String TRANSPARENT = "-fx-background-color: transparent;";
        private static final String SPECIAL = "-fx-background-color: #202324;";
        private static final String BOXSTYLE = "-fx-border-color: rgba(4,217,255,0.5);" + "-fx-padding: 10,0,0,0;"
                        + "-fx-border-width: 3;";
        private static final String LABELSTYLE = "-fx-font-size: 20;"
                        + FONT_FAMILY
                        + WHITE
                        + "-fx-font-weight: bolder;"
                        + TRANSPARENT;

        /**
         * DetailsScene constructor, creates scene with detailed information about
         * restaurant.
         *
         * @param clientApplication    - Takes instance of user application to interact
         *                             with it.
         * @param restaurantController - Takes instance of restaurant controller to
         *                             interact with it.
         * @param restaurants          - Takes the list of restaurants to display in
         *                             table, so it won't be loaded again.
         * @param reviewController     - Takes instance of review controller to
         *                             interact with it.
         * @param url                  - URL to website with restaurant.
         * @param type                 - Specifies the type of restaurant.
         */
        public DetailsScene(ClientApplication clientApplication, RestaurantController restaurantController,
                        ObservableList<Restaurant> restaurants, ReviewController reviewController, String url,
                        String type, Restaurant restaurantVar) {
                super(new VBox(), boxWidth + 150, boxHeight + 150);

                // Loads images from server.
                Consumer<Restaurant> cons = c -> this.restaurant = c;
                Consumer<List<Review>> rev = r -> this.restaurant.setReviews(r);
                if (restaurantVar == null) {
                        restaurantController.getRestaurant(cons, url);
                        while (restaurant == null) {
                                // Waiting for data...
                                System.out.print("");
                        }
                } else {
                        this.restaurant = restaurantVar;
                }
                if (restaurant.getImages() != null || !restaurant.getImages().isEmpty()) {
                        this.first = new Image(restaurant.getImages().get(0));
                        this.second = new Image(restaurant.getImages().get(1));
                        this.third = new Image(restaurant.getImages().get(2));
                } else {
                        InputStream in = getClass().getClass().getResourceAsStream("placeHolderImage.png");
                        this.first = new Image(in);
                        this.second = new Image(in);
                        this.third = new Image(in);
                }

                // Loads reviews from server.
                if (restaurantVar == null) {
                        reviewController.getAllReviews(rev, url);
                        long limit = System.currentTimeMillis() + 3000;
                        while (restaurant.getReviews() == null || restaurant.getReviews().isEmpty()) {
                                // Waiting for data...
                                System.out.print("");
                                if (System.currentTimeMillis() == limit) {
                                        break;
                                }

                        }
                }
                // MainBoxes, change values here.
                var webView = new WebView();
                webView.setPrefSize(boxWidth / 3, boxHeight / 3);
                webView.setStyle(BOXSTYLE);
                var webEngine = webView.getEngine();
                webEngine.load("https://www.google.com/maps/search/" + restaurant.getName()
                                + " Munich+Germany/@48.1549107,11.5418357,11z");

                var commentPane = new ScrollPane(createCommentsBox());
                commentPane.fitToHeightProperty().set(true);
                commentPane.fitToWidthProperty().set(true);
                commentPane.setMaxHeight(boxHeight / 3);
                commentPane.setMaxWidth(boxWidth);
                commentPane.getStylesheets()
                                .add(getClass().getClassLoader().getResource("detailsScrollPane.css").toExternalForm());

                var informationBox = createInformationBox(clientApplication, restaurants, type);
                informationBox.setPrefSize(boxWidth / 3, boxHeight / 3);
                informationBox.setStyle(BOXSTYLE);
                informationBox.setSpacing(15);

                var timeBox = new VBox(createTimeBox());
                timeBox.setPrefSize(boxWidth / 3, boxHeight / 3);
                timeBox.setSpacing(5);
                timeBox.setStyle(BOXSTYLE);

                var middleBox = new HBox(informationBox, webView, timeBox);
                middleBox.setAlignment(Pos.CENTER);
                middleBox.setSpacing(5);

                var pictureBox = new HBox(createImageBox(first), createImageBox(second),
                                createImageBox(third));
                pictureBox.setAlignment(Pos.CENTER);
                pictureBox.setSpacing(5);

                var mainBox = new VBox(pictureBox, middleBox, commentPane);
                mainBox.setAlignment(Pos.CENTER);
                mainBox.setSpacing(10);
                mainBox.setStyle(SPECIAL);
                setRoot(mainBox);
        }

        /**
         * Creates simple VBox with restaurant's information and more.
         * 
         * @param clientApplication - Takes instance of user application to interact
         *                          with it.
         * @param restaurants       - Takes the list of restaurants to display in table,
         *                          so it won't be loaded again.
         * @param type              - Specifies the type of restaurant.
         * @return VBox with restaurant's information.
         */
        private VBox createInformationBox(ClientApplication clientApplication, ObservableList<Restaurant> restaurants,
                        String type) {
                var nameLabel = textFieldConfigurator("\uD83C\uDFE2 Name: " + restaurant.getName());
                nameLabel.setAlignment(Pos.CENTER);
                nameLabel.setStyle(LABELSTYLE + "-fx-border-color: white;" + "-fx-border-width: 0 0 2 0;"
                                + "-fx-underline: true;");

                var ratingBuilder = new StringBuilder();
                ratingBuilder.append("Rating: " + Double.valueOf(restaurant.getRank()).intValue());
                var rate = Double.valueOf(restaurant.getRank()).intValue();
                for (int i = 0; i < rate; i++) {
                        ratingBuilder.append(" \uD83C\uDF1F");
                }
                var ratingLabel = new Label(ratingBuilder.toString());
                ratingLabel.setAlignment(Pos.CENTER_LEFT);
                ratingLabel.setStyle(LABELSTYLE);

                var addressLabel = new Label("\uD83D\uDCCD Address: " + restaurant.getAddress());
                addressLabel.setAlignment(Pos.CENTER_LEFT);
                addressLabel.setStyle(LABELSTYLE);

                var websiteLabel = new Label("\uD83C\uDF10 Website: ");
                websiteLabel.setAlignment(Pos.CENTER_LEFT);
                websiteLabel.setStyle(LABELSTYLE);

                var link = new Hyperlink("Go to website!");
                link.setAlignment(Pos.CENTER_LEFT);
                link.setStyle("-fx-font-size: 25;");
                link.setOnAction(event -> clientApplication.getHostServices()
                                .showDocument((restaurant.getWebsite().contains("https://www.yelp.com"))
                                                ? restaurant.getWebsite()
                                                : "https://www.yelp.com" + restaurant.getWebsite()));

                var webBox = new HBox(websiteLabel, link);
                webBox.setSpacing(10);
                webBox.setStyle(TRANSPARENT);
                webBox.setAlignment(Pos.CENTER_LEFT);

                var phoneLabel = new Label(
                                (restaurant.getPhone() == null || restaurant.getPhone().isEmpty())
                                                ? "\uD83D\uDCDE Phone: Phone was not provided."
                                                : "\uD83D\uDCDE Phone: " + restaurant.getPhone());
                phoneLabel.setAlignment(Pos.CENTER_LEFT);
                phoneLabel.setStyle(LABELSTYLE);

                var backButtonLabel = new Label("Back");
                backButtonLabel.setStyle(WHITE + "-fx-font-size: 16;");
                var back = new Button();
                back.setOnAction(action -> clientApplication.showRestaurantsScene(type, restaurants));
                back.setGraphic(new ImageView(
                                new Image(getClass().getClassLoader().getResourceAsStream("left.png"), 60,
                                                60, false,
                                                true)));
                back.setStyle(TRANSPARENT);

                var bookButtonLabel = new Label("Book");
                bookButtonLabel.setStyle(WHITE + "-fx-font-size: 16;");

                var reservation = new Button();
                reservation.setStyle(TRANSPARENT);
                reservation.setGraphic(new ImageView(
                                new Image(getClass().getClassLoader().getResourceAsStream("booking.png"), 60,
                                                60, false,
                                                true)));
                reservation.setOnAction(event -> {
                        if (restaurant.getTimeList().isEmpty() || restaurant.getTimeList() == null) {
                                invalidArgumentAlert();
                        } else {
                                clientApplication.showReservationScene(restaurant, type, restaurants);
                        }
                });

                var backButtonBox = new VBox(back, backButtonLabel);
                backButtonBox.setAlignment(Pos.CENTER);
                backButtonBox.setSpacing(5);
                var bookButtonBox = new VBox(reservation, bookButtonLabel);
                bookButtonBox.setAlignment(Pos.CENTER);
                bookButtonBox.setSpacing(5);
                var buttonsHbox = new HBox(backButtonBox, bookButtonBox);
                buttonsHbox.setAlignment(Pos.CENTER);
                buttonsHbox.setSpacing(boxWidth / 5);

                return new VBox(nameLabel, ratingLabel, addressLabel, webBox, phoneLabel, buttonsHbox);
        }

        /**
         * Creates simple VBox with working time of the restaurant.
         *
         * @return VBox with textFields.
         */
        private VBox createTimeBox() {
                var name = textFieldConfigurator("\uD83D\uDD52 Working Time");
                name.setStyle(LABELSTYLE + "-fx-border-color: white;" +
                                "-fx-border-width: 0 0 2 0;" + "-fx-underline: true;");
                name.setAlignment(Pos.CENTER);
                VBox vbox = new VBox(name);
                var timeList = restaurant.getTimeList();
                if (timeList == null || timeList.isEmpty()) {
                        var empty = textFieldConfigurator("Time schedule was not provided.");
                        empty.setAlignment(Pos.CENTER);
                        vbox.getChildren().add(empty);
                } else {
                        for (String str : timeList) {
                                var temp = textFieldConfigurator(str);
                                temp.setAlignment(Pos.CENTER);
                                vbox.getChildren().add(temp);
                        }
                }
                vbox.setStyle(LABELSTYLE);
                return vbox;
        }

        /**
         * Creates textfield with given string and configures it.
         *
         * @param str - Text to display.
         * @return TextField with custom text and transparent background.
         */
        private TextField textFieldConfigurator(String str) {
                TextField temp = new TextField();
                temp.setText(str);
                temp.setAlignment(Pos.CENTER_LEFT);
                temp.setEditable(false);
                temp.setStyle(TRANSPARENT
                                + "-fx-background-insets: 0;"
                                + "-fx-background-radius: 0;"
                                + "-fx-padding: 0;"
                                + "-fx-font-size: 20;"
                                + WHITE);
                return temp;
        }

        /**
         * Creates box which dynamically change the size of pictures pinned to it.
         *
         * @param img - Image to display.
         * @return Box with image as dynamic background.
         */
        private HBox createImageBox(Image img) {
                HBox temp = new HBox();
                temp.setPrefSize(boxWidth / 3, boxHeight / 3);
                temp.setStyle(BOXSTYLE);
                temp.setBackground(new Background(new BackgroundImage(
                                img,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                                new BackgroundSize(0, 0, false, false, true, false))));
                return temp;
        }

        /**
         * Creates vBox with HBoxes containing reviews.
         *
         * @return VBox with reviews.
         */
        private VBox createCommentsBox() {
                var temp = new VBox();
                var revs = restaurant.getReviews();
                if (revs == null || revs.isEmpty()) {
                        var empty = new Label("Reviews were not provided.");
                        empty.setStyle("-fx-font-size: 35;"
                                        + FONT_FAMILY
                                        + WHITE
                                        + TRANSPARENT);
                        empty.setAlignment(Pos.CENTER);
                        temp.getChildren().add(empty);
                } else {
                        for (Review rev : restaurant.getReviews()) {
                                temp.getChildren().add(createReviewBox(rev));
                        }
                }
                temp.setAlignment(Pos.CENTER);
                temp.setSpacing(10);
                temp.setStyle(SPECIAL);
                return temp;
        }

        /**
         * Creates HBoxes containing review information.
         *
         * @param review - Review object to get information from.
         * @return Styled HBox with one review.
         */
        private HBox createReviewBox(Review review) {
                var userName = new Label(review.getName());
                userName.setAlignment(Pos.CENTER);
                userName.setStyle(LABELSTYLE);

                var userRating = new Label(String.valueOf(review.getRank()));
                userRating.setAlignment(Pos.CENTER);
                userRating.setStyle("-fx-font-size: 25;"
                                + FONT_FAMILY
                                + WHITE
                                + "-fx-font-weight: bolder;"
                                + TRANSPARENT);

                var userRateBox = new HBox(userRating, new ImageView(
                                new Image(getClass().getClassLoader().getResourceAsStream("star.png"), 60,
                                                60, false, true)));
                userRateBox.setSpacing(5);
                userRateBox.setAlignment(Pos.CENTER);

                var userReview = new TextArea(review.getText());
                userReview.getStylesheets()
                                .add(getClass().getClassLoader().getResource("detailsTextArea.css").toExternalForm());
                userReview.setWrapText(true);
                userReview.setEditable(false);
                userReview.setMinWidth(boxWidth - 300);
                userReview.setMaxWidth(boxWidth);

                var userBox = new VBox(new ImageView(
                                new Image(getClass().getClassLoader().getResourceAsStream("avatar.png"), 90, 90, false,
                                                true)),
                                userName,
                                userRateBox);
                userBox.setAlignment(Pos.CENTER);
                userBox.setStyle(BOXSTYLE);
                userBox.setPrefSize(300, boxHeight / 3);
                var reviewBox = new HBox(userBox, userReview);
                reviewBox.setStyle(BOXSTYLE + SPECIAL);
                return reviewBox;
        }

        /**
         * Shows user alert that reservations are not available.
         */

        private void invalidArgumentAlert() {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Something went wrong!");
                alert.setHeaderText("We are sorry!");
                alert.setContentText("Reservations are not available for this restaurant!");
                alert.showAndWait();
        }
}
