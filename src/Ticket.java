import java.util.Random;

public class Ticket {
    private int ticketId;
    private String flightId;

    private boolean ticketIdSet = false;

    /**
     * Generate Ticket id
     */
    public void generateTicketId() {
        Random random = new Random();
        if (!ticketIdSet) {
            ticketId = random.nextInt(100);
            ticketIdSet = true;
        }
    }

    /** Setter & Getters */

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
}
