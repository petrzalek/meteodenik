package cz.utb.meteodenik.ui.theme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
            if (ContextCompat.checkSelfPermission(context, OPRAVNENI_POLOHA)
                == PackageManager.PERMISSION_GRANTED
            ) StavOpravneni.UDELENO else StavOpravneni.NEZNAMY
        )
    }
    var zobrazitVysvetleni by remember { mutableStateOf(false) }

    val spoustec = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { povoleno ->
        stav = when {
            povoleno -> StavOpravneni.UDELENO
            activity != null && ActivityCompat
                .shouldShowRequestPermissionRationale(activity, OPRAVNENI_POLOHA)
                -> StavOpravneni.ODMITNUTO
            else -> StavOpravneni.ODMITNUTO_NATRVALO
        }
        if (povoleno) onPolohaPovolena()
    }

    // Návrat z Nastavení: uživatel tam mohl oprávnění mezitím povolit.
    val aktualniOnPolohaPovolena by rememberUpdatedState(onPolohaPovolena)
    val lifecycle = (activity as? LifecycleOwner)?.lifecycle
    DisposableEffect(lifecycle) {
        val pozorovatel = LifecycleEventObserver { _, udalost ->
            if (udalost == Lifecycle.Event.ON_RESUME &&
                stav != StavOpravneni.UDELENO &&
                ContextCompat.checkSelfPermission(context, OPRAVNENI_POLOHA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                stav = StavOpravneni.UDELENO
                aktualniOnPolohaPovolena()
            }
        }
        lifecycle?.addObserver(pozorovatel)
        onDispose { lifecycle?.removeObserver(pozorovatel) }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        when (stav) {
            StavOpravneni.UDELENO ->
                Text(stringResource(R.string.poloha_povolena))

            StavOpravneni.ODMITNUTO_NATRVALO -> {
                Text(
                    text = stringResource(R.string.poloha_bez_opravneni),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                }) { Text(stringResource(R.string.otevrit_nastaveni)) }
            }

            else -> if (zobrazitVysvetleni) {
                Text(stringResource(R.string.poloha_vysvetleni))
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    zobrazitVysvetleni = false
                    spoustec.launch(OPRAVNENI_POLOHA)
                }) { Text(stringResource(R.string.poloha_pokracovat)) }
            } else {
                Button(onClick = {
                    val potrebaVysvetlit = activity != null && ActivityCompat
                        .shouldShowRequestPermissionRationale(activity, OPRAVNENI_POLOHA)
                    if (potrebaVysvetlit) zobrazitVysvetleni = true
                    else spoustec.launch(OPRAVNENI_POLOHA)
                }) { Text(stringResource(R.string.poloha_zjistit)) }
            }
        }
    }
}