package br.com.bairroemrede.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.bairroemrede.apresentacao.PainelUiState
import br.com.bairroemrede.data.TipoRegistro

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PainelScreen(
    painel: PainelUiState,
    onCadastrar: () -> Unit,
    onAbrir: (Long) -> Unit,
    onRecarregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var filtro by rememberSaveable { mutableStateOf<TipoRegistro?>(null) }
    val visiveis = painel.registros.filter { filtro == null || it.tipo == filtro }
    val contagem = if (visiveis.size == 1) "1 registro" else "%d registros".format(visiveis.size)

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Pedidos e ofertas do bairro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Toque em um registro para consultar o local, a quantidade e o status.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = onCadastrar) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Novo registro")
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(selected = filtro == null, onClick = { filtro = null }, label = { Text("Todos") })
                    FilterChip(selected = filtro == TipoRegistro.PEDIDO, onClick = { filtro = TipoRegistro.PEDIDO }, label = { Text("Pedidos") })
                    FilterChip(selected = filtro == TipoRegistro.OFERTA, onClick = { filtro = TipoRegistro.OFERTA }, label = { Text("Ofertas") })
                }
                if (!painel.carregando && !painel.falha) {
                    Text(contagem, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        when {
            painel.falha -> item {
                EstadoSemDados("Não foi possível abrir o painel", "Tente ler os registros novamente.", "Tentar novamente", onRecarregar)
            }
            painel.carregando -> item { CarregandoRegistros() }
            painel.registros.isEmpty() -> item {
                EstadoSemDados("Nenhum registro por enquanto", "Cadastre o primeiro pedido de ajuda ou oferta de recurso.")
            }
            visiveis.isEmpty() -> item {
                EstadoSemDados("Nenhum registro neste filtro", "Selecione Todos para ver o painel completo.", "Ver todos") { filtro = null }
            }
            else -> items(visiveis, key = { it.id }) { registro ->
                CartaoRegistro(registro) { onAbrir(registro.id) }
            }
        }
    }
}
