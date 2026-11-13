package br.com.bairroemrede.apresentacao

import br.com.bairroemrede.data.Categoria
import br.com.bairroemrede.data.Registro
import br.com.bairroemrede.data.TipoRegistro

data class RegistroRascunho(
    val titulo: String = "",
    val tipo: TipoRegistro = TipoRegistro.PEDIDO,
    val categoria: Categoria = Categoria.AGUA,
    val local: String = "",
    val quantidade: String = "1",
    val descricao: String = ""
) {
    fun validar(): ErrosCadastro = ErrosCadastro(
        titulo = when (titulo.trim().length) {
            in 3..60 -> null
            else -> "Informe um título de 3 a 60 caracteres."
        },
        local = when (local.trim().length) {
            in 3..80 -> null
            else -> "Informe um ponto de referência de 3 a 80 caracteres."
        },
        quantidade = if (quantidade.toIntOrNull()?.let { it in 1..999 } == true) {
            null
        } else {
            "Informe uma quantidade de 1 a 999."
        },
        descricao = if (descricao.trim().length <= 240) {
            null
        } else {
            "Use até 240 caracteres."
        }
    )

    fun criarRegistro(agora: Long): Registro? {
        if (validar().temErros) return null
        val quantidadeValida = quantidade.toIntOrNull() ?: return null
        return Registro(
            titulo = titulo.trim(),
            tipo = tipo,
            categoria = categoria,
            local = local.trim(),
            quantidade = quantidadeValida,
            descricao = descricao.trim(),
            criadoEm = agora
        )
    }
}

data class ErrosCadastro(
    val titulo: String? = null,
    val local: String? = null,
    val quantidade: String? = null,
    val descricao: String? = null
) {
    val temErros: Boolean
        get() = listOf(titulo, local, quantidade, descricao).any { it != null }
}
