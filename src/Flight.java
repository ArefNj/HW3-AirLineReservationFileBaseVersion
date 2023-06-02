import java.util.Objects;

public class Flight {
    private final String flightID;
    private final String Origen;
    private final String destination;
    private final String date;
    private final String time;
    private final int price;
    private final int seats;
    private final int bookedSeats;

    // constructor
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

    /**
     * Setter & Getter
     */
    public String getFlightID() {
        return flightID;
    }

    public String getOrigen() {
        return Origen;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public int getSeats() {
        return seats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }


}
