public class Passenger {

    private String passengerID;
    private String Password;
    private int charge;

    public Passenger(String passengerID, String password, int charge) {
        this.passengerID = passengerID;
        Password = password;
        this.charge = charge;
    }

    /** Setters & Getters */

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPassengerFlights() {
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }


}
