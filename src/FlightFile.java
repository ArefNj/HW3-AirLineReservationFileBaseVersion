import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Scanner;

public class FlightFile extends  WorkOnFiles{

    private final String flightPath = "files/Flights.kakasangi";

    private final long LENGTH_OF_LINE = 212;




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
    public int searchFlightIndexLineByFlightID(String targetFlightId) throws IOException {
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
        if (searchFlightIndexLineByFlightID(tempID) != -1) {
            System.out.println("This id has taken");
            return;
        }
        tempID = fixStringToWrite(tempID);

        randomAccessFile = new RandomAccessFile(flightPath, "rw");
        long pos;
        // GO TO EMPTY LINE
        if (searchFlightIndexLineByFlightID("N/A") != -1){
            randomAccessFile = new RandomAccessFile(flightPath, "rw");
            pos = (searchFlightIndexLineByFlightID("N/A") * LENGTH_OF_LINE );
        }
        else { // GO TO THE END OF FILE
            randomAccessFile = new RandomAccessFile(flightPath, "rw");
            pos = randomAccessFile.length();
        }
        randomAccessFile = new RandomAccessFile(flightPath, "rw");
        randomAccessFile.seek(pos);

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

    /**
     * Update flight form flight list
     */
    public void updateFlight() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Please enter the Flight Id Which flight do yo want to update ");
        String flightId = scanner.nextLine();

        int flightIndexLine = searchFlightIndexLineByFlightID(flightId);

        if (flightIndexLine == -1) {
            System.out.println("Please check your entry ");
            new Menu().pause();
            return;
        }

        // Print UpDate Menu
        System.out.println("""
                which detail do you want to change
                <1> Flight Id
                <2> Origin
                <3> Distinction
                <4> Date
                <5> Time
                <6> Price
                <7> Seats
                <0> Exit
                                        
                -->""");

        int key = scanner.nextInt();
        randomAccessFile = new RandomAccessFile(flightPath,"rw");

        switch (key) {
            case 1:
                System.out.println("Enter new Flight id");
                scanner.nextLine();
                // get input
                String tempId = scanner.nextLine();

                if (searchFlightIndexLineByFlightID(tempId) != -1){
                    System.out.println("THIS ID WAS TOKEN");
                    new Menu().pause();
                    break;
                }
                randomAccessFile = new RandomAccessFile(flightPath,"rw");
                tempId = fixStringToWrite(tempId);
                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE);
                randomAccessFile.writeChars(tempId);
                break;
            case 2: // ORIGIN
                System.out.println("Enter new Origin");
                scanner.nextLine();
                // get input
                String tempOrigin = scanner.nextLine();

                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE);
                randomAccessFile.writeChars(tempOrigin);
                break;
            case 3: // DISTANCE
                System.out.println("Enter new Distance");
                scanner.nextLine();
                String tempDistance = scanner.nextLine();
                tempDistance = fixStringToWrite(tempDistance);
                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE*2);
                randomAccessFile.writeChars(tempDistance);
                break;
            case 4: // DATE
                System.out.println("Enter new Date");
                scanner.nextLine();
                String tempDate = scanner.nextLine();
                tempDate = fixStringToWrite(tempDate);
                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE*3);
                randomAccessFile.writeChars(tempDate);
                break;
            case 5: // TIME
                System.out.println("Enter new Time");
                scanner.nextLine();
                String tempTime = scanner.nextLine();
                tempTime = fixStringToWrite(tempTime);
                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE*4);
                randomAccessFile.writeChars(tempTime);
                break;
            case 6: // PRICE
                System.out.println("Enter new Price");
                scanner.nextLine();
                int tempPrice = scanner.nextInt();
                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE*5);
                randomAccessFile.writeInt(tempPrice);
                break;
            case 7: // SEATS
                System.out.println("Enter Seats");
                scanner.nextLine();
                int tempSeats = scanner.nextInt();
                randomAccessFile.seek( flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE*5 + INT_SIZE); // +4 for the Price Int
                randomAccessFile.writeInt(tempSeats);
                break;
            case 0: // EXIT
                return;
            default:
                System.out.println("check your Input");
                new Menu().pause();
                this.updateFlight();
                break;
        }
        randomAccessFile.close();


    }

    /**
     * remove a flight from a flight list
     */
    public void removeFlight() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Please enter the Flight Id Which flight do yo want to remove ");
        String flightId = scanner.nextLine();

        randomAccessFile = new RandomAccessFile(flightPath,"rw");
        int flightIndexLine = searchFlightIndexLineByFlightID(flightId);

        if (flightIndexLine == -1) {
            System.out.println("Please check your entry ");
            new Menu().pause();
            return;
        }

        randomAccessFile = new RandomAccessFile(flightPath,"rw");
        randomAccessFile.seek(flightIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE*5 + INT_SIZE * 2);
        int countBookedSeat = randomAccessFile.readInt();

        if (countBookedSeat != 0) {
            System.out.println("you cannot remove this flight because someone booked this flight");
            new Menu().pause();
            return;
        }

        randomAccessFile.seek(flightIndexLine * LENGTH_OF_LINE);
        randomAccessFile.writeChars(fixStringToWrite("N/A")); // ID
        randomAccessFile.writeChars(fixStringToWrite("N/A")); // ORIGEN
        randomAccessFile.writeChars(fixStringToWrite("N/A")); // DESTINATION
        randomAccessFile.writeChars(fixStringToWrite("N/A")); // DATE
        randomAccessFile.writeChars(fixStringToWrite("N/A")); // TIME
        randomAccessFile.writeInt(0); // PRICE
        randomAccessFile.writeInt(0); // SEATS
        randomAccessFile.writeInt(0); // BOOKED SEATS

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
