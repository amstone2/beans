package org.beans

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.beans.db.DatabaseDriverFactory
import org.beans.db.createDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val database = createDatabase(DatabaseDriverFactory(applicationContext))
        setContent {
            App(database)
        }
    }
}