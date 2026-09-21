package com.example.nri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.nri.ui.theme.NRITheme
import androidx.compose.foundation.layout.Box
import com.example.nri.ui.bag.BagScreen
import com.example.nri.ui.cards.CardArchiveScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NRITheme {
                NRIApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun NRIApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.CHARACTER) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                when (currentDestination) {
                    AppDestinations.BAG       -> BagScreen()
                    AppDestinations.CHARACTER -> Greeting("Android")
                    AppDestinations.TABLET    -> Greeting("Планшет")
                    AppDestinations.CARDS     -> CardArchiveScreen()
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    TABLET("Планшет", R.drawable.ic_tablet),
    CHARACTER("Персонаж", R.drawable.ic_account_box),
    BAG("Рюкзак", R.drawable.ic_list),
    CARDS("Архив карт", R.drawable.ic_card_archive)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NRITheme {
        Greeting("Android")
    }
}