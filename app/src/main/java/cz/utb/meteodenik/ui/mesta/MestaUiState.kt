package cz.utb.meteodenik.ui.mesta

import cz.utb.meteodenik.data.DuvodSelhani
import cz.utb.meteodenik.model.MeteoZaznam
import cz.utb.meteodenik.model.Mesto

data class MestoSPocasim(
    val mesto: Mesto,
    val pocasi: MeteoZaznam? = null      // null = pro toto město ještě nemáme data
)

data class MestaUiState(
    val mesta: List<MestoSPocasim> = emptyList(),
    val vybraneMestoId: Int? = null,
    val nacitaSe: Boolean = false,
    val chyba: DuvodSelhani? = null      // ZMĚNA proti lekci 08: výčet, ne Int
)