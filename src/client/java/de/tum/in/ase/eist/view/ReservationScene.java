package de.tum.in.ase.eist.view;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tum.in.ase.eist.ClientApplication;
import de.tum.in.ase.eist.Reservation;
import de.tum.in.ase.eist.Restaurant;
import de.tum.in.ase.eist.controller.ReservationController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class ReservationScene extends Scene {
    private static Rectangle2D screen = Screen.getPrimary().getBounds();
    private static double boxHeight = screen.getMaxY() - screen.getMaxY() * 0.2;
    private static double boxWidth = screen.getMaxX() - screen.getMaxX() * 0.2;
    private TableView<String> table;
    private final String name;
    private final String type;
    private String temporalDate = "";
    private String temporalTime = "";
    private final Restaurant restaurant;
    private List<Reservation> reservations;
    private ClientApplication clientApplication;
    private ReservationController reservationController;
    private LocalDate pickedDate = LocalDate.now();
    private ObservableList<String> timeList;
    private DatePicker datePicker;
    private static final String CLOSED = "closed";
    private static final String DATE = "Date: ";
    private static final String TIME = "Time: ";
    private static final String BLACK = "-fx-background-color: #202324;";
    private static final String CONFIRMATION = "Status: Confirmed.";
    private static final String BOXSTYLE = "-fx-border-color: rgba(4,217,255,0.5);"
            + "-fx-padding: 10,0,0,0;"
            + "-fx-border-width: 3;"
            + BLACK;
    private static final String FONT_FAMILY = "-fx-font-family: 'Yu Gothic';";
    private static final String WHITE = "-fx-text-fill: white;";
    private static final String TRANSPARENT = "-fx-background-color: transparent;";
    private static final String LABELSTYLE = "-fx-font-size: 20;"
            + FONT_FAMILY
            + WHITE
            + "-fx-font-weight: bolder;"
            + TRANSPARENT;
    private static final String FIELDSTYLE = BLACK
            + "-fx-prompt-text-fill: white;"
            + "-fx-border-color: rgb(4,217,255);"
            + "-fx-padding: 10;"
            + WHITE
            + "-fx-border-width: 2;"
            + "-fx-background-radius: 20;"
            + "-fx-border-radius: 20;"
            + "-fx-font-size: 15;";
    private static final String BUTTONSTYLE = BLACK
            + "-fx-border-color: rgb(4,217,255);"
            + "-fx-padding: 10;"
            + WHITE
            + "-fx-border-width: 2;"
            + "-fx-background-radius: 20;"
            + "-fx-border-radius: 20;"
            + "-fx-font-size: 20;";
    private TextField personsField;
    private Button save;
    private Button cancel;
    private Button create;

    /**
     * 
     * ReservationScene constructor, creates scene with navigation panel for
     * modifying/creating reservations and displaying them on the right side.
     * 
     * @param clientApplication        - Takes instance of user application to
     *                                 interact with it.
     * @param restaurant               - Takes restaurant object from DetailsScene,
     *                                 so it won't be loaded again.
     * @param type                     - Specifies the type of restaurant.
     * @param restaurants              - Takes restaurants list from DetailsScene,
     *                                 required to display it again.
     * @param reservationControllerVar - Takes instance of reservationController to
     *                                 interact with it.
     */
    public ReservationScene(ClientApplication clientApplication, Restaurant restaurant, String type,
            ObservableList<Restaurant> restaurants, ReservationController reservationControllerVar) {
        super(new VBox(), boxWidth + 150, boxHeight + 150);
        this.timeList = FXCollections.observableArrayList();
        this.reservations = new ArrayList<>();
        this.table = new TableView<>(timeList);
        this.restaurant = restaurant;
        this.clientApplication = clientApplication;
        this.type = type;
        this.name = this.clientApplication.getFirstName() + " " + this.clientApplication.getLastName();
        this.reservationController = reservationControllerVar;
        // Initialization of reservations list.
        refreshList(type, restaurant.getName());

        VBox.setVgrow(table, Priority.ALWAYS);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String, String> timeColumn = new TableColumn<>("Available time slots");
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        timeColumn.setSortable(false);
        timeColumn.setPrefWidth(150);

        table.getStylesheets().add(getClass().getClassLoader().getResource("restaurantTable.css").toExternalForm());
        table.getColumns().add(timeColumn);

        var workingTime = restaurant.getTimeList();
        this.datePicker = new DatePicker();
        datePicker.getStylesheets().add(getClass().getClassLoader().getResource("datePicker.css").toExternalForm());
        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            pickedDate = newValue;
            switch (pickedDate.getDayOfWeek().toString()) {
                case ("MONDAY"):
                    createTimePeriods(workingTime.get(0));
                    break;
                case ("TUESDAY"):
                    createTimePeriods(workingTime.get(1));
                    break;
                case ("WEDNESDAY"):
                    createTimePeriods(workingTime.get(2));
                    break;
                case ("THURSDAY"):
                    createTimePeriods(workingTime.get(3));
                    break;
                case ("FRIDAY"):
                    createTimePeriods(workingTime.get(4));
                    break;
                case ("SATURDAY"):
                    createTimePeriods(workingTime.get(5));
                    break;
                case ("SUNDAY"):
                    createTimePeriods(workingTime.get(6));
                    break;
                default:
                    break;
            }
        });

        var back = new Button("Return to previous window");
        back.setStyle(BUTTONSTYLE);

        create = new Button("Create");
        create.setStyle(BUTTONSTYLE);

        cancel = new Button("Cancel");
        cancel.setStyle(BUTTONSTYLE);
        cancel.setDisable(true);

        save = new Button("Save");
        save.setStyle(BUTTONSTYLE);
        save.setDisable(true);

        var personsLabe = new Label("Enter amount of persons:");
        personsLabe.setStyle(LABELSTYLE);

        var dateLabel = new Label("Select the date:");
        dateLabel.setStyle(LABELSTYLE);

        var timeLabel = new Label("Select the time:");
        timeLabel.setStyle(LABELSTYLE);

        personsField = new TextField();
        personsField.setAlignment(Pos.CENTER);
        personsField.setStyle(FIELDSTYLE);
        personsField.setPromptText("Min: 1 - Max: 10");

        var navigationBox = new VBox(back, dateLabel, datePicker, personsLabe, personsField, timeLabel, table, create,
                cancel, save);
        navigationBox.setStyle(BOXSTYLE);
        navigationBox.setAlignment(Pos.CENTER);
        navigationBox.setSpacing(15);
        navigationBox.setPrefSize(boxWidth / 5, boxHeight);

        var reservationBox = new VBox();
        reservationBox.setSpacing(10);
        reservationBox.setStyle(BOXSTYLE);
        reservationBox.setPrefSize(boxWidth - boxWidth / 6 - 50, boxHeight - 50);

        var reservationPane = new ScrollPane(reservationBox);
        reservationPane.fitToHeightProperty().set(true);
        reservationPane.fitToWidthProperty().set(true);
        reservationPane.setMaxSize(boxWidth - boxWidth / 6, boxHeight);
        reservationPane.setMinSize(boxWidth - boxWidth / 6, boxHeight);
        reservationPane.getStylesheets()
                .add(getClass().getClassLoader().getResource("detailsScrollPane.css").toExternalForm());

        back.setOnAction(action -> clientApplication.showDetailsScene(null, restaurants, type, restaurant));

        for (Reservation res : reservations) {
            if (res.getDate() == null) {
                continue;
            }
            Platform.runLater(() -> createReservationBox(reservationBox, "", reservationPane, res));
        }

        create.setOnAction(action -> {
            if (table.getSelectionModel().getSelectedItem().contains(CLOSED)
                    || !checkNumberOfPeople(personsField.getText()) || pickedDate.isBefore(LocalDate.now())) {
                invalidArgumentAlert();
            } else {
                createReservationBox(reservationBox, personsField.getText(), reservationPane, null);
                reservationPane.setContent(reservationBox);
            }
        });

        cancel.setOnAction(action -> {
            create.setDisable(false);
            cancel.setDisable(true);
            save.setDisable(true);
        });

        save.setOnAction(action -> {
            if (table.getSelectionModel().getSelectedItem().contains(CLOSED)
                    || !checkNumberOfPeople(personsField.getText())) {
                invalidArgumentAlert();
            } else {
                var oldReservation = getReservation(temporalDate, temporalTime);
                var newDate = pickedDate.getDayOfMonth() + "." + pickedDate.getMonthValue() + "."
                        + pickedDate.getYear();
                var newTime = table.getSelectionModel().getSelectedItem();
                var newAmountOfPeople = personsField.getText();

                reservationController.updateReservation(oldReservation.getReservationId(), type, name,
                        restaurant.getName(),
                        newAmountOfPeople, newDate, newTime, oldReservation.getConfirmation());

                refreshList(type, restaurant.getName());
                var newReservation = getReservation(newDate, newTime);

                updateReservationLocally(reservationBox, newDate, newTime, newAmountOfPeople,
                        newReservation.getConfirmation());
                reservationPane.setContent(reservationBox);
                cancel.setDisable(true);
                save.setDisable(true);
                create.setDisable(false);
            }
        });

        var mainBox = new HBox(navigationBox, reservationPane);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle(BLACK);
        mainBox.setSpacing(10);
        setRoot(mainBox);
    }

    /**
     * 
     * Creates reservation VBox and adds it to existing or new HBox.
     * 
     * @param reservationBox - Link to main reservationBox.
     * @param persons        - Amount of people of reservation.
     * @param scrollPane     - Link to scrollPane which contains reservationBox.
     */
    private void createReservationBox(VBox reservationBox, String person, ScrollPane scrollPane,
            Reservation reservation) {
        var dateString = "";
        var timeString = "";
        var confirmationStatus = "";
        var persons = person;
        if (reservation == null) {
            dateString = pickedDate.getDayOfMonth() + "." + pickedDate.getMonthValue() + "." + pickedDate.getYear();
            timeString = table.getSelectionModel().getSelectedItem();
            confirmationStatus = "false";
        } else {
            dateString = reservation.getDate();
            timeString = reservation.getTime();
            confirmationStatus = reservation.getConfirmation();
            persons = reservation.getNumberOfPeople();
        }
        var stringList = List.of(dateString, timeString, confirmationStatus);

        if (!checkCreation(dateString, timeString, reservation)) {
            invalidArgumentAlert();
            return;
        }

        var nameLabel = new Label("Name: " + name);
        var dateLabel = new Label(DATE + dateString);
        var timeLabel = new Label(TIME + timeString);
        var peopleLabel = new Label("Amount of people: " + persons);
        var confirmationLabel = new Label();
        if (confirmationStatus.equals("false")) {
            confirmationLabel.setText("Status: Not confirmed.");
        } else {
            confirmationLabel.setText(CONFIRMATION);
        }
        var labelList = List.of(nameLabel, dateLabel, timeLabel, peopleLabel, confirmationLabel);

        var confirm = new Button("Confirm");
        if (confirmationStatus.equals("true")) {
            confirm.setDisable(true);
        }
        var change = new Button("Change");
        var delete = new Button("Delete");
        var buttonList = List.of(confirm, change, delete);

        nameLabel.setStyle(LABELSTYLE);
        dateLabel.setStyle(LABELSTYLE);
        timeLabel.setStyle(LABELSTYLE);
        peopleLabel.setStyle(LABELSTYLE);
        confirmationLabel.setStyle(LABELSTYLE);
        confirm.setStyle(BUTTONSTYLE);
        change.setStyle(BUTTONSTYLE);
        delete.setStyle(BUTTONSTYLE);

        var children = reservationBox.getChildren();
        boolean skipCheck = false;
        if (children.isEmpty()) {
            createReservationBoxHelper(children, persons, labelList, buttonList, stringList, reservation);
            skipCheck = true;
        }
        if (!skipCheck) {
            boolean addedCheck = false;
            for (int i = 0; i < children.size(); i++) {
                var child = ((HBox) children.get(i));
                if (child.getChildren().size() != 4) {
                    var temp = new VBox(nameLabel, dateLabel, timeLabel, peopleLabel, confirmationLabel, confirm,
                            change, delete);
                    temp.setStyle(BOXSTYLE);
                    temp.setSpacing(15);
                    temp.setAlignment(Pos.CENTER);
                    child.getChildren().add(temp);
                    addedCheck = true;
                    if (reservation == null) {
                        reservationController.createReservation(type, name, restaurant.getName(), persons, dateString,
                                timeString,
                                confirmationStatus);
                    }
                    refreshList(type, restaurant.getName());
                    break;
                }
            }
            if (!addedCheck) {
                createReservationBoxHelper(children, persons, labelList, buttonList, stringList, reservation);
            }
        }

        confirm.setOnAction(action -> {
            var res = getReservation(getDataFromLabel(dateLabel.getText()), getDataFromLabel(timeLabel.getText()));
            reservationController.updateReservation(res.getReservationId(), type, name, restaurant.getName(),
                    res.getNumberOfPeople(),
                    res.getDate(), res.getTime(), "true");
            confirmationLabel.setText(CONFIRMATION);
            refreshList(type, restaurant.getName());
            confirm.setDisable(true);
            create.setDisable(false);
            cancel.setDisable(true);
            save.setDisable(true);
            scrollPane.setContent(reservationBox);
        });

        change.setOnAction(action -> {
            if (reservation == null) {
                personsField.setText(person);
            } else {
                personsField.setText(reservation.getNumberOfPeople());
            }
            temporalDate = getDataFromLabel(dateLabel.getText());
            temporalTime = getDataFromLabel(timeLabel.getText());
            create.setDisable(true);
            cancel.setDisable(false);
            save.setDisable(false);
        });

        delete.setOnAction(action -> {
            var res = getReservation(getDataFromLabel(dateLabel.getText()), getDataFromLabel(timeLabel.getText()));
            create.setDisable(false);
            cancel.setDisable(true);
            save.setDisable(true);
            reservationController.deleteReservation(res.getReservationId(), type, name, restaurant.getName());
            deleteChild(reservationBox, dateLabel.getText(), timeLabel.getText());
            refreshList(type, restaurant.getName());
        });
    }

    /**
     * 
     * Updates the reservation box on client side.
     * 
     * @param reservationBox - Link to main reservationBox.
     * @param date           - New date value.
     * @param time           - New time value.
     * @param persons        - New persons value.
     * @param confirmation   - New confirmation value.
     */
    private void updateReservationLocally(VBox reservationBox, String date, String time, String persons,
            String confirmation) {
        var children = reservationBox.getChildren();
        for (int i = 0; i < children.size(); i++) {
            var child = ((HBox) children.get(i));
            for (Node n : child.getChildren()) {
                var nChildren = ((VBox) n).getChildren();
                if (((Label) nChildren.get(1)).getText().equals(DATE + temporalDate)
                        && ((Label) nChildren.get(2)).getText().equals(TIME + temporalTime)) {
                    ((Label) nChildren.get(1)).setText(DATE + date);
                    ((Label) nChildren.get(2)).setText(TIME + time);
                    ((Label) nChildren.get(3)).setText("Amount of people: " + persons);
                    if (confirmation.equals("true")) {
                        ((Label) nChildren.get(4)).setText(CONFIRMATION);
                    } else {
                        System.out.println("I AM IN UPDATE RESERVATION BUT IT IS FALSE");
                        ((Label) nChildren.get(4)).setText("Confirmation status: Not confirmed.");
                    }
                }
            }
        }
    }

    /**
     * 
     * Creates new HBox for reservation VBoxes and creates new reservation box in
     * it.
     * 
     * @param children     - Link to main VBox's children.
     * @param persons      - Amount of people for reservation.
     * @param labelList    - List with labels for box.
     * @param buttonList   - List with buttons for box.
     * @param stringList   - List with string content.
     * @param reservation- Needed to make sure that reservation box is created for
     *                     already existing reservation.
     */
    private void createReservationBoxHelper(ObservableList<Node> children, String persons, List<Label> labelList,
            List<Button> buttonList,
            List<String> stringList, Reservation reservation) {
        var temp = new VBox(labelList.get(0), labelList.get(1), labelList.get(2), labelList.get(3), labelList.get(4),
                buttonList.get(0), buttonList.get(1), buttonList.get(2));
        if (reservation == null) {
            reservationController.createReservation(type, name, restaurant.getName(), persons, stringList.get(0),
                    stringList.get(1),
                    stringList.get(2));
        }
        temp.setStyle(BOXSTYLE);
        temp.setSpacing(15);
        temp.setAlignment(Pos.CENTER);
        var tmp = new HBox(temp);
        tmp.setStyle(BOXSTYLE);
        tmp.setSpacing(15);
        tmp.setAlignment(Pos.CENTER);
        children.add(tmp);
        refreshList(type, restaurant.getName());
    }

    /**
     * 
     * Gets specific String from label text.
     * 
     * @param text - Label text.
     * @return String in correct format.
     */
    private String getDataFromLabel(String text) {
        var stringBuilder = new StringBuilder();
        var charArray = text.toCharArray();
        for (int i = 6; i < charArray.length; i++) {
            stringBuilder.append(charArray[i]);
        }
        return stringBuilder.toString();
    }

    /**
     * 
     * Checks if new reservation box can be created.
     * 
     * @param date        - Date of reservation.
     * @param time        - Time of reservation.
     * @param reservation - Needed to make sure that reservation box is created for
     *                    already existing reservation.
     * @return Boolean value of this check.
     */
    private boolean checkCreation(String date, String time, Reservation reservation) {
        refreshList(type, restaurant.getName());
        if (reservation != null) {
            return true;
        }
        if (table.getSelectionModel() != null && table.getSelectionModel().getSelectedItem().contains(CLOSED)) {
            return false;
        }
        if (reservations.size() == 1 && reservations.get(0).getDate() == null) {
            return true;
        }
        for (Reservation res : reservations) {
            if (res.getReservationId().equals("empty")) {
                continue;
            }
            if (res.getDate().equals(date) && res.getTime().equals(time)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * Deletes the specific reservation box from scroll pane.
     * 
     * @param reservationBox - ReservationBox reference.
     * @param date           - Specific date of the required Node.
     * @param time           - Specific time of the required Node.
     */
    private void deleteChild(VBox reservationBox, String date, String time) {
        var children = reservationBox.getChildren();
        for (int i = 0; i < children.size(); i++) {
            var child = ((HBox) children.get(i));
            for (Node n : child.getChildren()) {
                var nChildren = ((VBox) n).getChildren();
                if (((Label) nChildren.get(1)).getText().equals(date)
                        && ((Label) nChildren.get(2)).getText().equals(time)) {
                    child.getChildren().remove(n);
                    break;
                }
            }
        }
    }

    /**
     * 
     * Retrieves specific reservation.
     * 
     * @param date - Date of required reservation.
     * @param time - Time of required reservation.
     * @return Reservation.
     */
    private Reservation getReservation(String date, String time) {
        refreshList(type, restaurant.getName());
        for (Reservation res : reservations) {
            if (res.getDate() == null) {
                continue;
            }
            if (res.getDate().equals(date) && res.getTime().equals(time)) {
                return res;
            }
        }
        return null;
    }

    /**
     * 
     * Updates the reservations list with actual data.
     * 
     * @param type           - Type of restaurant.
     * @param restaurantName - Name of restaurant.
     */
    private void refreshList(String type, String restaurantName) {
        Consumer<List<Reservation>> consumer = res -> this.reservations = res;
        reservationController.getAllReservations(consumer, type, name, restaurantName);
        long limit = System.currentTimeMillis() + 2000;
        while (true) {
            // Waiting for data...
            if (System.currentTimeMillis() == limit) {
                break;
            }
        }
    }

    /**
     * 
     * Checks if amount of people for reservation is between limit.
     * 
     * @param numberOfPeople - String with amount of people.
     * @return Boolean value of match.
     */
    private boolean checkNumberOfPeople(String numberOfPeople) {
        return numberOfPeople.matches("[1-9]|10");
    }

    /**
     * 
     * Creates time table from given string with 2 different times.
     * 
     * @param time String which contains time periods to be parsed and added to time
     *             table.
     */
    private void createTimePeriods(String time) {
        timeList.clear();
        if (time.contains(CLOSED)) {
            timeList.add("Restaurant is closed during this day.");
            return;
        }
        Pattern pattern = Pattern
                .compile("[A-Za-z]{3}\\ (\\d+:\\d+)\\ ((A|P)M) - (\\d+:\\d+)\\ ((A|P)M)");
        Matcher matcher = pattern.matcher(time);
        String timeRange = "";
        while (matcher.find()) {
            timeRange = matcher.group(1) + " " + matcher.group(2) + " - " + matcher.group(4) + " "
                    + matcher.group(5);
        }
        try {
            var tmp = convertTimeTo24(timeRange);
            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
            Date startDate = dateFormat.parse(tmp.get(0));
            var startTime = new Time(startDate.getTime()).toLocalTime();
            Date endDate = dateFormat.parse(tmp.get(1));
            var endTime = new Time(endDate.getTime()).toLocalTime();
            if (startTime.getHour() > endTime.getHour() || startTime.getMinute() > endTime.getMinute()) {
                while (startTime.getHour() > endTime.getHour() || startTime.getMinute() > endTime.getMinute()) {
                    startTime = startTime.plusMinutes(45);
                    if (endTime.getHour() > startTime.getHour()
                            || endTime.getHour() == startTime.getHour()
                                    && startTime.getMinute() == endTime.getMinute()) {
                        timeList.add(startTime.toString());
                        break;
                    }
                    timeList.add(startTime.toString());
                }
            } else {
                while (startTime.getHour() < endTime.getHour() || startTime.getMinute() < endTime.getMinute()) {
                    startTime = startTime.plusMinutes(45);
                    if (startTime.getHour() > endTime.getHour()
                            || startTime.getHour() == endTime.getHour()
                                    && startTime.getMinute() == endTime.getMinute()) {
                        break;
                    }
                    timeList.add(startTime.toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Converts the time to 24 hours format, e.g. 7:00 PM to 19:00.
     * 
     * @param timeRange String which contains time periods.
     * @return List which contains start and end time of restaurant working period.
     * @throws ParseException
     */
    private static List<String> convertTimeTo24(String timeRange) throws ParseException {
        List<String> temp = new LinkedList<>();
        String[] timeRangeArray = timeRange.split(" - ");
        String[] startTimeArray = timeRangeArray[0].split(" ");
        String[] endTimeArray = timeRangeArray[1].split(" ");
        String startTime = startTimeArray[0] + " " + startTimeArray[1];
        String endTime = endTimeArray[0] + " " + endTimeArray[1];
        DateFormat df = new SimpleDateFormat("hh:mm a");
        DateFormat df24 = new SimpleDateFormat("HH:mm");
        Date startTime24 = df.parse(startTime);
        Date endTime24 = df.parse(endTime);
        String startTime24String = df24.format(startTime24);
        String endTime24String = df24.format(endTime24);
        temp.add(startTime24String);
        temp.add(endTime24String);
        return temp;
    }

    /**
     * Shows user alert that current time is already taken for specific date.
     */
    private void invalidArgumentAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText("We are sorry!");
        alert.setContentText("Invalid data provided.");
        alert.showAndWait();
    }
}
