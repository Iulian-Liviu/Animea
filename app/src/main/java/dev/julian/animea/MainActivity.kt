package dev.julian.animea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.julian.animea.network.WaifuServiceInstance
import dev.julian.animea.ui.theme.AnimeaTheme
import dev.julian.animea.ui.views.RandomImageRepository
import dev.julian.animea.ui.views.WaifusScreen
import dev.julian.animea.ui.views.WaifusScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContent {
            AnimeaTheme {
                Scaffold{ padding ->
                    WaifusScreen(modifier = Modifier.padding(padding), viewModel = viewModel())
                }
            }
        }
    }
}
