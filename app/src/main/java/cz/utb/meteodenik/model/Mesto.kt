package cz.utb.meteodenik.model

data class Mesto(
    val id: Int,
    val nazev: String,
    val sirka: Double,     // NOVÉ – bez souřadnic se API zeptat nelze
    val delka: Double      // NOVÉ; teplotaC z lekce 08 zmizela
)