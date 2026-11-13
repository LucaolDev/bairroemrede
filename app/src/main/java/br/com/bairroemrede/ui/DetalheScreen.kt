package br.com.bairroemrede.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.bairroemrede.apresentacao.PainelUiState
import br.com.bairroemrede.data.Registro
import br.com.bairroemrede.data.StatusRegistro
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun DetalheScreen(
    painel: PainelUiState,
    id: Long,
    gravando: Boolean,
    onAvancar: (Registro) -> Unit,
    onVoltar: () -> Unit,
    onRecarregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val registro = painel.registros.find { it.id == id }
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
            .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        when {
            painel.falha -> EstadoSemDados(
                "Detalhe indisponível", "Não foi possível ler o banco de dados.", "Tentar novamente", onRecarregar
            )
            painel.carregando -> CarregandoRegistros()
            registro == null -> EstadoSemDados(
                "Registro não encontrado", "Volte para consultar os registros disponíveis.", "Voltar", onVoltar
            )
            else -> DetalheRegistro(registro, gravando, onAvancar)
        }
    }
}

@Composable
private fun DetalheRegistro(registro: Registro, gravando: Boolean, onAvancar: (Registro) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TipoERotulo(registro.tipo, registro.categoria.rotulo)
            Text(registro.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            StatusDoRegistro(registro.status)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Informacao("Ponto de referência", registro.local)
            Informacao("Quantidade", registro.quantidade.toString())
            if (registro.descricao.isNotEmpty()) Informacao("Observações", registro.descricao)
            Informacao("Última atualização", formatarData(registro.atualizadoEm))
        }
    }

    val acao = when (registro.status) {
        StatusRegistro.ABERTO -> "Iniciar atendimento"
        StatusRegistro.EM_ATENDIMENTO -> "Marcar como atendido"
        StatusRegistro.ATENDIDO -> null
    }
    if (acao != null) {
        Button(
            onClick = { onAvancar(registro) },
            enabled = !gravando,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (gravando) "Salvando…" else acao) }
    } else {
        Text("Atendimento concluído.", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Informacao(rotulo: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(rotulo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun formatarData(millis: Long): String =
    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.forLanguageTag("pt-BR"))
        .format(Date(millis))
