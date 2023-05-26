import java.util.Random;

public class Ticket  extends  WorkOnFiles{
    private int ticketId;
    private String flightId;
    private int userIndexLine;

//    private boolean ticketIdSet = false;

    /**
     * Generate Ticket id
     */
    public Ticket(String flightId, int userIndexLine) {
        Random random = new Random();
        this.ticketId = random.nextInt(100000);
        this.flightId = flightId;
        this.userIndexLine = userIndexLine;
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

    public int getUserIndexLine() {
        return userIndexLine;
    }

    public void setUserIndexLine(int userIndexLine) {
        this.userIndexLine = userIndexLine;
    }
}
