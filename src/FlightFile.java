import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class FlightFile extends  WorkOnFiles{

    private final String flightPath = "files/Flights.kakasangi";

    private final long LENGTH_OF_LINE = 212;

    public final String NO_FILTER = "X";




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
    public void printAllFlight() throws IOException {
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
    public void printFlightWithTicketId(Flight passengerFlight, int ticketId) {
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
            long pos = ((long) i * LENGTH_OF_LINE) + STRING_FILE_SIZE;
            if (readString(pos).equals(targetFlightOrigen)){
            ExtractFlightFromFile(filterFlights,i * LENGTH_OF_LINE );

            }

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


        for (int i = 0; i < randomAccessFile.length()/ LENGTH_OF_LINE; i++) {
            // GO TO destination SECTION
            int pos = (int) (( i * LENGTH_OF_LINE) + STRING_FILE_SIZE * 2);
            if (readString(pos).equals(targetDestination)){
                // GO TO FIRST OF LINE
                pos = (int) (( i * LENGTH_OF_LINE));
                ExtractFlightFromFile(filterFlights,pos);

            }

        }

        randomAccessFile.close();
    }

    public void removeBookedSeat(int flightIndexLine) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");
        randomAccessFile.seek((flightIndexLine * LENGTH_OF_LINE) + (STRING_FILE_SIZE*5 + INT_SIZE * 2));
        int countOFBookedSeats = randomAccessFile.readInt();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - INT_SIZE);
        randomAccessFile.writeInt(countOFBookedSeats - 1);
        randomAccessFile.close();
    }

    public int priceOfFLight(int flightIndexLine) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "r");
        long pos = (flightIndexLine * LENGTH_OF_LINE ) + (STRING_FILE_SIZE * 5);
        randomAccessFile.seek(pos);
        int price = randomAccessFile.readInt();
        randomAccessFile.close();
        return price;

    }



    /**
     * filter flights by date
     *
     * @param targetDate    the target date
     * @param filterFlights the Flights list
     */
    public void filterFlightsByDate(String targetDate, ArrayList<Flight> filterFlights) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");


        for (int i = 0; i < randomAccessFile.length()/ LENGTH_OF_LINE; i++) {
            // GO TO date SECTION
            int pos = (int) (( i * LENGTH_OF_LINE) + STRING_FILE_SIZE * 3);
            if (readString(pos).equals(targetDate)){
                // GO TO FIRST OF LINE
                pos = (int) (( i * LENGTH_OF_LINE));
                ExtractFlightFromFile(filterFlights,pos);

            }

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


        for (int i = 0; i < randomAccessFile.length()/ LENGTH_OF_LINE; i++) {
            // GO TO time SECTION
            int pos = (int) (( i * LENGTH_OF_LINE) + STRING_FILE_SIZE * 4);
            if (readString(pos).equals(targetTime)){
                // GO TO FIRST OF LINE
                pos = (int) (( i * LENGTH_OF_LINE));
                ExtractFlightFromFile(filterFlights,pos);

            }

        }

        randomAccessFile.close();
    }


    /**
     * search Filter section
     */
    public void filterFlight() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Do you want to show you all Flights
                <1> Yes
                <2> No
                <0> return
                                
                -->
                """);

        int showAll = scanner.nextInt();
        switch (showAll) {
            // SHOW ALL
            case 1:
                printFlightHeader();
                printAllFlight();
                break;
            // FILTER SECTION
            case 2:
                System.out.println("Input Origen for Filter Flights by Origen ( Enter X for skip ) ");
                scanner.nextLine();
                String origen = scanner.nextLine();

                System.out.println("Input Destination for Filter Flights by Destination ( Enter X for skip ) ");
                String destination = scanner.nextLine();

                System.out.println("Input Date for Filter Flights by Date ( Enter X for skip ) ");
                String date = scanner.nextLine();

                System.out.println("Input Time for Filter Flights by Time ( Enter X for skip ) ");
                String time = scanner.nextLine();

                System.out.println("enter Y to Filter Flights by price (if you dont want to enter something");
                String price = scanner.nextLine();

                // USE HashSet TO STORAGE THE FLIGHTS
                ArrayList<Flight> newFlightList = new ArrayList<>();

                // ORIGEN FILTER
                if (!origen.equals(NO_FILTER)) {
                    filterFlightsByOrigen(origen, newFlightList);
                }
                // DESTINATION FILTER
                if (!destination.equals(NO_FILTER)) {
                    filterFlightsByDestination(destination, newFlightList);
                }
                // DATE FILTER
                if (!date.equals(NO_FILTER)) {
                    filterFlightsByDate(date, newFlightList);
                }
                // TIME FILTER
                if (!time.equals(NO_FILTER)) {
                    filterFlightsByTime(time, newFlightList);
                }

                // PRICE FILTER

                if (price.equals("Y") && newFlightList.isEmpty()){
                    randomAccessFile = new RandomAccessFile(flightPath, "rw");

                    for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
                        long pos = i * LENGTH_OF_LINE;
                        ExtractFlightFromFile(newFlightList , pos );
                    }
                    randomAccessFile.close();
                }
                if (price.equals("Y")) {
                    System.out.println("Input the maximum price");
                    int maxPrice = scanner.nextInt();
                    System.out.println("Input the minimum price ");
                    int minPrice = scanner.nextInt();
                    boolean flag = false;


                    for (int i = newFlightList.size() - 1; i >= 0; i--) {
                        // check Max Price
                        if (newFlightList.get(i).getPrice() <= maxPrice) {
                            // check Min Price
                            if (newFlightList.get(i).getPrice() >= minPrice) {

                                // CHECK FOR PRINT HEADER
                                if (!flag) {
                                    flag = true;
                                    printFlightHeader();
                                }

                                // PRINT
                                printFlight(newFlightList.get(i));
                            }
                        }
                    }

                } else {
                    printFlightHeader();
                    for (Flight flight : newFlightList) {
                        printFlight(flight);
                    }
                }


                break;
            case 0:
                return;
            default:
                this.filterFlight();
        }


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
        if (searchFlightIndexLineByFlightID(tempID) != -1 || tempID.equals("N/A")) {
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

    public void ExtractFlightFromFile(ArrayList<Flight> filterFlights, long pos) throws IOException {

            randomAccessFile = new RandomAccessFile(flightPath, "rw");
            randomAccessFile.seek(pos);
                                // read FlightID       // read Origen                             // read Destination                            // read Date                                     // read Time                                    // read Price               // read seats               // read BookedSeat
            Flight flight = new Flight(readString(pos), readString(pos + STRING_FILE_SIZE), readString(pos + STRING_FILE_SIZE *2), readString(pos + STRING_FILE_SIZE * 3), readString(pos + STRING_FILE_SIZE *4), randomAccessFile.readInt(), randomAccessFile.readInt(), randomAccessFile.readInt());
            filterFlights.add(flight);

    }

    public Flight ExtractFlightFromFile(long pos) throws IOException {
        randomAccessFile = new RandomAccessFile(flightPath, "rw");
        randomAccessFile.seek(pos);
                  // read FlightID         // read Origen                             // read Destination                            // read Date                                     // read Time                                    // read Price               // read seats               // read BookedSeat
        return new Flight(readString(pos), readString(pos + STRING_FILE_SIZE), readString(pos + STRING_FILE_SIZE *2), readString(pos + STRING_FILE_SIZE * 3), readString(pos + STRING_FILE_SIZE *4), randomAccessFile.readInt(), randomAccessFile.readInt(), randomAccessFile.readInt());

    }


    public String getFlightPath() {
        return flightPath;
    }

    public long getLENGTH_OF_LINE() {
        return LENGTH_OF_LINE;
    }
}
