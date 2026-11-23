package cz.utb.meteodenik.ui.mesta

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.utb.meteodenik.data.MestaZdroj
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

private const val KLIC_VYBRANE_MESTO = "vybraneMestoId"

class MestaViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val vybraneMestoId: StateFlow<Int?> =
        savedStateHandle.getStateFlow<Int?>(KLIC_VYBRANE_MESTO, null)

    val uiState: StateFlow<MestaUiState> =
        combine(MestaZdroj.mesta, vybraneMestoId) { seznam, vybraneId ->
            MestaUiState(mesta = seznam, vybraneMestoId = vybraneId)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MestaUiState()
        )

    fun vyberMesto(id: Int) {
        savedStateHandle[KLIC_VYBRANE_MESTO] = id
    }

    fun pridejMesto(nazev: String) {
        if (nazev.isBlank()) return
        MestaZdroj.pridej(nazev.trim())
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Stav", "MestaViewModel onCleared")   // dočasně, pro dnešní měření
    }
}