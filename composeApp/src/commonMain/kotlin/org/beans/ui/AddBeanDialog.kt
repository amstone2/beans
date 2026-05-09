package org.beans.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.beans.BeansAppDatabase
import org.beans.db.BeansDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBeanDialogButton(database: BeansAppDatabase) {
    val openDialog = remember { mutableStateOf(false) }

    Button(onClick = { openDialog.value = true }) { Text("Add Bean") }

    if (openDialog.value) {
        BasicAlertDialog(onDismissRequest = { openDialog.value = false })
        {
            BeansForm(database)
        }
    }
}

@Composable
private fun BeansForm(database: BeansAppDatabase) {
    var brand by remember { mutableStateOf("") }
    var product by remember { mutableStateOf("") }
    var roastLevel by remember { mutableStateOf("") }
    Column {
        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = product,
            onValueChange = { product = it },
            label = { Text("Product") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = roastLevel,
            onValueChange = { roastLevel = it },
            label = { Text("Roast level") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val beansDataSource = BeansDataSource(database)
                beansDataSource.addBean(
                    dateAdded = "2026-05-08",
                    roastedDate = null,
                    brand = brand,
                    product = product,
                    roastLevel = roastLevel.ifBlank { null }
                )
                brand = ""
                product = ""
                roastLevel = ""
            },
            enabled = brand.isNotBlank() && product.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Bean")
        }
    }
}
