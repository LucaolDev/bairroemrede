package br.com.bairroemrede.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.bairroemrede.apresentacao.ErrosCadastro
import br.com.bairroemrede.apresentacao.RegistroRascunho
import br.com.bairroemrede.data.Categoria
import br.com.bairroemrede.data.TipoRegistro

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CadastroScreen(
    gravando: Boolean,
    onSalvar: (RegistroRascunho) -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf(TipoRegistro.PEDIDO) }
    var categoria by rememberSaveable { mutableStateOf(Categoria.AGUA) }
    var local by rememberSaveable { mutableStateOf("") }
    var quantidade by rememberSaveable { mutableStateOf("1") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var tentouSalvar by rememberSaveable { mutableStateOf(false) }

    val foco = LocalFocusManager.current
    val rascunho = RegistroRascunho(titulo, tipo, categoria, local, quantidade, descricao)
    val erros = if (tentouSalvar) rascunho.validar() else ErrosCadastro()

    Column(
        modifier = modifier.imePadding().verticalScroll(rememberScrollState())
            .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Text(
            "Use um ponto de referência público. Os campos com * são obrigatórios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("Tipo de registro", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoRegistro.entries.forEach { opcao ->
                    FilterChip(selected = tipo == opcao, enabled = !gravando, onClick = { tipo = opcao }, label = { Text(opcao.rotulo) })
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("Categoria", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Categoria.entries.forEach { opcao ->
                    FilterChip(selected = categoria == opcao, enabled = !gravando, onClick = { categoria = opcao }, label = { Text(opcao.rotulo) })
                }
            }
        }

        Campo(
            valor = titulo, onValueChange = { titulo = it }, rotulo = "Título *", erro = erros.titulo,
            habilitado = !gravando, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Campo(
            valor = local, onValueChange = { local = it }, rotulo = "Ponto de referência *", erro = erros.local,
            habilitado = !gravando, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Campo(
            valor = quantidade, onValueChange = { quantidade = it }, rotulo = "Quantidade *", erro = erros.quantidade,
            habilitado = !gravando, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Campo(
            valor = descricao, onValueChange = { descricao = it }, rotulo = "Observações", erro = erros.descricao,
            habilitado = !gravando, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            multilinha = true
        )

        Button(
            onClick = {
                tentouSalvar = true
                if (!rascunho.validar().temErros) {
                    foco.clearFocus()
                    onSalvar(rascunho)
                }
            },
            enabled = !gravando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (gravando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Salvar registro")
            }
        }
    }
}

@Composable
private fun Campo(
    valor: String,
    onValueChange: (String) -> Unit,
    rotulo: String,
    erro: String?,
    habilitado: Boolean,
    keyboardOptions: KeyboardOptions,
    multilinha: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(rotulo) },
        modifier = Modifier.fillMaxWidth(),
        enabled = habilitado,
        singleLine = !multilinha,
        minLines = if (multilinha) 2 else 1,
        maxLines = if (multilinha) 4 else 1,
        isError = erro != null,
        supportingText = erro?.let { { Text(it) } },
        keyboardOptions = keyboardOptions
    )
}
