
package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme
import androidx.compose.ui.unit.Density

@Composable
fun VizitkaKarta(
    jmeno: String,
    obor: String,
    email: String,
    modifier: Modifier = Modifier
) {
    val avatarPopis = stringResource(
        R.string.vizitka_avatar_popis,
        jmeno
    )

    val emailPopis = stringResource(
        R.string.vizitka_email_popis
    )

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    )
                    .semantics {
                        contentDescription = avatarPopis
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = jmeno
                        .trim()
                        .split("\\s+".toRegex())
                        .take(2)
                        .mapNotNull {
                            it.firstOrNull()?.uppercase()
                        }
                        .joinToString(""),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = jmeno,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = obor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics {
                        contentDescription = "$emailPopis: $email"
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Vizitka - světlá",
    showBackground = true
)
@Composable
private fun VizitkaKartaSvetlaPreview() {
    MeteoDenikTheme {
        VizitkaKarta(
            jmeno = "Jan Novák",
            obor = "Informační technologie",
            email = "jan.novak@example.com",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "Vizitka - tmavá",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VizitkaKartaTmavaPreview() {
    MeteoDenikTheme {
        VizitkaKarta(
            jmeno = "Jan Novák",
            obor = "Informační technologie",
            email = "jan.novak@example.com",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "Vizitka - velké písmo",
    showBackground = true,
    widthDp = 320
)
@Composable
private fun VizitkaKartaVelkePismoPreview() {
    MeteoDenikTheme {
        val density = LocalDensity.current

        CompositionLocalProvider(
            LocalDensity provides Density(
                density = density.density,
                fontScale = 1.5f
            )
        ) {
            VizitkaKarta(
                jmeno = "Jan Novák",
                obor = "Informační technologie a počítačové systémy",
                email = "jan.novak@example.com",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}