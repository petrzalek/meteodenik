package cz.utb.meteodenik.ui.theme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cz.utb.meteodenik.R

private const val OPRAVNENI_POLOHA = Manifest.permission.ACCESS_COARSE_LOCATION

enum class StavOpravneni { NEZNAMY, ODMITNUTO, ODMITNUTO_NATRVALO, UDELENO }

@Composable
fun PolohaKarta(onPolohaPovolena: () -> Unit) {

    val context = LocalContext.current
    val activity = LocalActivity.current

    var stav by remember {
        mutableStateOf(
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    OPRAVNENI_POLOHA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                StavOpravneni.UDELENO
            } else {
                StavOpravneni.NEZNAMY
            }
        )
    }

    var zobrazitVysvetleni by remember {
        mutableStateOf(false)
    }

    var souradnice by remember {
        mutableStateOf<Location?>(null)
    }

    val aktualniStav by rememberUpdatedState(stav)
    val aktualniOnPolohaPovolena by rememberUpdatedState(onPolohaPovolena)

    // Načtení poslední známé polohy.
    fun nacistPolohu() {
        if (
            ContextCompat.checkSelfPermission(
                context,
                OPRAVNENI_POLOHA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        try {
            val locationManager =
                context.getSystemService(LocationManager::class.java)

            souradnice = locationManager.getLastKnownLocation(
                LocationManager.NETWORK_PROVIDER
            )

        } catch (e: SecurityException) {
            souradnice = null
        }
    }

    val spoustec = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { povoleno ->

        stav = when {
            povoleno -> StavOpravneni.UDELENO

            activity != null &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        OPRAVNENI_POLOHA
                    ) -> StavOpravneni.ODMITNUTO

            else -> StavOpravneni.ODMITNUTO_NATRVALO
        }

        if (povoleno) {
            nacistPolohu()
            onPolohaPovolena()
        }
    }

    // Kontrola oprávnění při návratu do aplikace.
    val lifecycle = (activity as? LifecycleOwner)?.lifecycle

    DisposableEffect(lifecycle) {

        val pozorovatel = LifecycleEventObserver { _, udalost ->

            if (udalost == Lifecycle.Event.ON_RESUME) {

                val povoleno =
                    ContextCompat.checkSelfPermission(
                        context,
                        OPRAVNENI_POLOHA
                    ) == PackageManager.PERMISSION_GRANTED

                if (povoleno) {
                    stav = StavOpravneni.UDELENO
                    nacistPolohu()
                    aktualniOnPolohaPovolena()

                } else if (aktualniStav == StavOpravneni.UDELENO) {
                    stav = StavOpravneni.NEZNAMY
                    souradnice = null
                }
            }
        }

        lifecycle?.addObserver(pozorovatel)

        onDispose {
            lifecycle?.removeObserver(pozorovatel)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        when (stav) {

            StavOpravneni.NEZNAMY -> {

                Button(
                    onClick = {
                        spoustec.launch(OPRAVNENI_POLOHA)
                    }
                ) {
                    Text(stringResource(R.string.poloha_zjistit))
                }
            }

            StavOpravneni.ODMITNUTO -> {

                if (zobrazitVysvetleni) {

                    Text(
                        text = stringResource(R.string.poloha_vysvetleni),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            zobrazitVysvetleni = false
                            spoustec.launch(OPRAVNENI_POLOHA)
                        }
                    ) {
                        Text(stringResource(R.string.poloha_pokracovat))
                    }

                } else {

                    Button(
                        onClick = {
                            zobrazitVysvetleni = true
                        }
                    ) {
                        Text(stringResource(R.string.poloha_zjistit))
                    }
                }
            }

            StavOpravneni.ODMITNUTO_NATRVALO -> {

                Text(
                    text = stringResource(R.string.poloha_bez_opravneni)
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts(
                                "package",
                                context.packageName,
                                null
                            )
                        )

                        context.startActivity(intent)
                    }
                ) {
                    Text(stringResource(R.string.otevrit_nastaveni))
                }
            }

            StavOpravneni.UDELENO -> {

                Text(
                    text = stringResource(R.string.poloha_povolena),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(8.dp))

                if (souradnice != null) {

                    Text(
                        text = stringResource(
                            R.string.poloha_souradnice,
                            souradnice!!.latitude,
                            souradnice!!.longitude
                        )
                    )

                } else {

                    Text(
                        text = stringResource(
                            R.string.poloha_nedostupna
                        )
                    )
                }
            }
        }
    }
}