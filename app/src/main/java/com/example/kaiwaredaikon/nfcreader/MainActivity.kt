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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private val handler = Handler()

    private var status = 0
    private val nfcAdapter by lazy { NfcAdapter.getDefaultAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setText(STATUS_WAIT)
      checkNfcStatus()
        // NfcAdapter の検出開始を実行する。
    }

    override fun onResume() {

        Log.d("NFC", "onResume ");
        super.onResume()

        // PendingIntent 起動時に実行される intent の生成と PendingIntent のインスタンス化をする。
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val feliCaPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // NfcAdapter.ACTION_NDEF_DISCOVERED をアクション名とするフィルタを生成する。
        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { this.addDataType("*/*") }
        nfcAdapter.enableForegroundDispatch(this, feliCaPendingIntent, arrayOf(filter), arrayOf(NFC_TYPES))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("NFC", "感知完治感知")

        if (status == STATUS_WAIT) {
            Log.d("NFC", "変更変更")
            setText(STATUS_SUCCESS)

            Timer().schedule(3000, 3000, {
                Log.d("NFC", "タイマータイマー")
                handler.post {
                    setText(STATUS_WAIT)
                }
                Log.d("NFC", "タイマータイマー2")
                this.cancel()
            })
        }
    }

    private fun setText(sts: Int) {

        status = sts

        Log.d("NFC", "sts$sts")
        when (sts) {
            STATUS_WAIT -> textView.text = "Capiカードをかざしてください。"
            STATUS_SUCCESS -> textView.text = "カードを読み込みました"
            STATUS_INVALID -> textView.text = "NFCが無効になっています。"
        }
    }

    private fun checkNfcStatus() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (!nfcAdapter.isEnabled) {
            AlertDialog.Builder(this).also {
                it.setMessage("NFCが無効になっています。" + "NFC設定画面を開きますか？")
                it.setPositiveButton("はい") { _, _ ->
                    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                }
                it.setNegativeButton("いいえ") { _, _ ->
                    setText(STATUS_INVALID)
                }
            }.show()
        }
    }

    companion object {
        const val STATUS_WAIT = 1
        const val STATUS_SUCCESS = 2
        const val STATUS_INVALID = 3
        val NFC_TYPES = arrayOf(NfcF::class.java.name)
    }
}
