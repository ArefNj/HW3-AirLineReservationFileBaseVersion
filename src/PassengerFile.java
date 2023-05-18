import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class PassengerFile extends WorkOnFiles{

    private final String userPath = "files/Users.kakasangi";


    private final int LENGTH_OF_LINE = 84;



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
        int pos = passengerIndexLine * LENGTH_OF_LINE + (STRING_FILE_SIZE* 2);
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
     * Searching an id form User ArrayList
     *
     * @param targetId the id we're looking for
     * @return The indexOf the targetId ( return -1 if it was not found )
     */
    public int findUserIndex(String targetId) throws IOException {
        randomAccessFile = new RandomAccessFile(userPath, "rw");
        int pos;
        for (int i = 0; i < randomAccessFile.length()/LENGTH_OF_LINE; i++) {
            pos = i * LENGTH_OF_LINE;
            if (readString(pos, userPath).equals(targetId)) {
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
        int pos;
        for (int i = 0; i < randomAccessFile.length()/LENGTH_OF_LINE; i++) {
            pos = i * LENGTH_OF_LINE;

            if (readString(pos, userPath).equals(targetId)) {
                if (readString(pos + STRING_FILE_SIZE, userPath).equals(targetPass)) {
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

//    public String readString(long startPoint) throws IOException {
//        randomAccessFile = new RandomAccessFile(userPath, "rw");
//        randomAccessFile.seek(startPoint);
//        String tempStr = super.readString(startPoint);
//        return  tempStr.trim();
//
//    }



}
