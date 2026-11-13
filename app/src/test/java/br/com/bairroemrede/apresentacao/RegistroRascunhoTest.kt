package br.com.bairroemrede.apresentacao

import br.com.bairroemrede.data.Categoria
import br.com.bairroemrede.data.StatusRegistro
import br.com.bairroemrede.data.TipoRegistro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RegistroRascunhoTest {
    private val preenchido = RegistroRascunho(
        titulo = "  Água no abrigo  ",
        tipo = TipoRegistro.OFERTA,
        categoria = Categoria.AGUA,
        local = "  Centro comunitário  ",
        quantidade = "12",
        descricao = "  Garrafas fechadas  "
    )

    @Test
    fun rejeitaCamposObrigatoriosEmBranco() {
        val rascunho = preenchido.copy(titulo = " ", local = "")
        assertNotNull(rascunho.validar().titulo)
        assertNotNull(rascunho.validar().local)
        assertNull(rascunho.criarRegistro(100))
    }

    @Test
    fun aceitaSomenteQuantidadesInteirasEntreUmENovecentosENoventaENove() {
        listOf("", "0", "-1", "1000", "1.5", "muitas", "2147483648").forEach { quantidade ->
            assertNotNull(preenchido.copy(quantidade = quantidade).validar().quantidade)
        }
        assertNull(preenchido.copy(quantidade = "1").validar().quantidade)
        assertNull(preenchido.copy(quantidade = "999").validar().quantidade)
    }

    @Test
    fun limitaTituloLocalEObservacoes() {
        val erros = preenchido.copy(
            titulo = "a".repeat(61),
            local = "a".repeat(81),
            descricao = "a".repeat(241)
        ).validar()
        assertNotNull(erros.titulo)
        assertNotNull(erros.local)
        assertNotNull(erros.descricao)
    }

    @Test
    fun criaRegistroAbertoComOsDadosNormalizados() {
        assertFalse(preenchido.validar().temErros)
        val registro = requireNotNull(preenchido.criarRegistro(100))
        assertEquals("Água no abrigo", registro.titulo)
        assertEquals("Centro comunitário", registro.local)
        assertEquals("Garrafas fechadas", registro.descricao)
        assertEquals(12, registro.quantidade)
        assertEquals(TipoRegistro.OFERTA, registro.tipo)
        assertEquals(Categoria.AGUA, registro.categoria)
        assertEquals(StatusRegistro.ABERTO, registro.status)
        assertEquals(100L, registro.criadoEm)
        assertEquals(100L, registro.atualizadoEm)
    }

    @Test
    fun percorreOsEstadosSemPularAtendimento() {
        assertEquals(StatusRegistro.EM_ATENDIMENTO, StatusRegistro.ABERTO.proximo())
        assertEquals(StatusRegistro.ATENDIDO, StatusRegistro.EM_ATENDIMENTO.proximo())
        assertNull(StatusRegistro.ATENDIDO.proximo())
    }
}
