package de.tum.in.ase.eist;

public class Reservation {
    private String reservationId;
    private String customer;
    private String numberOfPeople;
    private String date;
    private String time;
    private String confirmation;

    public Reservation(){
    }

    public Reservation(String reservationId, String customer, String numberOfPeople, String date, String time,
            String confirmation) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
        this.date = date;
        this.time = time;
        this.confirmation = confirmation;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConfirmation() {
        return this.confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    @Override
    public String toString() {
        return "Reservation{" + " " +
                reservationId + " " +
                customer + " " +
                numberOfPeople + " " +
                date + " " +
                time + " " +
                confirmation + " " +
                "}";
    }
}