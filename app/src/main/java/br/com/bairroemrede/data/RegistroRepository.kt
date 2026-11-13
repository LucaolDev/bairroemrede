package br.com.bairroemrede.data

class RegistroRepository(private val dao: RegistroDao) {
    val registros = dao.observarTodos()

    suspend fun cadastrar(registro: Registro): Long = dao.inserir(registro)

    suspend fun avancarStatus(registro: Registro, agora: Long): Boolean {
        val proximo = registro.status.proximo() ?: return false
        return dao.atualizarStatus(registro.id, registro.status, proximo, agora) == 1
    }
}
