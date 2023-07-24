package client.utility;

public final class LoginSceneConstants {
    private LoginSceneConstants() {
    }

    public static final double BOX_SPACING = 15;
    public static final double FIELD_WIDTH = 250;
    public static final double BUTTON_WIDTH = 100;
    public static final double BUTTON_HEIGHT = 50;
    public static final double BACKGROUND_WIDTH = 971;
    public static final double BACKGROUND_HEIGHT = 600;
    public static final String BUTTON_STYLE = "-fx-background-color: #202324;"
            + "-fx-border-color: rgb(4,217,255);"
            + "-fx-padding: 10;"
            + "-fx-text-fill: white;"
            + "-fx-border-width: 2;"
            + "-fx-background-radius: 20;"
            + "-fx-border-radius: 20;"
            + "-fx-font-size: 15;";
    public static final String FIELD_STYLE = BUTTON_STYLE
            + "-fx-prompt-text-fill: white;";
    public static final String REGEX_FOR_NAMES =
            "^[a-zA-ZàáâäãåąćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$";
}
