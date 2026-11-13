package br.com.bairroemrede.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.bairroemrede.R
import br.com.bairroemrede.apresentacao.PainelUiState
import br.com.bairroemrede.data.StatusRegistro

@Composable
internal fun InicioScreen(
    painel: PainelUiState,
    onPainel: () -> Unit,
    onCadastrar: () -> Unit,
    onRecarregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cores = MaterialTheme.colorScheme
    val registros = painel.registros

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF143D31),
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_bairro_em_rede),
                        contentDescription = null,
                        modifier = Modifier.size(46.dp)
                    )
                    Text(
                        "Apoio em emergências",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Organize pedidos de ajuda e ofertas de recursos com moradores e voluntários do bairro.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFD6EAE0)
                    )
                    Button(
                        onClick = onCadastrar,
                        modifier = Modifier.padding(top = 3.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cores.primaryContainer,
                            contentColor = cores.onPrimaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                        Text("Novo registro")
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Resumo local",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onPainel) { Text("Ver registros") }
                }
                when {
                    painel.falha -> EstadoSemDados(
                        "Resumo indisponível",
                        "Houve uma falha ao ler os registros.",
                        "Tentar novamente",
                        onRecarregar
                    )
                    painel.carregando -> CarregandoRegistros()
                    else -> Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Resumo(StatusRegistro.ABERTO.rotulo, registros.count { it.status == StatusRegistro.ABERTO }, Modifier.weight(1f))
                        Resumo(StatusRegistro.EM_ATENDIMENTO.rotulo, registros.count { it.status == StatusRegistro.EM_ATENDIMENTO }, Modifier.weight(1f))
                        Resumo(StatusRegistro.ATENDIDO.rotulo, registros.count { it.status == StatusRegistro.ATENDIDO }, Modifier.weight(1f))
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(18.dp), color = cores.tertiaryContainer, contentColor = cores.onTertiaryContainer) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Outlined.Warning, contentDescription = null)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Em caso de risco imediato", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("Procure os canais oficiais de emergência. Os registros ficam neste aparelho.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun Resumo(rotulo: String, quantidade: Int, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surfaceContainer) {
        Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 11.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    quantidade.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(rotulo, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
