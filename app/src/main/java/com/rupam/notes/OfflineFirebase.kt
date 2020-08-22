package com.rupam.notes

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class OfflineFirebase : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}