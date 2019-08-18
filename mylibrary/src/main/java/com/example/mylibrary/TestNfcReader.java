package com.example.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.util.Log;

import java.util.Arrays;

public final class TestNfcReader {

    private NfcAdapter nfcAdapter;

    public void initNfcAdapter( Context context ){
        nfcAdapter = NfcAdapter.getDefaultAdapter( context );

        if( !nfcAdapter.isEnabled() ){
        }
    }

    public void startDetection(Context context, Activity activity ){
        Intent intent = new Intent(context, context.getClass() );
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        nfcAdapter.enableForegroundDispatch( activity, pendingIntent, null, null);
    }

    public void endDetection(Activity activity){
        nfcAdapter.disableForegroundDispatch(activity);
    }

    public byte[] getUid(Intent intent ){
        byte[] uid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        Log.d( "TestNfcReader", "uid = " + Arrays.toString(uid));
        return uid;
    }
}
