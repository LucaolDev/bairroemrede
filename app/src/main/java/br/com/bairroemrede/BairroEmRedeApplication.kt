package br.com.bairroemrede

import android.app.Application
import androidx.room.Room
import br.com.bairroemrede.data.BairroDatabase
import br.com.bairroemrede.data.RegistroRepository

class BairroEmRedeApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(this, BairroDatabase::class.java, "bairro-em-rede.db").build()
    }

    val registros by lazy { RegistroRepository(database.registroDao()) }
}
