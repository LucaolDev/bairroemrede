package br.com.bairroemrede.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoRegistro(val rotulo: String) {
    PEDIDO("Pedido de ajuda"),
    OFERTA("Oferta de recurso")
}

enum class Categoria(val rotulo: String) {
    AGUA("Água"),
    ALIMENTOS("Alimentos"),
    ABRIGO("Abrigo"),
    TRANSPORTE("Transporte"),
    HIGIENE("Higiene"),
    OUTROS("Outros")
}

enum class StatusRegistro(val rotulo: String) {
    ABERTO("Em aberto"),
    EM_ATENDIMENTO("Em atendimento"),
    ATENDIDO("Atendido");

    fun proximo(): StatusRegistro? = when (this) {
        ABERTO -> EM_ATENDIMENTO
        EM_ATENDIMENTO -> ATENDIDO
        ATENDIDO -> null
    }
}

@Entity(tableName = "registros")
data class Registro(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val tipo: TipoRegistro,
    val categoria: Categoria,
    val local: String,
    val quantidade: Int,
    val descricao: String,
    val status: StatusRegistro = StatusRegistro.ABERTO,
    val criadoEm: Long,
    val atualizadoEm: Long = criadoEm
)
