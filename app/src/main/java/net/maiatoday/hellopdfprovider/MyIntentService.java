package net.maiatoday.hellopdfprovider;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.maiatoday.hellopdfprovider.util.FileUtils;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    private static final String ACTION_FETCH = "net.maiatoday.hellopdfprovider.action.FOO";
    private static final String ACTION_BAZ = "net.maiatoday.hellopdfprovider.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_URL = "net.maiatoday.hellopdfprovider.extra.url";
    private static final String EXTRA_FILENAME = "net.maiatoday.hellopdfprovider.extra.filename";
    private static final String LOG_TAG = MyIntentService.class.getSimpleName();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, String url, String filename) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FETCH);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_FILENAME, filename);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_URL, param1);
        intent.putExtra(EXTRA_FILENAME, param2);
        context.startService(intent);
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_URL);
                final String param2 = intent.getStringExtra(EXTRA_FILENAME);
                handleActionFetch(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_URL);
                final String param2 = intent.getStringExtra(EXTRA_FILENAME);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Fetch in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetch(String url, String filename) {
        try {
            downloadFromUrl(url, filename);
            downloadComplete();
        } catch (IOException e) {
            e.printStackTrace();
            downloadFailed();
        }
    }

    private void downloadFailed() {

    }

    private void downloadComplete() {
        Intent localIntent =
                new Intent(Constants.BROADCAST_ACTION)
                        // Puts the status into the Intent
                        .putExtra(Constants.EXTENDED_DATA_STATUS, "ok");
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(LOG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void downloadFromUrl(String downloadUrl, String fileName) throws IOException {

        URL url = new URL(downloadUrl);

        File file = new File(this.getCacheDir(), fileName);
        FileUtils.createParentDirectories(file);

        long startTime = System.currentTimeMillis();
        Log.d(LOG_TAG, "download beginning");
        Log.d(LOG_TAG, "download url:" + url);
        Log.d(LOG_TAG, "downloaded file name:" + fileName);

        /* Open a connection to that URL. */
        URLConnection ucon = url.openConnection();
        /*
         * Define InputStreams to read from the URLConnection.
         */
        InputStream is = ucon.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        /*
         * Read bytes to the Buffer until there is nothing more to read(-1).
         */
        ByteArrayBuffer baf = new ByteArrayBuffer(5000);
        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }

        /* Convert the Bytes read to a String. */
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baf.toByteArray());
        fos.flush();
        fos.close();
        Log.d(LOG_TAG, "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
    }
}
