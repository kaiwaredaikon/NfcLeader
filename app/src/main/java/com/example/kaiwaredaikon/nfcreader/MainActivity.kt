package com.example.kaiwaredaikon.nfcreader

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.mylibrary.TestNfcReader
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private val handler = Handler()
    private var testNfcReader = TestNfcReader()
    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTextView(STATUS_WAIT)
        testNfcReader.initNfcAdapter( this )
    }

    override fun onResume() {
        super.onResume()
        testNfcReader.startDetection( this, this )
    }

    override fun onPause() {
        super.onPause()
        testNfcReader.endDetection( this )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (status == STATUS_WAIT) {

            testNfcReader.getUid(intent)

            setTextView(STATUS_SUCCESS)

            Timer().schedule(3000, 3000, {
                handler.post {
                    setTextView(STATUS_WAIT)
                }
                this.cancel()
            })
        }
    }

    private fun setTextView(sts: Int) {

        status = sts

        Log.d("MainActivity", "sts$sts")

        when (sts) {
            STATUS_WAIT -> textView.text = "カードをかざしてください。"
            STATUS_SUCCESS -> textView.text = "カードを読み込みました"
            STATUS_INVALID -> textView.text = "無効になっています。"
        }
    }

    companion object {
        const val STATUS_WAIT = 1
        const val STATUS_SUCCESS = 2
        const val STATUS_INVALID = 3
    }
}
