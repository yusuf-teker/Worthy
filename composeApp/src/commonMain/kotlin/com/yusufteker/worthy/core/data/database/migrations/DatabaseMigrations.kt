package com.yusufteker.worthy.core.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {

        connection.execSQL(""" ALTER TABLE transactions ADD COLUMN originalId INTEGER NOT NULL DEFAULT 0 """.trimIndent())

        connection.execSQL(""" UPDATE transactions SET originalId = id """.trimIndent())

    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(""" ALTER TABLE Cards ADD COLUMN statementDay INTEGER DEFAULT 1 NOT NULL; """.trimIndent())
    }
}

val migrationList = listOf<Migration>(MIGRATION_2_3,MIGRATION_3_4)