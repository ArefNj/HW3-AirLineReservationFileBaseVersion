import java.io.IOException;
import java.util.Scanner;

public class Admin {

    /**
     * singleton admin instance
     */
    private static Admin singletonAdmin;

    private Admin() {
    }

    public static Admin getInstance() {
        if (singletonAdmin == null) {
            singletonAdmin = new Admin();
        }
        return singletonAdmin;
    }

    /**
     * Admin section
     */
    public void admin(FlightFile flights) throws IOException {
        Scanner scan = new Scanner(System.in);
        new Menu().printAdminMenu();
        int key = scan.nextInt();

        switch (key) {
            // Add
            case 1:
                flights.addFlight();
                break;
            // Update
            case 2:
                flights.updateFlight();
                break;
            // Remove
            case 3:
                flights.removeFlight();
                break;
            // Flight Schedule
            case 4:
                new FlightFile().printFlightHeader();
                new FlightFile().printAllFlight();
                break;
            // Exit point
            case 0:
                return;
            default:
                System.out.println("please check your entry");
                new Menu().pause();
                break;
        }
        this.admin(flights);

    }
}
