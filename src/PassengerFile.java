import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class PassengerFile extends WorkOnFiles {

    private final String userPath = "files/Users.kakasangi";


    private final long LENGTH_OF_LINE = 84;


    /**
     * creat new User
     *
     * @param ID       The ID of New User
     * @param password The Password Of New User
     */
    public void creatNewPassenger(String ID, String password) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        randomAccessFile.seek(randomAccessFile.length());


        // write file
        randomAccessFile.writeChars(fixStringToWrite(ID)); // 0 - 40 for ID
        randomAccessFile.writeChars(fixStringToWrite(password)); // 40 - 80 password
        randomAccessFile.writeInt(0); // 80 - 84 charge


        // close file
        randomAccessFile.close();
    }

    /**
     * Change Password method
     *
     * @param passengerIndexLine the index of Passenger record in All
     * @param newPass            the new password
     */
    public void changePassword(int passengerIndexLine, String newPass) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        // GO TO START OF LINE AND PASSWORD SECTION
        randomAccessFile.seek((long) passengerIndexLine * LENGTH_OF_LINE + (STRING_FILE_SIZE));
        randomAccessFile.writeChars(fixStringToWrite(newPass));


        randomAccessFile.close();

    }

    /**
     * Charge Account method
     *
     * @param passengerIndexLine the index of Passenger record in All
     */
    public void chargeAccount(int passengerIndexLine) throws IOException {
        Scanner scan = new Scanner(System.in);
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        // GO TO START OF LINE AND Charge SECTION
        long pos = (passengerIndexLine * LENGTH_OF_LINE) + (STRING_FILE_SIZE * 2);
        randomAccessFile.seek(pos);

        int currentCharge = randomAccessFile.readInt();
        System.out.println("Your Balance : " + currentCharge);

        System.out.println("enter how much do you want to charge your account ");
        int charge = scan.nextInt();
        randomAccessFile.seek(pos);
        randomAccessFile.writeInt(currentCharge + charge);

        randomAccessFile.close();
    }


    /**
     * Booking Ticket Method
     *
     * @param flights            List all Flights
     * @param passengerIndexLine the Passenger Index Line wants to buy Ticket
     */
    public void bookingTicket(FlightFile flights, int passengerIndexLine, TicketFile ticketFile) throws IOException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please Enter Flight Id");
        String flightId = scan.nextLine();


        // Searching
        int FlightIndex = flights.searchFlightIndexLineByFlightID(flightId);


        // return if it has Not found
        if (FlightIndex == -1) {
            System.out.println("The Target Flight Not Found");
            new Menu().pause();
            return;
        }


        // PASSENGER FILE
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        // GO TO CHARGE SECTION
        long pos = (long) passengerIndexLine * LENGTH_OF_LINE + STRING_FILE_SIZE * 2;
        randomAccessFile.seek(pos);
        int userCharge = randomAccessFile.readInt();
        randomAccessFile.close();

        // FLIGHT FILE
        randomAccessFile = new RandomAccessFile(flights.getFlightPath(), "rw");

        // GO TO PRICE SECTION
        pos = FlightIndex * flights.getLENGTH_OF_LINE() + STRING_FILE_SIZE * 5;
        randomAccessFile.seek(pos);
        int flightPrice = randomAccessFile.readInt();
        int flightSeats = randomAccessFile.readInt();
        int flightBookedSeats = randomAccessFile.readInt();
        randomAccessFile.close();

        // CONDITIONS
        // return if the Passenger account has not enough charge
        if (userCharge < flightPrice) {
            System.out.println("The price of flight is :" + flightPrice);
            System.out.println("your balance :" + userCharge);
            System.out.println("please charge your account");
            new Menu().pause();
            return;
        }
        // return if the flight is full
        if (flightBookedSeats == flightSeats) {
            System.out.println("The flight is full please choose another flight");
            new Menu().pause();
            return;
        }


        // Creat new ticket
        Ticket newTicket = new Ticket(flightId, passengerIndexLine);
        // TICKET FILE
        randomAccessFile = new RandomAccessFile(ticketFile.getTicketPath(), "rw");
        int emptySpace = ticketFile.searchInTicketFileByTicketId(-404);

        if // GO TO EMPTY SPACE
        (emptySpace != -1) {
            randomAccessFile.seek((long) emptySpace * ticketFile.getLENGTH_OF_LINE());
        } else // GO TO END OF FILE
        {
            randomAccessFile.seek(randomAccessFile.length());
        }

        randomAccessFile.writeInt(newTicket.getTicketId()); // 0 - 4 TICKET ID
        randomAccessFile.writeChars(fixStringToWrite(newTicket.getFlightId())); // 4 - 44 FLIGHT ID
        randomAccessFile.writeInt(newTicket.getUserIndexLine()); // 44 - 48 USER INDEX LINE

        randomAccessFile.close();


        // pay cash
        // USER FILE
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        pos = (passengerIndexLine * LENGTH_OF_LINE) + STRING_FILE_SIZE * 2;
        randomAccessFile.seek(pos);
        randomAccessFile.writeInt(userCharge - flightPrice);
        randomAccessFile.close();

        // add one to booked seats

        // FLIGHT FILE
        randomAccessFile = new RandomAccessFile(flights.getFlightPath(), "rw");
        pos = (FlightIndex * flights.getLENGTH_OF_LINE()) + (STRING_FILE_SIZE * 5) + (INT_SIZE * 2);
        randomAccessFile.seek(pos);
        randomAccessFile.writeInt(flightBookedSeats + 1);
        randomAccessFile.close();

        // show massage
        System.out.println("the Flight Booked");

        new Menu().pause();

    }


    /**
     * Cancellation method
     * 1. search between tickets from ticket file to find the index of ticket in ticket file
     * 2. if it found -> Refund money to passenger
     * 3. Remove a booked seat
     * 4. Remove Ticket From Ticket file
     *
     * @param passengerIndex Index OF Line OF passenger File
     * @param flights        Flights File
     * @param tickets        Tickets File
     */
    public void cancellationTicket(int passengerIndex, FlightFile flights, TicketFile tickets) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println(" \n");
        System.out.println("""
                      Please Enter the ticket Id Which flight do yo want to remove
                      
                <<The system deducts 10% of the flight money as a penalty from your account
                       and returns the rest of the flight money to your account>>""");

        // GET TICKET INDEX LINE

        // get TicketId
        int ticketID = scanner.nextInt();
        int ticketIndexLine = tickets.searchInTicketFileByTicketId(ticketID);
        int passengerIndexLine = tickets.getPassengerIndexLine(ticketIndexLine);

        // Not found condition for Ticket id
        if (ticketIndexLine == -1 || passengerIndexLine != passengerIndex) {
            System.out.println("the target ticket ID not found");
            new Menu().pause();
            return;
        }

        // REFUND MONEY
        String flightID = tickets.getFLightID(ticketIndexLine);
        int flightIndex = flights.searchFlightIndexLineByFlightID(flightID);
        int flightPrice = flights.priceOfFLight(flightIndex);
        refundMoney(passengerIndex, flightPrice);

        // REMOVE A BOOKED SEAT
        flights.removeBookedSeat(flightIndex);

        // REMOVE TICKET FROM TICKET FILE

        tickets.removeTicketFromTicketFile(ticketIndexLine);

        System.out.println("<Flight removed>");
        new Menu().pause();

    }

    /**
     * calculate and refund money after cancel ticket
     *
     * @param indexLine     the passenger record index
     * @param priceOfFlight price of flight
     * @throws IOException work with file exceptions
     */
    public void refundMoney(long indexLine, int priceOfFlight) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        long pos = (indexLine * LENGTH_OF_LINE) + STRING_FILE_SIZE * 2;
        randomAccessFile.seek(pos);

        int passengerCharge = randomAccessFile.readInt();
        randomAccessFile.seek(randomAccessFile.getFilePointer() - INT_SIZE);
        randomAccessFile.writeInt(passengerCharge + (priceOfFlight * 9 / 10));
        randomAccessFile.close();
    }

    /**
     * Searching an id form User ArrayList
     *
     * @param targetId the id we're looking for
     * @return The indexOf the targetId ( return -1 if it was not found )
     */
    public int searchID(String targetId) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        long pos;
        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
            pos = i * LENGTH_OF_LINE;
            if (readString(pos).equals(targetId)) {
                randomAccessFile.close();
                return i;
            }
        }
        randomAccessFile.close();
        return -1;
    }

    /**
     * Searching an id form User ArrayList with Password for signIn section
     *
     * @param targetId   the id we're looking for
     * @param targetPass the password we're looking for
     * @return The indexOf the targetId ( return -1 if it was not found )
     */
    public int checkIdAndPassword(String targetId, String targetPass) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        long pos;
        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
            pos = i * LENGTH_OF_LINE;

            if (readString(pos).equals(targetId)) {
                if (readString(pos + STRING_FILE_SIZE).equals(targetPass)) {
                    randomAccessFile.close();
                    return i;
                }
                randomAccessFile.close();
                return -1;
            }
        }
        randomAccessFile.close();
        return -1;
    }

    /**
     * Print booked Tickets
     *
     * @param passengerIndexLine the index of Passenger in All
     * @param flights            List of all Flights
     */
    public void printBookedTicket(int passengerIndexLine, FlightFile flights, TicketFile ticketFile) throws IOException {
        boolean flag = false;
        randomAccessFile = new RandomAccessFile(ticketFile.getTicketPath(), "rw");
        for (int i = 0; i < randomAccessFile.length() / ticketFile.getLENGTH_OF_LINE(); i++) {
            randomAccessFile = new RandomAccessFile(ticketFile.getTicketPath(), "rw");

            int pos = i * ticketFile.getLENGTH_OF_LINE() + INT_SIZE + STRING_FILE_SIZE;
            randomAccessFile.seek(pos);
            int tempIndexChecker = randomAccessFile.readInt();

            // once RUN CONDITION for print header table
            if (!flag) {
                flights.printFlightHeaderTickets();
            }

            // CHECK IF IT'S OK : PRINT WITH TICKET ID
            if (tempIndexChecker == passengerIndexLine) {

                // GO TO FIRST OF LINE
                pos = i * ticketFile.getLENGTH_OF_LINE();
                randomAccessFile.seek(pos);

                int tempTicketID = randomAccessFile.readInt();
                String tempFlightId = readString(randomAccessFile.getFilePointer());
                randomAccessFile.close();


                pos = (int) (flights.searchFlightIndexLineByFlightID(tempFlightId) * flights.getLENGTH_OF_LINE());

                Flight flight = flights.ExtractFlightFromFile(pos);
                flights.printFlightWithTicketId(flight, tempTicketID);

            }
            flag = true;
            randomAccessFile = new RandomAccessFile(ticketFile.getTicketPath(), "rw");

        }
        // NOT FOUND
        if (!flag) {
            System.out.println("nothing found");
        }
        new Menu().pause();
    }

}
