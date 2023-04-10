package com.poc.commom.base.tracker

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase


class TrackerLogEventImpl : TrackerLogEvent {

    private val firebaseTracker by lazy {
        Firebase.analytics
    }


    override fun trackEventClick(event: String, category: String, eventLabel: String) {
        firebaseTracker.logEvent(category) {
            param(FirebaseAnalytics.Param.ITEM_NAME, event)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, eventLabel)
        }

        firebaseTracker.logEvent(category, Bundle().apply {
            this.putString("EventName", event)
            this.putString("EventLabel", eventLabel)
        })
    }
}