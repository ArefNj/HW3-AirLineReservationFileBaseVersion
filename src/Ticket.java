import java.util.Random;

public class Ticket extends WorkOnFiles {
    private final int ticketId;
    private final String flightId;
    private final int userIndexLine;

    /**
     * Generate Ticket id
     */
    public Ticket(String flightId, int userIndexLine) {
        Random random = new Random();
        this.ticketId = random.nextInt(100000);
        this.flightId = flightId;
        this.userIndexLine = userIndexLine;
    }

    /**
     * Setter & Getters
     */
    public int getTicketId() {
        return ticketId;
    }

    public String getFlightId() {
        return flightId;
    }

    public int getUserIndexLine() {
        return userIndexLine;
    }

}
