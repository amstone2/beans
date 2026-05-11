package org.beans.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.beans.BeansAppDatabase
import org.beans.db.BeansDataSource

@Composable
fun BeansList(database: BeansAppDatabase, innerPadding: PaddingValues) {
    val dataSource = remember(database) { BeansDataSource(database) }
    val beans by remember(dataSource) { dataSource.getaAllBeans() }
        .collectAsState(initial = emptyList())

    if (beans.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("No beans yet — tap + to add one")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(items = beans, key = { it.beanId }) { bean ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "${bean.brand} – ${bean.product}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        bean.roastLevel?.let {
                            Text("Roast: $it", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            "Added: ${bean.dateAdded}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}