import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class PassengerFile extends WorkOnFiles{

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
     * @param passengerIndexLine the index of Passenger in All
     * @param newPass        the new password
     */
    public void changePassword(int passengerIndexLine, String newPass) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        // GO TO START OF LINE AND PASSWORD SECTION
        randomAccessFile.seek((long) passengerIndexLine * LENGTH_OF_LINE +(STRING_FILE_SIZE));
        randomAccessFile.writeChars(fixStringToWrite(newPass));


        randomAccessFile.close();

    }

    /**
     * Charge Account method
     *
     * @param passengerIndexLine the index of Passenger in All
     */
    public void chargeAccount(int passengerIndexLine) throws IOException {
        Scanner scan = new Scanner(System.in);
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        // GO TO START OF LINE AND Charge SECTION
        long pos = (passengerIndexLine * LENGTH_OF_LINE) + (STRING_FILE_SIZE* 2);
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
     * @param flights   List all Flights
     * @param passengerIndexLine the Passenger Index Line wants to buy Ticket
     */
    public void bookingTicket(FlightFile flights, int passengerIndexLine, TicketFile ticketFile) throws IOException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please Enter Flight Id");
        String flightId = scan.nextLine();


        // Searching
        int FlightIndex = flights.searchFlightIndexLineByFlightID(flightId);

        System.out.println(FlightIndex);

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
        int userCharge =  randomAccessFile.readInt();
        randomAccessFile.close();

        // FLIGHT FILE
        randomAccessFile = new RandomAccessFile(flights.getFlightPath(), "rw");

        // GO TO PRICE SECTION
        pos = FlightIndex * flights.getLENGTH_OF_LINE() + STRING_FILE_SIZE * 5;
        randomAccessFile.seek(pos);
        System.out.println(randomAccessFile.getFilePointer());
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
        Ticket newTicket = new Ticket(flightId,passengerIndexLine);
        // TICKET FILE
        randomAccessFile = new RandomAccessFile(ticketFile.getTicketPath(), "rw");
        int ticketCount = (int) (randomAccessFile.length() / ticketFile.getLENGTH_OF_LINE());
        pos = (long) ticketCount * ticketFile.getLENGTH_OF_LINE();
        randomAccessFile.seek(pos);
        randomAccessFile.writeInt(newTicket.getTicketId()); // 0 - 4 TICKET ID
        randomAccessFile.writeChars(fixStringToWrite(newTicket.getFlightId())); // 4 - 44 FLIGHT ID
        randomAccessFile.writeInt(newTicket.getUserIndexLine()); // 44 - 48 USER INDEX LINE

        randomAccessFile.close();



        // pay cash
        // USER FILE
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        pos = (long) (passengerIndexLine * LENGTH_OF_LINE) + STRING_FILE_SIZE * 2;
        randomAccessFile.seek(pos);
        randomAccessFile.writeInt(userCharge - flightPrice);
        randomAccessFile.close();

        // add one to booked seats

        // FLIGHT FILE
        randomAccessFile = new RandomAccessFile(flights.getFlightPath(), "rw");
        pos = (FlightIndex * flights.getLENGTH_OF_LINE()) + (STRING_FILE_SIZE * 5 ) + (INT_SIZE * 2);
        randomAccessFile.seek(pos);
        randomAccessFile.writeInt(flightBookedSeats + 1);
        randomAccessFile.close();

        // show massage
        System.out.println("the Flight Booked");

        new Menu().pause();

    }


    /**
     * Searching an id form User ArrayList
     *
     * @param targetId the id we're looking for
     * @return The indexOf the targetId ( return -1 if it was not found )
     */
    public int findUserIndex(String targetId) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        long pos;
        for (int i = 0; i < randomAccessFile.length()/LENGTH_OF_LINE; i++) {
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
    public int findUserIndex(String targetId, String targetPass) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        long pos;
        for (int i = 0; i < randomAccessFile.length()/LENGTH_OF_LINE; i++) {
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

}
