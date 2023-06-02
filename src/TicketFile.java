import java.io.IOException;
import java.io.RandomAccessFile;

public class TicketFile extends WorkOnFiles {

    private final String ticketPath = "files/Tickets.kakasangi";
    private final int LENGTH_OF_LINE = 48;


    /**
     * get FlightID with index of ticket record
     *
     * @param ticketIndexLine index of ticket record
     * @return the flight id of ticket
     */
    public String getFLightID(int ticketIndexLine) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "r");
        int flightIDPOS = (ticketIndexLine * LENGTH_OF_LINE) + INT_SIZE;
        String flightID = readString((flightIDPOS));
        randomAccessFile.close();
        return flightID;
    }

    /**
     * get passenger index record from ticket
     *
     * @param ticketIndexLine index of ticket record
     * @return index of passenger record
     */
    public int getPassengerIndexLine(int ticketIndexLine) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "r");
        int passengerIndexPOS = (ticketIndexLine * LENGTH_OF_LINE) + INT_SIZE + STRING_FILE_SIZE;
        randomAccessFile.seek(passengerIndexPOS);
        int passengerIndex = randomAccessFile.readInt();
        randomAccessFile.close();
        return passengerIndex;

    }

    /**
     * search in ticket file by ticket id to find index of record of ticket
     *
     * @param targetTicketID ticket Id
     * @return index of ticket record
     */
    public int searchInTicketFileByTicketId(int targetTicketID) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "r");
        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
            int pos = i * LENGTH_OF_LINE;
            randomAccessFile.seek(pos);
            int tempTicketID = randomAccessFile.readInt();
            if (tempTicketID == targetTicketID) {
                randomAccessFile.close();
                return i;
            }
        }
        randomAccessFile.close();
        return -1;
    }

    /**
     * set null in files of tickets
     *
     * @param indexPos index of ticket record
     */
    public void removeTicketFromTicketFile(int indexPos) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "rw");
        randomAccessFile.seek((long) indexPos * LENGTH_OF_LINE);
        randomAccessFile.writeInt(-404);
        randomAccessFile.writeChars(fixStringToWrite("N/A"));
        randomAccessFile.writeInt(-404);
        randomAccessFile.close();
    }

    /**
     * getter
     */
    public String getTicketPath() {
        return ticketPath;
    }

    public int getLENGTH_OF_LINE() {
        return LENGTH_OF_LINE;
    }
}
