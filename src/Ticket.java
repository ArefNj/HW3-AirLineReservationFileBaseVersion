import java.util.Random;

public class Ticket {
    private int ticketId;
    private String flightId;

    private String userId;

//    private boolean ticketIdSet = false;

    /**
     * Generate Ticket id
     */
    public Ticket(int ticketId, String flightId, String userId) {
        Random random = new Random();
        ticketId = random.nextInt(100);
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.userId = userId;
    }

//    public void generateTicketId() {
//        Random random = new Random();
//        if (!ticketIdSet) {
//            ticketId = random.nextInt(100);
//            ticketIdSet = true;
//        }
//    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
