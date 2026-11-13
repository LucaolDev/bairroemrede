package br.com.bairroemrede.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.bairroemrede.apresentacao.OperacaoRegistro
import br.com.bairroemrede.apresentacao.RegistrosViewModel
import kotlinx.coroutines.launch

private const val INICIO = "inicio"
private const val PAINEL = "painel"
private const val CADASTRO = "cadastro"
private const val DETALHE = "detalhe/{id}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BairroEmRedeApp(viewModel: RegistrosViewModel) {
    val controlador = rememberNavController()
    val entrada by controlador.currentBackStackEntryAsState()
    val rota = entrada?.destination?.route ?: INICIO
    val painel by viewModel.painel.collectAsStateWithLifecycle()
    val operacao by viewModel.operacao.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val escopo = rememberCoroutineScope()
    val gravando = operacao == OperacaoRegistro.Gravando

    LaunchedEffect(operacao) {
        val mensagem = when (val resultado = operacao) {
            is OperacaoRegistro.Cadastrado -> {
                if (controlador.currentDestination?.route == CADASTRO) {
                    controlador.navigate("detalhe/" + resultado.id) {
                        popUpTo(CADASTRO) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                "Registro salvo."
            }
            OperacaoRegistro.StatusAtualizado -> "Status atualizado."
            is OperacaoRegistro.Falha -> resultado.mensagem
            else -> return@LaunchedEffect
        }
        viewModel.consumirResultado()
        escopo.launch {
            snackbar.currentSnackbarData?.dismiss()
            snackbar.showSnackbar(mensagem)
        }
    }

    val titulo = when (rota) {
        PAINEL -> "Painel do bairro"
        CADASTRO -> "Novo registro"
        DETALHE -> "Detalhe do registro"
        else -> "Bairro em Rede"
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(titulo, modifier = Modifier.semantics { heading() }, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    if (rota != INICIO) {
                        IconButton(onClick = { controlador.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { espaco ->
        val tela = Modifier.fillMaxSize()
        NavHost(
            navController = controlador,
            startDestination = INICIO,
            modifier = Modifier.padding(espaco).fillMaxSize()
        ) {
            composable(INICIO) {
                InicioScreen(
                    painel,
                    onPainel = { controlador.navigate(PAINEL) { launchSingleTop = true } },
                    onCadastrar = { controlador.navigate(CADASTRO) { launchSingleTop = true } },
                    onRecarregar = viewModel::observarRegistros,
                    modifier = tela
                )
            }
            composable(PAINEL) {
                PainelScreen(
                    painel,
                    onCadastrar = { controlador.navigate(CADASTRO) { launchSingleTop = true } },
                    onAbrir = { controlador.navigate("detalhe/$it") { launchSingleTop = true } },
                    onRecarregar = viewModel::observarRegistros,
                    modifier = tela
                )
            }
            composable(CADASTRO) {
                CadastroScreen(gravando, viewModel::cadastrar, tela)
            }
            composable(DETALHE, arguments = listOf(navArgument("id") { type = NavType.LongType })) { item ->
                DetalheScreen(
                    painel = painel,
                    id = item.arguments?.getLong("id") ?: 0L,
                    gravando = gravando,
                    onAvancar = viewModel::avancarStatus,
                    onVoltar = { controlador.popBackStack() },
                    onRecarregar = viewModel::observarRegistros,
                    modifier = tela
                )
            }
        }
    }
}
