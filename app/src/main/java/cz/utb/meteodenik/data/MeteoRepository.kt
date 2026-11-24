package cz.utb.meteodenik.data

import cz.utb.meteodenik.model.MeteoZaznam
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.NalezeneMesto

interface MeteoRepository {
    /** Aktuální počasí pro dané město. Vrací doménový model, nikdy DTO. */
    suspend fun aktualniPocasi(mesto: Mesto, vynutitObnovu: Boolean = false): VysledekSite<MeteoZaznam>

    /** Vyhledání města podle názvu (geokódování). */
    suspend fun najdiMesta(dotaz: String): VysledekSite<List<NalezeneMesto>>
}