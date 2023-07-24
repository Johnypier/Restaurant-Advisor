package client.view;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import client.app.ClientApplication;
import client.controller.RestaurantClientController;
import client.controller.ReviewClientController;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import restaurants.information.Restaurant;
import restaurants.information.Review;

import static client.utility.DetailsSceneConstants.*;

public class DetailsScene extends Scene {
    private Restaurant restaurant;
    // Configuration constants which require JavaFX thread.
    private static final Rectangle2D SCREEN = Screen.getPrimary().getBounds();
    private static final double SCENE_HEIGHT = SCREEN.getMaxY() - SCREEN.getMaxY() * 0.2 + 150;
    private static final double SCENE_WIDTH = SCREEN.getMaxX() - SCREEN.getMaxX() * 0.2 + 150;
    private static final double ELEMENTS_HEIGHT = SCREEN.getMaxY() - SCREEN.getMaxY() * 0.2;
    private static final double ELEMENTS_WIDTH = SCREEN.getMaxX() - SCREEN.getMaxX() * 0.2;

    /**
     * DetailsScene constructor, creates a scene with the detailed restaurant information.
     *
     * @param clientApplication          Instance of the user application to interact with it.
     * @param restaurantClientController Instance of the restaurant controller to interact with it.
     * @param restaurants                The list of the restaurants to display in the table, so it is cached and reused.
     * @param reviewClientController     Instance of review controller to interact with it.
     * @param url                        The URL to the website with the restaurant.
     * @param type                       The type of restaurant.
     * @param cachedRestaurant           Cached restaurant loaded in previous requests.
     */
    public DetailsScene(ClientApplication clientApplication, RestaurantClientController restaurantClientController,
                        ObservableList<Restaurant> restaurants, ReviewClientController reviewClientController,
                        String url, String type, Restaurant cachedRestaurant) {
        super(new VBox(), SCENE_WIDTH, SCENE_HEIGHT);

        // Load restaurant images from the server.
        Consumer<Restaurant> restaurantConsumer = response -> this.restaurant = response;
        Consumer<List<Review>> reviewsConsumer = response -> this.restaurant.setReviews(response);
        if (cachedRestaurant == null) {
            restaurantClientController.getRestaurant(restaurantConsumer, url);
            while (restaurant == null) {
                // Waiting for the data from the server...
                // Thread should be busy while waiting, therefore printing nothing.
                System.out.print("");
            }
        } else {
            this.restaurant = cachedRestaurant;
        }

        // Setup images, if none found then use placeholder images.
        Image firstImage;
        Image secondImage;
        Image thirdImage;
        if (restaurant.getImages() != null && !restaurant.getImages().isEmpty()) {
            firstImage = new Image(restaurant.getImages().get(0));
            secondImage = new Image(restaurant.getImages().get(1));
            thirdImage = new Image(restaurant.getImages().get(2));
        } else {
            String placeholder = "placeHolderImage.png";
            firstImage = new Image(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(placeholder)));
            secondImage = new Image(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(placeholder)));
            thirdImage = new Image(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(placeholder)));
        }

        // Load reviews from server.
        if (cachedRestaurant == null) {
            reviewClientController.getAllReviews(reviewsConsumer, url);
            long limit = System.currentTimeMillis() + 3000;
            while (restaurant.getReviews() == null || restaurant.getReviews().isEmpty()) {
                // Waiting for the data from the server...
                // Thread should be busy while waiting, therefore printing nothing.
                System.out.print("");
                // Server response takes too much time, therefore stop.
                if (System.currentTimeMillis() == limit) {
                    break;
                }
            }
        }

        // WebView of Google Maps showing the restaurant location.
        // Actually works as "mini" browser.
        var webView = new WebView();
        webView.setPrefSize(ELEMENTS_WIDTH / 3, ELEMENTS_HEIGHT / 3);
        webView.setStyle(BOX_STYLE);
        var webEngine = webView.getEngine();
        // TODO: Replace with actual restaurant name, currently using example data.
        webEngine.load("https://www.google.com/maps/search/Ratskeller+Munich+Germany/@48.1549107,11.5418357,11z");

        // Contains all restaurant's reviews.
        var commentPane = new ScrollPane(createCommentsBox());
        commentPane.fitToHeightProperty().set(true);
        commentPane.fitToWidthProperty().set(true);
        commentPane.setMaxHeight(ELEMENTS_HEIGHT / 3);
        commentPane.setMaxWidth(ELEMENTS_WIDTH);
        commentPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getClassLoader().getResource("detailsScrollPane.css"))
                       .toExternalForm());

        // Contains general restaurant's information.
        var informationBox = createInformationBox(clientApplication, restaurants, type);
        informationBox.setPrefSize(ELEMENTS_WIDTH / 3, ELEMENTS_HEIGHT / 3);
        informationBox.setStyle(BOX_STYLE);
        informationBox.setSpacing(LARGE_SPACING);

        // Contains opening hours of the restaurant.
        var timeBox = new VBox(createTimeBox());
        timeBox.setPrefSize(ELEMENTS_WIDTH / 3, ELEMENTS_HEIGHT / 3);
        timeBox.setSpacing(SMALL_SPACING);
        timeBox.setStyle(BOX_STYLE);

        // Elements containers.
        var imagesBox = new HBox(createImageBox(firstImage), createImageBox(secondImage), createImageBox(thirdImage));
        imagesBox.setAlignment(Pos.CENTER);
        imagesBox.setSpacing(SMALL_SPACING);
        var middleBox = new HBox(informationBox, webView, timeBox);
        middleBox.setAlignment(Pos.CENTER);
        middleBox.setSpacing(SMALL_SPACING);
        var mainBox = new VBox(imagesBox, middleBox, commentPane);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(MEDIUM_SPACING);
        mainBox.setStyle(SPECIAL_BACKGROUND);
        setRoot(mainBox);
    }

    /**
     * Creates a simple VBox with general restaurant information.
     *
     * @param clientApplication Instance of the user application to interact with it.
     * @param restaurants       The list of the restaurants to display in the table, so it is cached and reused.
     * @param type              The type of restaurant.
     * @return VBox with general restaurant information.
     */
    private VBox createInformationBox(ClientApplication clientApplication, ObservableList<Restaurant> restaurants,
                                      String type) {
        var nameLabel = textFieldConfigurator("\uD83C\uDFE2 Name: " + restaurant.getName());
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle(RESTAURANT_NAME_LABEL_STYLE);

        var ratingBuilder = new StringBuilder();
        ratingBuilder.append("Rating: ").append(Double.valueOf(restaurant.getRank()).intValue());
        var rate = Double.valueOf(restaurant.getRank()).intValue();
        ratingBuilder.append(" \uD83C\uDF1F".repeat(Math.max(0, rate)));
        var ratingLabel = new Label(ratingBuilder.toString());
        ratingLabel.setAlignment(Pos.CENTER_LEFT);
        ratingLabel.setStyle(LABEL_STYLE);

        var addressLabel = new Label("\uD83D\uDCCD Address: " + restaurant.getAddress());
        addressLabel.setAlignment(Pos.CENTER_LEFT);
        addressLabel.setStyle(LABEL_STYLE);

        var websiteLabel = new Label("\uD83C\uDF10 Website: ");
        websiteLabel.setAlignment(Pos.CENTER_LEFT);
        websiteLabel.setStyle(LABEL_STYLE);
        var link = new Hyperlink("click me");
        link.setAlignment(Pos.CENTER_LEFT);
        link.setStyle(HYPER_LINK_STYLE);
        link.setOnAction(event -> clientApplication.getHostServices().showDocument(restaurant.getWebsite()));
        var webBox = new HBox(websiteLabel, link);
        webBox.setSpacing(10);
        webBox.setStyle(TRANSPARENT_BACKGROUND);
        webBox.setAlignment(Pos.CENTER_LEFT);

        var phoneLabel = new Label((restaurant.getPhone() == null || restaurant.getPhone().isEmpty())
                                   ? "\uD83D\uDCDE Phone: Phone was not provided."
                                   : "\uD83D\uDCDE Phone: " + restaurant.getPhone());
        phoneLabel.setAlignment(Pos.CENTER_LEFT);
        phoneLabel.setStyle(LABEL_STYLE);

        var back = new Button();
        back.setOnAction(action -> clientApplication.showRestaurantsScene(type, restaurants));
        back.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("left.png")),
                          BACK_BUTTON_SIZE, BACK_BUTTON_SIZE, false, true)));
        back.setStyle(TRANSPARENT_BACKGROUND);

        var backButtonBox = new VBox(back);
        backButtonBox.setAlignment(Pos.CENTER_LEFT);
        backButtonBox.setSpacing(SMALL_SPACING);

        return new VBox(nameLabel, ratingLabel, addressLabel, webBox, phoneLabel, backButtonBox);
    }

    /**
     * Creates a simple VBox with opening hours of the restaurant.
     *
     * @return VBox with the text fields.
     */
    private VBox createTimeBox() {
        var headerField = textFieldConfigurator("\uD83D\uDD52 Opening Hours");
        headerField.setStyle(RESTAURANT_NAME_LABEL_STYLE);
        VBox tempBox = new VBox(headerField);
        var timeList = restaurant.getTimeList();
        if (timeList == null || timeList.isEmpty()) {
            var empty = textFieldConfigurator("Opening hours were not provided.");
            tempBox.getChildren().add(empty);
        } else {
            for (String str : timeList) {
                var temp = textFieldConfigurator(str);
                tempBox.getChildren().add(temp);
            }
        }
        tempBox.setStyle(LABEL_STYLE);
        return tempBox;
    }

    /**
     * Creates a text field with the given string and applies predefined configuration.
     *
     * @param str The text to display.
     * @return TextField with the custom text and style.
     */
    private TextField textFieldConfigurator(String str) {
        TextField temp = new TextField();
        temp.setText(str);
        temp.setAlignment(Pos.CENTER);
        temp.setEditable(false);
        temp.setStyle(TEXT_FIELD_STYLE);
        return temp;
    }

    /**
     * Creates a box with restaurant's photo as a background.
     * It will fit the box dimensions.
     *
     * @param img Image to display.
     * @return Box with an image on the background.
     */
    private HBox createImageBox(Image img) {
        HBox temp = new HBox();
        temp.setPrefSize(ELEMENTS_WIDTH / 3, ELEMENTS_HEIGHT / 3);
        temp.setStyle(BOX_STYLE);
        temp.setBackground(new Background(new BackgroundImage(
                img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(0, 0, false, false, true, false))));
        return temp;
    }

    /**
     * Creates a vBox with HBoxes containing reviews.
     *
     * @return VBox with reviews.
     */
    private VBox createCommentsBox() {
        var temp = new VBox();
        if (restaurant.getReviews() == null || restaurant.getReviews().isEmpty()) {
            var empty = new Label("Reviews were not provided.");
            empty.setStyle(EMPTY_COMMENTS_LABEL_STYLE);
            empty.setAlignment(Pos.CENTER);
            temp.getChildren().add(empty);
        } else {
            for (Review review : restaurant.getReviews()) {
                temp.getChildren().add(createReviewBox(review));
            }
        }
        temp.setAlignment(Pos.CENTER);
        temp.setSpacing(MEDIUM_SPACING);
        temp.setStyle(SPECIAL_BACKGROUND);
        return temp;
    }

    /**
     * Creates HBox containing the review information.
     *
     * @param review Review object to get the information from.
     * @return Styled HBox with one review.
     */
    private HBox createReviewBox(Review review) {
        var userNameLabel = new Label(review.getName());
        userNameLabel.setAlignment(Pos.CENTER);
        userNameLabel.setStyle(LABEL_STYLE);

        var userRatingLabel = new Label(String.valueOf(review.getRank()));
        userRatingLabel.setAlignment(Pos.CENTER);
        userRatingLabel.setStyle(RATING_LABEL_STYLE);

        var userRateBox = new HBox(userRatingLabel, new ImageView(
                new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("star.png")),
                          BACK_BUTTON_SIZE, BACK_BUTTON_SIZE, false, true)));
        userRateBox.setSpacing(SMALL_SPACING);
        userRateBox.setAlignment(Pos.CENTER);

        var userReviewTextArea = new TextArea(review.getText());
        userReviewTextArea.getStylesheets().add(
                Objects.requireNonNull(getClass().getClassLoader().getResource("detailsTextArea.css"))
                       .toExternalForm());
        userReviewTextArea.setWrapText(true);
        userReviewTextArea.setEditable(false);
        userReviewTextArea.setMinWidth(ELEMENTS_WIDTH - 300);
        userReviewTextArea.setMaxWidth(ELEMENTS_WIDTH);

        var userBox = new VBox(new ImageView(
                new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("avatar.png")),
                          USER_AVATAR_PLACEHOLDER_SIZE, USER_AVATAR_PLACEHOLDER_SIZE, false, true)), userNameLabel,
                               userRateBox);
        userBox.setAlignment(Pos.CENTER);
        userBox.setStyle(BOX_STYLE);
        userBox.setPrefSize(USER_REVIEW_INFO_BOX_WIDTH, ELEMENTS_HEIGHT / 3);
        var reviewBox = new HBox(userBox, userReviewTextArea);
        reviewBox.setStyle(BOX_STYLE + SPECIAL_BACKGROUND);
        return reviewBox;
    }
}
