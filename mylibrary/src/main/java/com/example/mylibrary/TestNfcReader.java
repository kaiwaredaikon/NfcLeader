package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public final class TestNfcReader {

    // 基本的にKotlinとの整合性為に、変数、メソッド、メソッド引数にはNonNull or Nullableをつけて開発を行う。

    @Nullable
    private NfcAdapter nfcAdapter;

    public interface NfcManagerCallback {
        void success( @NonNull String uid);
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
        StringBuilder sb = new StringBuilder();
        for (byte bt : bytes) {
            int i = 0xFF & (int) bt;
            String str = Integer.toHexString(i);
            sb.append(str);
        }
        return sb.toString();
    }
}
