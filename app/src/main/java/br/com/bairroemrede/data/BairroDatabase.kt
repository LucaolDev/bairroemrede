package br.com.bairroemrede.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Registro::class], version = 1)
abstract class BairroDatabase : RoomDatabase() {
    abstract fun registroDao(): RegistroDao
}
