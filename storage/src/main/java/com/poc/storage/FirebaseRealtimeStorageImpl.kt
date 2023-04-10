package com.poc.storage

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class FirebaseRealtimeStorageImpl<T : Any>(override var referenceDatabaseName: String) :
    StorageDAO<T> {

    private val realtimeDatabase by lazy {
        Firebase.database
    }

    private val refDatabase = realtimeDatabase.reference.child(referenceDatabaseName)

    override fun saveData(data: T) {
        if(referenceDatabaseName == "profile") {
            val hashMap: MutableMap<String, T> = java.util.HashMap()
            val idField = data::class.java.getDeclaredField("id")
            idField.isAccessible = true
            val id = idField.get(data) as String
            hashMap[id] = data
            refDatabase.child(hashMap.keys.first()).setValue(data)
        } else {
            val hashMap: MutableMap<String, T> = java.util.HashMap()
            hashMap[UUID.randomUUID().toString()] = data
            refDatabase.child(hashMap.keys.first()).setValue(data)
        }
    }

    override fun getOneData(onRecovery: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit,userId: String?) {
        refDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val genericTypeIndicator: GenericTypeIndicator<Map<String, Any>> =
                    object : GenericTypeIndicator<Map<String, Any>>() {}
                snapshot.getValue(genericTypeIndicator)?.let { onRecovery.invoke(it)}
            }
            override fun onCancelled(error: DatabaseError) {
                onFailure.invoke("Error to recovery data")
            }

        })
    }

    override fun recoveryAllData(onRecovery: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit) {
        refDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val genericTypeIndicator: GenericTypeIndicator<Map<String, Any>> =
                    object : GenericTypeIndicator<Map<String, Any>>() {}
                snapshot.getValue(genericTypeIndicator)?.let { onRecovery.invoke(it)}
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure.invoke("Error to recovery data")
            }

        })
    }
}