
package cz.utb.meteodenik.ui.mesta

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

@Composable
fun MestoKarta(
    mesto: Mesto,
    modifier: Modifier = Modifier,
    vybrane: Boolean = false
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = if (vybrane) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                    .semantics {
                        contentDescription = mesto.nazev
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mesto.nazev.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mesto.nazev,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = if (mesto.teplotaC != null) {
                        stringResource(
                            R.string.mesto_teplota,
                            mesto.teplotaC
                        )
                    } else {
                        stringResource(
                            R.string.mesto_teplota_neznama
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

internal val ukazkovaMesta = listOf(
    Mesto(id = 1, nazev = "Zlín", teplotaC = 18.5),
    Mesto(id = 2, nazev = "Brno", teplotaC = 21.0),
    Mesto(id = 3, nazev = "Uherské Hradiště", teplotaC = null)
)

@Preview(name = "Karta – světlá", showBackground = true)
@Preview(
    name = "Karta – tmavá",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MestoKartaPreview() {
    MeteoDenikTheme {
        MestoKarta(
            mesto = ukazkovaMesta.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PridatMestoRadek(
    nazev: String,
    onNazevChange: (String) -> Unit,
    onPridat: () -> Unit,
    jeChyba: Boolean = false,
    modifier: Modifier = Modifier
) {
    val popisChyby = stringResource(R.string.mesto_duplikat)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = nazev,
            onValueChange = onNazevChange,
            label = {
                Text(stringResource(R.string.mesto_pole_label))
            },
            singleLine = true,
            isError = jeChyba,
            supportingText = {
                if (jeChyba) {
                    Text(text = popisChyby)
                }
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = onPridat,
            enabled = nazev.isNotBlank()
        ) {
            Text(
                text = stringResource(R.string.mesto_pridat)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PridatMestoRadekPreview() {
    MeteoDenikTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            PridatMestoRadek(
                nazev = "",
                onNazevChange = {},
                onPridat = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            PridatMestoRadek(
                nazev = "Zlín",
                onNazevChange = {},
                onPridat = {},
                jeChyba = true
            )
        }
    }
}

@Composable
fun MestaSeznam(
    mesta: List<Mesto>,
    vybraneMestoId: Int?,
    onMestoKliknuto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (mesta.isEmpty()) {
        Text(
            text = stringResource(R.string.mesta_prazdno),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 32.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = mesta,
                key = { mesto -> mesto.id }
            ) { mesto ->
                MestoKarta(
                    mesto = mesto,
                    vybrane = mesto.id == vybraneMestoId,
                    modifier = Modifier.clickable {
                        onMestoKliknuto(mesto.id)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeznamMestObsah(
    stav: MestaUiState,
    onMestoKliknuto: (Int) -> Unit,
    onPridejMesto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var rozepsanyNazev by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.mesta_titulek)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            PridatMestoRadek(
                nazev = rozepsanyNazev,
                onNazevChange = {
                    rozepsanyNazev = it
                },
                onPridat = {
                    onPridejMesto(rozepsanyNazev)
                    rozepsanyNazev = ""
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = pluralStringResource(
                    R.plurals.mesta_pocet,
                    stav.mesta.size,
                    stav.mesta.size
                ),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            MestaSeznam(
                mesta = stav.mesta,
                vybraneMestoId = stav.vybraneMestoId,
                onMestoKliknuto = onMestoKliknuto
            )
        }
    }
}

@Composable
fun SeznamMestObrazovka(
    onMestoKliknuto: (Int) -> Unit,
    viewModel: MestaViewModel = viewModel()
) {
    val stav by viewModel.uiState.collectAsStateWithLifecycle()

    SeznamMestObsah(
        stav = stav,
        onMestoKliknuto = { id ->
            viewModel.vyberMesto(id)
            onMestoKliknuto(id)
        },
        onPridejMesto = viewModel::pridejMesto
    )
}

@Preview(
    name = "Obrazovka – světlá",
    showBackground = true
)
@Composable
private fun SeznamMestObsahPreview() {
    MeteoDenikTheme {
        SeznamMestObsah(
            stav = MestaUiState(
                mesta = ukazkovaMesta,
                vybraneMestoId = 2
            ),
            onMestoKliknuto = {},
            onPridejMesto = {}
        )
    }
}

@Preview(
    name = "Obrazovka – tmavá",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SeznamMestObsahTmavaPreview() {
    MeteoDenikTheme {
        SeznamMestObsah(
            stav = MestaUiState(
                mesta = ukazkovaMesta,
                vybraneMestoId = 2
            ),
            onMestoKliknuto = {},
            onPridejMesto = {}
        )
    }
}

@Preview(
    name = "Obrazovka – prázdný seznam",
    showBackground = true
)
@Composable
private fun SeznamMestObsahPrazdnyPreview() {
    MeteoDenikTheme {
        SeznamMestObsah(
            stav = MestaUiState(
                mesta = emptyList(),
                vybraneMestoId = null
            ),
            onMestoKliknuto = {},
            onPridejMesto = {}
        )
    }
}
