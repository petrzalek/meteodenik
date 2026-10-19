package cz.utb.meteodenik.cv04.prehled

data class Mesto(val nazev: String, val teplota: Double)

fun Double.jakoCelsia(): String = "$this C"

fun Mesto.popis(): String = "$nazev: ${teplota.jakoCelsia()}"

sealed interface StavPrehledu {
    data object Nacita : StavPrehledu
    data object Chyba : StavPrehledu
    data object Prazdno : StavPrehledu
    data class Hotovo(val mesta: List<Mesto>) : StavPrehledu
}

class Prehled(private val mesta: List<Mesto>) {

    fun textStavu(): String {
        val stav = if (mesta.isEmpty()) {
            StavPrehledu.Prazdno
        } else {
            StavPrehledu.Hotovo(mesta)
        }

        return when (stav) {
            StavPrehledu.Nacita -> "Načítám…"
            StavPrehledu.Chyba -> "Chyba"
            StavPrehledu.Prazdno -> "Nic tu není"
            is StavPrehledu.Hotovo -> "Měst: ${stav.mesta.size}"
        }
    }

    fun nejteplejsi(): Mesto? =
        mesta.maxByOrNull { it.teplota }

    fun vypis() {
        mesta.forEach { println(it.popis()) }
        println(textStavu())

        nejteplejsi()?.let {
            println("Nejtepleji: ${it.nazev} (${it.teplota.jakoCelsia()})")
        }
    }
}

fun main() {
    val p = Prehled(
        listOf(
            Mesto("Zlín", 21.4),
            Mesto("Brno", 23.0),
            Mesto("Otrokovice", 20.9)
        )
    )
    p.vypis()
}