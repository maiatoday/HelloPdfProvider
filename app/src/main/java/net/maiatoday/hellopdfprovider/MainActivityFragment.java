package net.maiatoday.hellopdfprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private Button mFetch;
    private Button mPaw;
    private File mFile;
    private String mFileName = "documents/file.pdf";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The filter's action is BROADCAST_ACTION
        IntentFilter mIntentFilter = new IntentFilter(
                Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMyReceiver, mIntentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMyReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mFetch = (Button) view.findViewById(R.id.btn_fetch);
        mPaw = (Button) view.findViewById(R.id.btn_paw);
        return view;
    }

    public void fetch(View view) {
        Log.d(LOG_TAG, "fetch");
        MyIntentService.startActionFoo(getActivity(), getString(R.string.url_pdf), mFileName);
        mPaw.setEnabled(false);
    }

    public void paw(View view) {
        Log.d(LOG_TAG, "paw");
        shareDocument(mFile);
        mPaw.setEnabled(false);
    }

    private void shareDocument(final File inFile)
    {
        if (inFile != null) {
            // let the FileProvider generate an URI for this private file
            final Uri uri = FileProvider.getUriForFile(getActivity(), "net.maiatoday.hellopdfprovider", inFile);
            // create an intent, so the user can choose which application he/she wants to use to share this file
            final Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("application/pdf")
                    .setSubject(this.getString(R.string.share_subject))
                    .setStream(uri)
                    .setChooserTitle(R.string.share_title)
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            this.startActivity(intent);
        }
    }


    private BroadcastReceiver mMyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(LOG_TAG, "Do something.");
            mPaw.setEnabled(true);
            mFile = new File(getActivity().getCacheDir(), mFileName);
        }
    };

}
