package net.maiatoday.hellopdfprovider.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;

/**
 * Created by maia on 2015/07/09.
 */
public class FileUtils {
    private final static int BUFFER_SIZE = 1024;

    public final static boolean writeStringToFile(final String inData, final File toFile)
    {
        if (toFile == null) return false;

        BufferedWriter output = null;
        BufferedReader input = null;
        try
        {
            input = new BufferedReader(new StringReader(inData));
            output = new BufferedWriter(new FileWriter(toFile));

            char[] buffer = new char[BUFFER_SIZE];
            int n;

            while((n = input.read(buffer, 0, BUFFER_SIZE)) != -1)
            {
                output.write(buffer, 0, n);
            }

            return true;
        }
        catch(Exception e){}
        finally
        {
            IOUtils.close(input);
            IOUtils.close(output);
        }
        return false;
    }

    public static final void createParentDirectories(final File inFile)
    {
        if (inFile != null)
        {
            final File parentDir = inFile.getParentFile();

            if ((parentDir != null) && !parentDir.exists())
            {
                parentDir.mkdirs();

            }
        }
    }
}
