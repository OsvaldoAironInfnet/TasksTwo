package com.poc.firstprojectinfnet.home.data

data class Task(
    val title: String = "",
    val description: String = "",
    val id: String = "",
    val favorite: Boolean,
    val date: String = "",
    val hour: String = "",
    val crit: String = "",
    val validationId: String = ""
) : java.io.Serializable