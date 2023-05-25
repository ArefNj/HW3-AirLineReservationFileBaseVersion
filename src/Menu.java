import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private final PassengerFile PASSENGER_FILE = new PassengerFile();
    private final FlightFile FLIGHTS_FILE = new FlightFile();

    private final TicketFile TICKET_FILE = new TicketFile();

    public void menu() throws IOException {
        Scanner scan = new Scanner(System.in);

        printMainMenu();
        int mainMenuKey = scan.nextInt();

        switch (mainMenuKey) {
            case 1 -> signIn();
            case 2 -> signUp();
            default -> {
                System.out.println("Please Enter the proper value");
                pause();
            }
        }
        this.menu();
    }

    /**
     * SignIn method
     * Admin Menu :
     * UserId : Admin | Password : Admin
     * else :
     *  go to passenger Menu
     */
    public void signIn() throws IOException {
        Scanner scan = new Scanner(System.in);
        printSignInMenuHeader();

        System.out.println("Enter your ID");
        String tempId = scan.nextLine();


        System.out.println("Enter your password");
        String tempPass = scan.nextLine();
        // Admin Entry point
        if (tempId.equals("Admin") && tempPass.equals("Admin")) {
            Admin admin = Admin.getInstance();
            admin.admin(FLIGHTS_FILE);
            System.out.println("Have Nice Day !");
            pause();
            this.menu();


        }
        // else Users
        else {
            int indexKey = PASSENGER_FILE.findUserIndex(tempId, tempPass);
            if (indexKey == -1) {
                System.out.println("Entry id or password is wrong");
                pause();
                this.menu();
            }
            passengerMenu(indexKey);
        }


    }

    /**
     * SignUp method
     * get a user ID & Password for account
     *
     */
    public void signUp() throws IOException {
        Scanner scan = new Scanner(System.in);
        printSignUpMenuHeader();

        System.out.println("Enter your ID ( Enter 'X' to Exit )");
        String tempId = scan.nextLine();

        if (tempId.equals("X")) {
            this.menu();
        }

        if (PASSENGER_FILE.findUserIndex(tempId) != -1 || tempId.equals("Admin")) {
            System.out.println("The User name was Token");
            pause();
            this.signUp();
        }

        System.out.println("Enter your password");
        String tempPass = scan.nextLine();

        PASSENGER_FILE.creatNewPassenger(tempId, tempPass);

        this.menu();


    }

    /** Passenger Menu method
     *  use a SwitchCase for go to other methods
     */
    public void passengerMenu(int passengerIndex) throws IOException {
        printPassengerMenu();
        Scanner scan = new Scanner(System.in);
        int key = scan.nextInt();

        switch (key) {
            // change Password
            case 1 -> {
                System.out.print("Enter new password\n");
                scan.nextLine();
                String newPassengerPassword = scan.nextLine();
                PASSENGER_FILE.changePassword(passengerIndex, newPassengerPassword);
            }
            // searching
            case 2 -> FLIGHTS_FILE.filterFlight();

            // booking
            case 3 -> PASSENGER_FILE.bookingTicket(FLIGHTS_FILE, passengerIndex, TICKET_FILE);

            // cancel
//            case 4 -> PASSENGER_FILE.cancellationTicket(passengerIndex, AllFlightsList);

            // see booked
            case 5 -> PASSENGER_FILE.printBookedTicket(passengerIndex, FLIGHTS_FILE, TICKET_FILE);

            // add charge
            case 6 -> PASSENGER_FILE.chargeAccount(passengerIndex);
            case 0 -> {
                System.out.println(" Have nice day !");
                pause();
                this.menu();
            }
            default -> {
                System.out.println("please check your entry");
                pause();
                this.passengerMenu(passengerIndex);
            }
        }
        this.passengerMenu(passengerIndex);

    }

    public void printMainMenu() {
        System.out.print("""
                ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                           WELCOME TO AIRLINE RESERVATION SYSTEM
                ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                ..........................MENU OPTIONS........................
                                
                    <1> Sign in
                    <2> Sign up\s
                   
                    
                    
                -->\040""");
    }

    /**
     * Print header of SignUp Menu
     */
    public void printSignUpMenuHeader() {
        System.out.print("""
                ::::::::::::::::::::::::::::::::::::::::
                               Sign Up Menu
                ::::::::::::::::::::::::::::::::::::::::
                 ......................................
                          
                                
                """);
    }

    /**
     * Print Passenger Menu
     */
    public void printPassengerMenu() {
        System.out.print("""
                ::::::::::::::::::::::::::::::::::::::::
                         PASSENGER MENU OPTIONS
                ::::::::::::::::::::::::::::::::::::::::
                 ......................................
                 
                    <1> Change password
                    <2> Search flight tickets
                    <3> Booking ticket
                    <4> Ticket cancellation
                    <5> Booked tickets
                    <6> Add charge
                    <0> Sign out
                    
                    
                -->\040""");
    }


    /**
     * Print header of SignIn Menu
     */
    public void printSignInMenuHeader() {
        System.out.print("""
                ::::::::::::::::::::::::::::::::::::::::
                               Sign In Menu
                ::::::::::::::::::::::::::::::::::::::::
                 ......................................
                          
                                
                                
                """);
    }

    /**
     * Print Admin Menu
     */
    public void printAdminMenu() {
        System.out.print("""
                ::::::::::::::::::::::::::::::::::::::::
                           Admin MENU OPTIONS
                ::::::::::::::::::::::::::::::::::::::::
                 ......................................
                 
                    <1> Add
                    <2> Update
                    <3> Remove
                    <4> Flight schedules
                    <0> Sign out
                    
                    
                -->\040""");
    }

    /**
     * Pause method
     */
    public void pause() {
        System.out.println("Press Any Key To Continue...");
        new java.util.Scanner(System.in).nextLine();
    }
}
