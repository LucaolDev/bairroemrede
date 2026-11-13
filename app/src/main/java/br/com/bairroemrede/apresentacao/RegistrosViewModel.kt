package br.com.bairroemrede.apresentacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bairroemrede.data.Registro
import br.com.bairroemrede.data.RegistroRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class PainelUiState(
    val registros: List<Registro> = emptyList(),
    val carregando: Boolean = true,
    val falha: Boolean = false
)

sealed interface OperacaoRegistro {
    data object Ociosa : OperacaoRegistro
    data object Gravando : OperacaoRegistro
    data class Cadastrado(val id: Long) : OperacaoRegistro
    data object StatusAtualizado : OperacaoRegistro
    data class Falha(val mensagem: String) : OperacaoRegistro
}

class RegistrosViewModel(private val repository: RegistroRepository) : ViewModel() {
    private val _painel = MutableStateFlow(PainelUiState())
    val painel = _painel.asStateFlow()

    private val _operacao = MutableStateFlow<OperacaoRegistro>(OperacaoRegistro.Ociosa)
    val operacao = _operacao.asStateFlow()

    private var consulta: Job? = null

    init {
        observarRegistros()
    }

    fun observarRegistros() {
        consulta?.cancel()
        _painel.value = PainelUiState()
        consulta = viewModelScope.launch {
            repository.registros
                .catch { _painel.value = PainelUiState(carregando = false, falha = true) }
                .collect { registros ->
                    _painel.value = PainelUiState(registros, carregando = false)
                }
        }
    }

    fun cadastrar(rascunho: RegistroRascunho) {
        val registro = rascunho.criarRegistro(System.currentTimeMillis()) ?: return
        executar("O registro não foi salvo. Tente novamente.") {
            OperacaoRegistro.Cadastrado(repository.cadastrar(registro))
        }
    }

    fun avancarStatus(registro: Registro) {
        if (registro.status.proximo() == null) return
        executar("O status não foi salvo. Tente novamente.") {
            if (repository.avancarStatus(registro, System.currentTimeMillis())) {
                OperacaoRegistro.StatusAtualizado
            } else {
                OperacaoRegistro.Falha("O status já mudou. Confira o registro.")
            }
        }
    }

    fun consumirResultado() {
        _operacao.value = OperacaoRegistro.Ociosa
    }

    private fun executar(mensagemDeErro: String, gravar: suspend () -> OperacaoRegistro) {
        if (_operacao.value != OperacaoRegistro.Ociosa) return
        _operacao.value = OperacaoRegistro.Gravando
        viewModelScope.launch {
            _operacao.value = try {
                gravar()
            } catch (cancelada: CancellationException) {
                throw cancelada
            } catch (_: Exception) {
                OperacaoRegistro.Falha(mensagemDeErro)
            }
        }
    }
}
