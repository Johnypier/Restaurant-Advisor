package client.utility;

public final class DetailsSceneConstants {
    private DetailsSceneConstants() {
    }

    public static final double SMALL_SPACING = 5;
    public static final double MEDIUM_SPACING = 10;
    public static final double LARGE_SPACING = 15;
    public static final double BACK_BUTTON_SIZE = 60;
    public static final double USER_AVATAR_PLACEHOLDER_SIZE = 90;
    public static final double USER_REVIEW_INFO_BOX_WIDTH = 300;
    public static final String FONT_FAMILY = "-fx-font-family: 'Yu Gothic';";
    public static final String WHITE_TEXT = "-fx-text-fill: white;";
    public static final String TRANSPARENT_BACKGROUND = "-fx-background-color: transparent;";
    public static final String SPECIAL_BACKGROUND = "-fx-background-color: #202324;";
    public static final String BOX_STYLE = "-fx-border-color: rgba(4,217,255,0.5);"
            + "-fx-padding: 10,0,0,0;"
            + "-fx-border-width: 3;";
    public static final String LABEL_STYLE = "-fx-font-size: 20;"
            + FONT_FAMILY
            + WHITE_TEXT
            + "-fx-font-weight: bolder;"
            + TRANSPARENT_BACKGROUND;
    public static final String RESTAURANT_NAME_LABEL_STYLE = LABEL_STYLE +
            "-fx-border-color: white;"
            + "-fx-border-width: 0 0 2 0;"
            + "-fx-underline: true;";
    public static final String HYPER_LINK_STYLE = "-fx-font-size: 25;";
    public static final String FONT_SIZE_16 = "-fx-font-size: 16;";
    public static final String TEXT_FIELD_STYLE = TRANSPARENT_BACKGROUND
            + "-fx-background-insets: 0;"
            + "-fx-background-radius: 0;"
            + "-fx-padding: 0;"
            + "-fx-font-size: 20;"
            + WHITE_TEXT;
    public static final String EMPTY_COMMENTS_LABEL_STYLE = "-fx-font-size: 35;"
            + FONT_FAMILY
            + WHITE_TEXT
            + TRANSPARENT_BACKGROUND;
    public static final String RATING_LABEL_STYLE = HYPER_LINK_STYLE
            + FONT_FAMILY
            + WHITE_TEXT
            + "-fx-font-weight: bolder;"
            + TRANSPARENT_BACKGROUND;
}
