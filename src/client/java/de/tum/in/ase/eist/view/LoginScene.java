package de.tum.in.ase.eist.view;

import de.tum.in.ase.eist.ClientApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;

public class LoginScene extends Scene {
    private static final String FIELDSTYLE = "-fx-background-color: #202324;" + "-fx-prompt-text-fill: white;"
            + "-fx-border-color: rgb(4,217,255);" + "-fx-padding: 10;" + "-fx-text-fill: white;"
            + "-fx-border-width: 2;" + "-fx-background-radius: 20;" + "-fx-border-radius: 20;"
            + "-fx-font-size: 15;";
    private static final String BUTTONSTYLE = "-fx-background-color: #202324;"
            + "-fx-border-color: rgb(4,217,255);" + "-fx-padding: 10;"
            + "-fx-text-fill: white;" + "-fx-border-width: 2;" + "-fx-background-radius: 20;"
            + "-fx-border-radius: 20;" + "-fx-font-size: 15;";
    private static final String REGEX_NAME = "^[a-zA-ZàáâäãåąćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$";

    /**
     * LoginScene constructor, creates simple scene with 2 text fields and login
     * button.
     *
     * @param clientApplication - Takes instance of user application to interact
     *                          with it.
     */
    public LoginScene(ClientApplication clientApplication) {
        super(new VBox(), 971, 600);

        var firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        firstNameField.setMaxWidth(250);
        var lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        lastNameField.setMaxWidth(250);
        firstNameField.setStyle(FIELDSTYLE);
        lastNameField.setStyle(FIELDSTYLE);

        var loginButton = new Button("Login");
        loginButton.setPrefSize(100, 50);
        loginButton.setStyle(BUTTONSTYLE);
        loginButton.setOnAction(action -> {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                emptyAlert();
            } else {
                if (firstNameField.getText().matches(REGEX_NAME)
                        && lastNameField.getText().matches(REGEX_NAME)) {
                    clientApplication.setFirstName(firstNameField.getText());
                    clientApplication.setLastName(lastNameField.getText());
                    clientApplication.showSelectionScene();
                } else {
                    invalidNameAlert();
                }
            }
        });

        var vBox = new VBox(firstNameField, lastNameField, loginButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(40, 5, 5, 5));
        vBox.setSpacing(15.0);
        vBox.setBackground(
                new Background(
                        new BackgroundImage(
                                new Image("LoginBackground.png"),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(971, 600, false, false, false, false))));
        setRoot(vBox);
    }

    /**
     * Shows user alert that one of the required fields is empty.
     */
    private void emptyAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Empty Field");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all fields!");
        alert.showAndWait();
    }

    /**
     * Shows user alert that one of the required fields is empty.
     */

    private void invalidNameAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Invalid Name");
        alert.setHeaderText(null);
        alert.setContentText("Please enter valid name!");
        alert.showAndWait();
    }

}
