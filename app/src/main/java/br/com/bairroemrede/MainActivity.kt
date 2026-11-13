package br.com.bairroemrede

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.bairroemrede.apresentacao.RegistrosViewModel
import br.com.bairroemrede.ui.BairroEmRedeApp
import br.com.bairroemrede.ui.BairroEmRedeTheme

class MainActivity : ComponentActivity() {
    private val viewModel: RegistrosViewModel by viewModels {
        viewModelFactory {
            initializer { RegistrosViewModel((application as BairroEmRedeApplication).registros) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BairroEmRedeTheme {
                BairroEmRedeApp(viewModel)
            }
        }
    }
}
