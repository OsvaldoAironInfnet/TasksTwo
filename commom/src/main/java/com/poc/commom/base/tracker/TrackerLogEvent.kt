package com.poc.commom.base.tracker

interface TrackerLogEvent {
    fun trackEventClick(event: String, category: String, eventLabel: String)
}