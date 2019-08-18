package com.example.kaiwaredaikon.nfcreader

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.mylibrary.TestNfcReader
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), TestNfcReader.NfcManagerCallback {

    private val handler = Handler()
    private var testNfcReader = TestNfcReader()
    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTextView(STATUS_WAIT)
        testNfcReader.initNfcAdapter(this)
        testNfcReader.setCallbacks(this)
    }

    override fun onResume() {
        super.onResume()
        testNfcReader.startDetection(this)
    }

    override fun onPause() {
        super.onPause()
        testNfcReader.endDetection(this)
    }

    override fun success(uid: String?) {
        Log.d("TestNfcReader", "uid$uid")

        handler.post {
            setTextView(STATUS_SUCCESS)
            Timer().schedule(3000, 3000, {
                handler.post {
                    setTextView(STATUS_WAIT)
                }
                this.cancel()
            })
        }
    }

    override fun error() {
    }

    private fun setTextView(sts: Int) {

        status = sts

        Log.d("TestNfcReader", "sts$sts")

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
