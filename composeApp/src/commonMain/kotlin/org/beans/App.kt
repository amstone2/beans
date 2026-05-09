package org.beans

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import org.beans.ui.AddBeanDialogButton

@Composable
fun App(database: BeansAppDatabase) {
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            floatingActionButton = { AddBeanDialogButton(database) },
            floatingActionButtonPosition = FabPosition.End
        )
        { }
    }
}