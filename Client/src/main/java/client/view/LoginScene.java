package client.view;

import client.app.ClientApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;

import static client.utility.LoginSceneConstants.*;

public class LoginScene extends Scene {
    /**
     * LoginScene constructor, creates a simple scene with two text fields and login
     * button.
     *
     * @param clientApplication - Instance of the user application to interact
     *                          with it.
     */
    public LoginScene(ClientApplication clientApplication) {
        super(new VBox(), BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        var firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        firstNameField.setMaxWidth(FIELD_WIDTH);
        var lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        lastNameField.setMaxWidth(FIELD_WIDTH);
        firstNameField.setStyle(FIELD_STYLE);
        lastNameField.setStyle(FIELD_STYLE);

        var loginButton = new Button("Login");
        loginButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        loginButton.setStyle(BUTTON_STYLE);
        loginButton.setOnAction(action -> {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                customInformationAlert("Empty Field", "Please fill all fields!");
            } else {
                if (firstNameField.getText().matches(REGEX_FOR_NAMES) && lastNameField.getText()
                                                                                      .matches(REGEX_FOR_NAMES)) {
                    clientApplication.setFirstName(firstNameField.getText());
                    clientApplication.setLastName(lastNameField.getText());
                    clientApplication.showSelectionScene();
                } else {
                    customInformationAlert("Invalid Name", "Please enter valid name!");
                }
            }
        });

        var mainBox = new VBox(firstNameField, lastNameField, loginButton);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(BOX_SPACING);
        mainBox.setPadding(new Insets(40, 5, 5, 5));
        mainBox.setBackground(new Background(new BackgroundImage(
                new Image("LoginBackground.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, false, false, false, false))));
        setRoot(mainBox);
    }

    /**
     * Shows the user an information alert with the custom title and content.
     * Header will be removed.
     *
     * @param title   Title of the alert
     * @param content Content of the alert body
     */
    private void customInformationAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
