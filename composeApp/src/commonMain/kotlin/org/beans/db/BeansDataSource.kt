package org.beans.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import org.beans.BeansAppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class BeansDataSource(db: BeansAppDatabase) {

    private val beans = db.beansQueries

    fun addBean(dateAdded: String,
               roastedDate: String?,
               brand: String,
               product: String,
               roastLevel: String?
    ) {
        beans.insert(
            dateAdded = dateAdded,
            roastedDate = roastedDate,
            brand = brand,
            product = product,
            roastLevel = roastLevel,
        )
    }

    fun getaAllBeans() = beans.selectAll().asFlow().mapToList(Dispatchers.IO)

    fun updateBean(beanId: Long,
                   dateAdded: String,
                roastedDate: String?,
                brand: String,
                product: String,
                roastLevel: String
    ) {
        beans.update(
            beanId = beanId,
            dateAdded = dateAdded,
            roastedDate = roastedDate,
            brand = brand,
            product = product,
            roastLevel = roastLevel
        )
    }

    fun deleteBean(beanId: Long) =  beans.delete(beanId = beanId)

}