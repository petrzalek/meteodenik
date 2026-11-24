package cz.utb.meteodenik.ui.mesta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R

@Composable
fun SeznamMestObsah(
    stav: MestaUiState,
    onZkusitZnovu: () -> Unit,
    onMestoKliknuto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (stav.nacitaSe) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        val duvod = stav.chyba
        if (duvod != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(duvod.textProUzivatele()),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = onZkusitZnovu) {
                    Text(stringResource(R.string.akce_zkusit_znovu))
                }
            }
        }
        LazyColumn {
            items(stav.mesta, key = { it.mesto.id }) { radek ->
                MestoKarta(
                    radek = radek,
                    modifier = Modifier.clickable { onMestoKliknuto(radek.mesto.id) }
                )
            }
        }
    }
}