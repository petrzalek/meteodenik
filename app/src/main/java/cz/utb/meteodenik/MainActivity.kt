package cz.utb.meteodenik

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import cz.utb.meteodenik.navigace.MeteoDenikNavHost
import cz.utb.meteodenik.ui.mesta.SeznamMestObrazovka
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

private const val TAG = "Cyklus"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeteoDenikTheme {
                MeteoDenikNavHost()
            }
        }
    }
}