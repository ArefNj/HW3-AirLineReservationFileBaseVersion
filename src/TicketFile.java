import java.io.IOException;
import java.io.RandomAccessFile;

public class TicketFile extends WorkOnFiles{

    private final String ticketPath = "files/Tickets.kakasangi";

    private final int LENGTH_OF_LINE = 48;



    public String getFLightID(int ticketIndexLine) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "r");
        int flightIDPOS = (ticketIndexLine * LENGTH_OF_LINE) + INT_SIZE;
        String flightID = readString((flightIDPOS));
        randomAccessFile.close();
        return flightID;
    }

    public int getPassengerIndexLine(int ticketIndexLine) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "r");
        int passengerIndexPOS = (ticketIndexLine * LENGTH_OF_LINE) + INT_SIZE + STRING_FILE_SIZE;
        randomAccessFile.seek(passengerIndexPOS);
        int passengerIndex = randomAccessFile.readInt();
        randomAccessFile.close();
        return passengerIndex;

    }
    public int searchInTicketFileByTicketId(int targetTicketID) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "r");
        for (int i = 0; i < randomAccessFile.length() / LENGTH_OF_LINE; i++) {
            int pos = i * LENGTH_OF_LINE;
            randomAccessFile.seek(pos);
            int tempTicketID = randomAccessFile.readInt();
            if (tempTicketID == targetTicketID){
                randomAccessFile.close();
                return i;
            }
        }
        randomAccessFile.close();
        return -1;
    }
    public void removeTicketFromTicketFile(int startPos) throws IOException {
        randomAccessFile = new RandomAccessFile(ticketPath, "rw");
        randomAccessFile.seek((long) startPos * LENGTH_OF_LINE);
        randomAccessFile.writeInt(-404);
        randomAccessFile.writeChars(fixStringToWrite("N/A"));
        randomAccessFile.writeInt(-404);
        randomAccessFile.close();
    }
    public String getTicketPath() {
        return ticketPath;
    }
    public int getLENGTH_OF_LINE() {
        return LENGTH_OF_LINE;
    }
}
