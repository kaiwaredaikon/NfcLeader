package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;

public final class TestNfcReader {

    private NfcAdapter nfcAdapter;

    public interface NfcManagerCallback {
        void success(String uid);
        void error();
    }

    private NfcManagerCallback nfcManagerCallback;

    public void setCallbacks(NfcManagerCallback nfcManagerCallback) {
        this.nfcManagerCallback = nfcManagerCallback;
    }

    public void initNfcAdapter(Context context) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (!nfcAdapter.isEnabled()) {
            return;
        }
    }

    public void startDetection(Activity activity) {
        nfcAdapter.enableReaderMode(activity, new ReaderCallback(), NfcAdapter.FLAG_READER_NFC_F, null);
    }

    public void endDetection(Activity activity) {
        nfcAdapter.disableReaderMode(activity);
    }

    private class ReaderCallback implements NfcAdapter.ReaderCallback {
        @Override
        public void onTagDiscovered(Tag tag) {
            byte[] rawid = tag.getId();
            final String idm = bytesToString(rawid);
            Log.d("TestNfcReader", "Tag ID: " + idm);
            nfcManagerCallback.success(idm);
        }
    }

    private String bytesToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte bt : bytes) {
            int i = 0xFF & (int) bt;
            String str = Integer.toHexString(i);
            sb.append(str);
        }
        return sb.toString();
    }
}
