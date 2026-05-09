package org.beans

import androidx.compose.ui.window.ComposeUIViewController
import org.beans.db.DatabaseDriverFactory
import org.beans.db.createDatabase

fun MainViewController() = ComposeUIViewController {
    App(createDatabase(DatabaseDriverFactory()))
}
