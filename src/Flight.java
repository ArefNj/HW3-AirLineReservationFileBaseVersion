import java.util.Objects;

public class Flight {
    private String flightID;
    private String Origen;
    private String destination;
    private String date;
    private String time;
    private int price;
    private int seats;
    private int bookedSeats;

    public Flight(String flightID, String origen, String destination, String date, String time, int price, int seats, int bookedSeats) {
        this.flightID = flightID;
        Origen = origen;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
        this.seats = seats;
        this.bookedSeats = bookedSeats;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return price == flight.price && seats == flight.seats && bookedSeats == flight.bookedSeats && Objects.equals(flightID, flight.flightID) && Objects.equals(Origen, flight.Origen) && Objects.equals(destination, flight.destination) && Objects.equals(date, flight.date) && Objects.equals(time, flight.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightID, Origen, destination, date, time, price, seats, bookedSeats);
    }

    /**
     * Setter & Getter
     */
    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public void setTicket() {
    }


}
