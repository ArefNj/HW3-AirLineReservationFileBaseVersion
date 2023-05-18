import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class FlightFile extends  WorkOnFiles{

    private final String flightPath = "files/Flights.kakasangi";

    private final int LENGTH_OF_LINE = 212;




    /**
     * print header for search filter section and admin Flight schedules section
     */
    public void printFlightHeader() {
        System.out.println("|FlightId   |Origin     |Destination  |Date        |Time       |Price      |Seats |Booked Seats|");
    }

    /**
     * print Flight booked with ticket IDs
     */
    public void printFlightHeaderTickets() {
        System.out.println("|FlightId   |Origin     |Destination  |Date        |Time       |Price      |Seats |Booked Seats|Ticket Id|");
    }


    /**
     * print all flight which passed the method
     */
    public void printFlight() throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");
        for (int i = 0; i < randomAccessFile.length()/ LENGTH_OF_LINE ; i++) {
            long pos = (long) i * LENGTH_OF_LINE;
            String flightId = readString(pos);
            String flightOrigen = readString(pos+ STRING_FILE_SIZE);
            String flightDestination = readString(pos+ STRING_FILE_SIZE *2);
            String flightDate = readString(pos+ STRING_FILE_SIZE *3);
            String flightTime = readString(pos+ STRING_FILE_SIZE *4);
            randomAccessFile.seek(pos + STRING_FILE_SIZE *5);
            int flightPrice = randomAccessFile.readInt();
            int flightSeats = randomAccessFile.readInt();
            int flightBookedSeats = randomAccessFile.readInt();
            Flight flight = new Flight(flightId,flightOrigen,flightDestination,flightDate,flightTime,flightPrice,flightSeats,flightBookedSeats);

            printFlight(flight);
        }
        randomAccessFile.close();
        new Menu().pause();
    }


    /**
     * print a specific flight details
     *
     * @param flight the target flight
     */
    public void printFlight(Flight flight) {

        System.out.println("................................................................................................");
        System.out.printf("|%-11s|%-11s|%-13s|%-12s|%-11s|%-,11d|%-6d|%-12d|\n"
                , flight.getFlightID()
                , flight.getOrigen()
                , flight.getDestination()
                , flight.getDate()
                , flight.getTime()
                , flight.getPrice()
                , flight.getSeats()
                , flight.getBookedSeats());
    }

    /**
     * print flight details with ticket ID
     *
     * @param passengerFlight the passenger flight list
     * @param ticketId        the ticket ID of flight
     */
    public void printFlight(Flight passengerFlight, int ticketId) {
        System.out.println("..........................................................................................................");
        System.out.printf("|%-11s|%-11s|%-13s|%-12s|%-11s|%-,11d|%-6d|%-12d|%-9d|\n"
                , passengerFlight.getFlightID()
                , passengerFlight.getOrigen()
                , passengerFlight.getDestination()
                , passengerFlight.getDate()
                , passengerFlight.getTime()
                , passengerFlight.getPrice()
                , passengerFlight.getSeats()
                , passengerFlight.getBookedSeats()
                , ticketId);


    }

    /**
     * search Flight index by Flight ID
     *
     * @param targetFlightId the flight ID that you wants to find
     * @return the index the target ( return -1 if it was not found )
     */
    public int findFlightIndex(String targetFlightId) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");

        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
            long pos = (long) i * LENGTH_OF_LINE;
            String flightID = readString(pos);
            if (flightID.equals(targetFlightId)) {
                randomAccessFile.close();
                return i;
            }
        }
        randomAccessFile.close();
        return -1;
    }

    /**
     * filter flights by origen
     *
     * @param targetFlightOrigen the target origen
     * @param filterFlights      the Flights list
     */
    public void filterFlightsByOrigen(String targetFlightOrigen, ArrayList<Flight> filterFlights) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");


        for (int i = 0; i < randomAccessFile.length()/ LENGTH_OF_LINE; i++) {
            // GO TO ORIGIN SECTION
            long pos = ((long) i * LENGTH_OF_LINE) + STRING_LENGTH;
            ExtractFlightFromFile(targetFlightOrigen, filterFlights, pos);
        }
        randomAccessFile.close();
    }

    /**
     * filter flights by destination
     *
     * @param targetDestination the target destination
     * @param filterFlights     the Flights list
     */
    public void filterFlightsByDestination(String targetDestination, ArrayList<Flight> filterFlights) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");

        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
            // GO TO DESTINATION SECTION
            long pos = ((long) i * LENGTH_OF_LINE) + STRING_LENGTH *2;
            ExtractFlightFromFile(targetDestination, filterFlights, pos);
        }
        randomAccessFile.close();
    }



    /**
     * filter flights by date
     *
     * @param targetDate    the target date
     * @param filterFlights the Flights list
     */
    public void filterFlightsByDate(String targetDate, ArrayList<Flight> filterFlights) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");

        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE ; i++) {
            // GO TO DATE SECTION
            long pos = ((long) i * LENGTH_OF_LINE) + STRING_LENGTH *3;
            ExtractFlightFromFile(targetDate, filterFlights, pos);
        }
        randomAccessFile.close();
    }

    /**
     * filter flights by time
     *
     * @param targetTime    the target time
     * @param filterFlights the Flights list
     */
    public void filterFlightsByTime(String targetTime, ArrayList<Flight> filterFlights) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");

        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE ; i++) {
            // GO TO DATE SECTION
            long pos = ((long) i * LENGTH_OF_LINE) + STRING_LENGTH *4;
            ExtractFlightFromFile(targetTime, filterFlights, pos);
        }
        randomAccessFile.close();
    }


    // Administrator methods

    /**
     * Add flight to flight List
     */
    public void addFlight() throws IOException {
        Scanner scan = new Scanner(System.in);
        // get ID in a temp String to check
        System.out.println("Enter Flight Id");
        String tempID = scan.nextLine();

        // check ID
        if (findFlightIndex(tempID) != -1) {
            System.out.println("This id has taken");
            return;
        }
        tempID = fixStringToWrite(tempID);

        randomAccessFile = new RandomAccessFile(flightPath, "rw");
        randomAccessFile.seek(randomAccessFile.length());

        randomAccessFile.writeChars(tempID); //0 - 40 for flightID
        System.out.println("Enter The Origen");
        randomAccessFile.writeChars(fixStringToWrite(scan.nextLine())); // 40 - 80 for Origen
        System.out.println("Enter the Destination");
        randomAccessFile.writeChars(fixStringToWrite(scan.nextLine())); // 80 - 120 for Destination
        System.out.println("Enter the date");
        randomAccessFile.writeChars(fixStringToWrite(scan.nextLine()));// 120 - 160 for Date
        System.out.println("Enter Time");
        randomAccessFile.writeChars(fixStringToWrite(scan.nextLine()));// 160 - 200 for Time
        System.out.println("Enter Price");
        randomAccessFile.writeInt(scan.nextInt()); // 200 - 204 for Price
        System.out.println("Enter number of seats");
        randomAccessFile.writeInt(scan.nextInt()); // 204 - 208 for Count of Seats
        randomAccessFile.writeInt(0); // 208 - 212 for Count of Booked Seats

        randomAccessFile.close();

    }

    public void ExtractFlightFromFile(String filterTarget, ArrayList<Flight> filterFlights, long pos) throws IOException {
        String temp = readString(pos);

        if (temp.equals(filterTarget)) {

            randomAccessFile.seek(pos + STRING_FILE_SIZE *5);
            Flight flight = new Flight(readString(pos), readString(pos + STRING_FILE_SIZE), readString(pos + STRING_FILE_SIZE *2), readString(pos + STRING_FILE_SIZE * 3), readString(pos + STRING_FILE_SIZE *4), randomAccessFile.readInt(), randomAccessFile.readInt(), randomAccessFile.readInt());
            filterFlights.add(flight);
        }
    }




}
