package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.opentok.android.*
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.ResponseStopLive
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.API_KEY
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_go_live.*


class GoLiveActivity : BaseActivity(), Session.SessionListener, PublisherKit.PublisherListener {

    companion object {
        val KEY_POST_ID = "KEY_POST_ID"
        val KEY_SESSION_ID = "KEY_SESSION_ID"
        val KEY_TOKEN = "KEY_TOKEN"
    }


    //    private val SESSION_ID = "1_MX40NjI1NDI3Mn5-MTU1OTE5NzAwNTQyM35BdW5EcUhtaVR4U1BwWG1JNUVVSldxMDl-fg"
//    private val SESSION_ID = "2_MX40NjI1NDI3Mn5-MTU1OTIwMzEzNzYzN35kNmtNKzRvNkZDT1ZLQkZ2ZnMvd0J1OEd-QX4"

    //    private val TOKEN =
//        "T1==cGFydG5lcl9pZD00NjI1NDI3MiZzaWc9ZGQxM2UwZDhmYWIyNjczOWQ5N2U4MzgwZDQxODUzOTJjYTNmODkyMzpzZXNzaW9uX2lkPTFfTVg0ME5qSTFOREkzTW41LU1UVTFPVEU1TnpBd05UUXlNMzVCZFc1RWNVaHRhVlI0VTFCd1dHMUpOVVZWU2xkeE1EbC1mZyZjcmVhdGVfdGltZT0xNTU5MTk4MTE4Jm5vbmNlPTAuMDEzNjIxMDk3Mjc3MTEwNjk0JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE1NjE3OTAxMTYmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0="
//    private val TOKEN =
//        "T1==cGFydG5lcl9pZD00NjI1NDI3MiZzaWc9NWYwZjBlNjY4NTAzNjgyZmZhOTYxMjg3M2E1YTMyZGVjYjAzZDc2NDpzZXNzaW9uX2lkPTFfTVg0ME5qSTFOREkzTW41LU1UVTFPVEU1TnpBd05UUXlNMzVCZFc1RWNVaHRhVlI0VTFCd1dHMUpOVVZWU2xkeE1EbC1mZyZjcmVhdGVfdGltZT0xNTU5MTk5Mjc5Jm5vbmNlPTAuNjUzMDgzNDgyMzMyNDkxOCZyb2xlPXN1YnNjcmliZXImZXhwaXJlX3RpbWU9MTU2MTc5MTI3NyZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="
//    private val TOKEN =
//        "T1==cGFydG5lcl9pZD00NjI1NDI3MiZzaWc9YjlkOTM0YzlmMmVkZmZiNTYyNjhmYmI4MTNmYTFkNWQ3ZDVjNDRhMDpzZXNzaW9uX2lkPTJfTVg0ME5qSTFOREkzTW41LU1UVTFPVEl3TXpFek56WXpOMzVrTm10Tkt6UnZOa1pEVDFaTFFrWjJabk12ZDBKMU9FZC1RWDQmY3JlYXRlX3RpbWU9MTU1OTIwMzEzNyZyb2xlPXB1Ymxpc2hlciZub25jZT0xNTU5MjAzMTM3Ljg3OTk5OTUyMDU2OCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="

    private val LOG_TAG = this::class.java.simpleName
    private val RC_SETTINGS_SCREEN_PERM = 123
    private val RC_VIDEO_APP_PERM = 124


    private var postId: Int? = null
    private var sessionId: String? = null
    private var token: String? = null

    private var mSession: Session? = null

    private var mPublisher: Publisher? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_live)

        postId = intent?.getIntExtra(KEY_POST_ID, -1)
        sessionId = intent?.getStringExtra(KEY_SESSION_ID)
        token = intent?.getStringExtra(KEY_TOKEN)

        Log.d(LOG_TAG, sessionId)
        Log.d(LOG_TAG, token)


        initializeAndConnectToSession()

        initClickListeners()

    }

    /* Activity lifecycle methods */
    override fun onPause() {
        Log.d(LOG_TAG, "onPause")
        super.onPause()
        if (mSession != null) {
            mSession?.onPause()
        }
    }

    override fun onResume() {
        Log.d(LOG_TAG, "onResume")
        super.onResume()
        if (mSession != null) {
            mSession?.onResume()
        }
    }

    override fun onDestroy() {
        disconnectSession()
        super.onDestroy()
    }

    private fun initClickListeners() {
        btn_stop.setOnClickListener {
            disconnectSession()
            onBackPressed()
        }

        btnBack.setOnClickListener {
            disconnectSession()
            onBackPressed()
        }
    }

    private fun initializeAndConnectToSession() {
        mSession = Session.Builder(this, API_KEY, sessionId).build()
        mSession?.setSessionListener(this)
        mSession?.connect(token)
    }

    private fun disconnectSession() {
        mSession?.disconnect()

        if (postId == -1) {
            Log.d("postId", "Invalid post id")
        }

        WebService.stopLive(postId!!, object : DataListener<ResponseStopLive> {
            override fun onStartCall() {

            }

            override fun onResponse(responseObj: ResponseStopLive?) {

            }

            override fun onError(errorMessage: String) {

            }
        })
    }

    // SessionListener methods =========================================================================================

    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")

        mPublisher = Publisher.Builder(this).build()
        mPublisher?.setPublisherListener(this)
        mPublisher?.cycleCamera()

        publisher_container.addView(mPublisher?.view)
        mSession?.publish(mPublisher)

        tv_counter.visibility = View.VISIBLE

        object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tv_counter.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                disconnectSession()
                finish()
            }

        }.start()
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Received")
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped")
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }


    // PublisherListener methods =======================================================================================

    override fun onStreamCreated(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated")
    }

    override fun onStreamDestroyed(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed")
    }

    override fun onError(publisherKit: PublisherKit, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.message)
    }
}
