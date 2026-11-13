package br.com.bairroemrede.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.bairroemrede.data.Registro
import br.com.bairroemrede.data.StatusRegistro
import br.com.bairroemrede.data.TipoRegistro

@Composable
internal fun TipoERotulo(tipo: TipoRegistro, categoria: String) {
    val cores = MaterialTheme.colorScheme
    Text(
        text = tipo.rotulo + " · " + categoria,
        style = MaterialTheme.typography.labelMedium,
        color = if (tipo == TipoRegistro.PEDIDO) cores.tertiary else cores.primary,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
internal fun StatusDoRegistro(status: StatusRegistro) {
    val cores = MaterialTheme.colorScheme
    val (fundo, texto) = when (status) {
        StatusRegistro.ABERTO -> cores.tertiaryContainer to cores.onTertiaryContainer
        StatusRegistro.EM_ATENDIMENTO -> cores.secondaryContainer to cores.onSecondaryContainer
        StatusRegistro.ATENDIDO -> cores.primaryContainer to cores.onPrimaryContainer
    }
    Surface(shape = CircleShape, color = fundo, contentColor = texto) {
        Text(
            status.rotulo,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun CartaoRegistro(registro: Registro, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TipoERotulo(registro.tipo, registro.categoria.rotulo)
            Text(registro.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    registro.local,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            StatusDoRegistro(registro.status)
        }
    }
}

@Composable
internal fun EstadoSemDados(titulo: String, mensagem: String, acao: String? = null, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 26.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(titulo, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(
            mensagem,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (acao != null) OutlinedButton(onClick = onClick) { Text(acao) }
    }
}

@Composable
internal fun CarregandoRegistros() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
