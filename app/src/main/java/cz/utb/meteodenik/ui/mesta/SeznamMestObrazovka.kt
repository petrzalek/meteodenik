package cz.utb.meteodenik.ui.mesta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.utb.meteodenik.R

@Composable
fun SeznamMestObrazovka(
    onMestoKliknuto: (Int) -> Unit,
    viewModel: MestaViewModel = viewModel()
) {
    val stav by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.mesta_titulek),   // váš klíč nadpisu
                    style = MaterialTheme.typography.headlineSmall
                )
                // Gesto uživatele = vynucená obnova, cache Repository se obejde.
                TextButton(onClick = { viewModel.obnov(vynutit = true) }) {
                    Text(stringResource(R.string.akce_obnovit))
                }
            }
            SeznamMestObsah(
                stav = stav,
                onZkusitZnovu = { viewModel.obnov(vynutit = true) },
                onMestoKliknuto = { id ->
                    viewModel.vyberMesto(id)
                    onMestoKliknuto(id)              // navigace na detail z lekce 08
                }
            )
        }
    }
}