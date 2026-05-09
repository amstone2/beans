package org.beans.db

import org.beans.BeansAppDatabase

fun createDatabase(factory: DatabaseDriverFactory): BeansAppDatabase =
    BeansAppDatabase(factory.createDriver())
