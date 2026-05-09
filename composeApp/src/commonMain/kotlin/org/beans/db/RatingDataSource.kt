package org.notes.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import org.beans.BeansAppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class RatingDataSource(db: BeansAppDatabase) {

    private val notes = db.notesQueries

    fun addNote(beanId: Long,
                tag: String
    ) {
        notes.insert(
            beanId = beanId,
            tag = tag,
        )
    }

    fun selectByBeanId(beanId: Long) = notes.selectByBeanId(beanId).asFlow().mapToList(Dispatchers.IO)

    fun deleteByNoteId(noteId: Long) =  notes.deleteByNoteId(noteId)

    fun deleteByBeanId(noteId: Long) =  notes.deleteByBeanId(noteId)
}