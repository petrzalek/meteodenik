package cz.utb.meteodenik.ui.mesta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R

@Composable
fun MestoKarta(radek: MestoSPocasim, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = radek.mesto.nazev, style = MaterialTheme.typography.titleMedium)
            Text(
                text = radek.pocasi?.let { stringResource(R.string.teplota_format, it.teplotaC) }
                    ?: stringResource(R.string.teplota_chybi),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}