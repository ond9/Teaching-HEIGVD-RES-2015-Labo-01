package ch.heigvd.res.lab01.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;
import ch.heigvd.res.lab01.impl.Utils;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

    private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
    private Long lineNumber = 0L;
    private boolean newLine = true;
    private boolean lastCharLineBreak = false;

    public FileNumberingFilterWriter(Writer out) {
        super(out);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        //throw new UnsupportedOperationException("The student has not implemented this method yet.");

        if (newLine) {
            writeLN();
            newLine = false;
        }
        String nextLine[] = Utils.getNextLine(str.substring(off, off + len));
        if (nextLine[0].isEmpty()) {
            super.write(nextLine[1], 0, nextLine[1].length());
            return;
        }
        while (!nextLine[0].isEmpty()) {
            super.write(nextLine[0], 0, nextLine[0].length());
            writeLN();
            nextLine = Utils.getNextLine(nextLine[1]);
        }
        if (!nextLine[1].isEmpty()) {
            super.write(nextLine[1], 0, nextLine[1].length());
        }

    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        //throw new UnsupportedOperationException("The student has not implemented this method yet.");
        super.write(cbuf, off, len);
    }

    @Override
    public void write(int c) throws IOException {
        //throw new UnsupportedOperationException("The student has not implemented this method yet.");
        if (newLine) {
            writeLN();
            newLine = false;
        }
        if (c == '\n' || c == '\r') {
            lastCharLineBreak = true;
        } else if (lastCharLineBreak) {
            writeLN();
            lastCharLineBreak = false;
        }
        super.write(c);
    }

    private void writeLN() throws IOException {
        String str = ++lineNumber + "\t";
        super.write(str, 0, str.length());
    }

}
