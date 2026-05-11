package org.beans

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import org.beans.ui.AddBeanDialogButton
import org.beans.ui.BeansList

@Composable
fun App(beansAppDatabase: BeansAppDatabase) {
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            floatingActionButton = { AddBeanDialogButton(beansAppDatabase) },
            floatingActionButtonPosition = FabPosition.EndOverlay
        ) { innerPadding ->
            BeansList(beansAppDatabase, innerPadding)
        }
    }
}
