package br.com.bairroemrede.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {
    @Query("SELECT * FROM registros ORDER BY criadoEm DESC, id DESC")
    fun observarTodos(): Flow<List<Registro>>

    @Insert
    suspend fun inserir(registro: Registro): Long

    @Query(
        """
        UPDATE registros
        SET status = :novoStatus, atualizadoEm = :atualizadoEm
        WHERE id = :id AND status = :statusAnterior
        """
    )
    suspend fun atualizarStatus(
        id: Long,
        statusAnterior: StatusRegistro,
        novoStatus: StatusRegistro,
        atualizadoEm: Long
    ): Int
}
