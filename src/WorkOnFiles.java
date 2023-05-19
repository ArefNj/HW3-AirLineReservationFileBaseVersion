import java.io.IOException;
import java.io.RandomAccessFile;

public class WorkOnFiles {

    protected final int STRING_LENGTH = 20;
    protected final int STRING_FILE_SIZE = STRING_LENGTH * 2;
    protected RandomAccessFile randomAccessFile;

    protected final int INT_SIZE = 4;


    public String readString(long startPoint) throws IOException {
        StringBuilder str = new StringBuilder();
        randomAccessFile.seek(startPoint);
        for (long i = 0; i < STRING_LENGTH; i++) {
            str.append(randomAccessFile.readChar());
        }

        return str.toString().trim();
    }

    public String fixStringToWrite(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        while (stringBuilder.length() < STRING_LENGTH) {
            stringBuilder.append(" ");
        }
        string = stringBuilder.toString();
        return string.substring(0, STRING_LENGTH);

    }


}
