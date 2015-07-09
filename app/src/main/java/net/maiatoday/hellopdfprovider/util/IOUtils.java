package net.maiatoday.hellopdfprovider.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by maia on 2015/07/09.
 */
public class IOUtils {
    public final static void close(Reader reader)
    {
        try
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        catch(IOException e){}
    }

    public final static void close(Writer writer)
    {
        try
        {
            if (writer != null)
            {
                writer.close();
            }
        }
        catch(IOException e){}
    }
}
