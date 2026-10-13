|
data class Zapis(val mesto: String, val teplota: Double, val poznamka: String = "")
|
getMesto(): String, getTeplota(): double, getPoznamka(): String, component1(): String, component2(): double, component3(): String, copy(String, double, String): Zapis, equals(Object): boolean, hashCode(): int, toString(): String
|
cca 40–60 řádků
|

|
object Prevody
|
public static final Prevody INSTANCE; privátní konstruktor Prevody()
|
cca 5–10 řádků
|

|
const val ABSOLUTNI_NULA = -273.15
|
public static final double ABSOLUTNI_NULA	
|
cca 1–2 řádků
|

|
fun Double.zaokrouhlenoNaDesetiny(): Double	
|
public static final double zaokrouhlenoNaDesetiny(double)
|
cca 3–5 řádků
|

|
suspend fun nactiPoZdrzeni(zapisy: List<Zapis>): List<Zapis>
|
public static final Object nactiPoZdrzeni(List zapisy, Continuation $completion)
|
cca 15–30 řádků
|

1. Kolik metod má dekompilovaná třída Zapis a které nemají v Kotlinu přímý zápis?

Třída Zapis má 10 metod: tři gettery, tři metody componentN(), metodu copy(), metody equals(), hashCode() a toString(). Přímý zápis v Kotlinu nemají gettery, metody componentN(), copy(), equals(), hashCode() ani toString() — kompilátor je vytvoří automaticky díky data class a deklarovaným vlastnostem. Konstruktory se do tohoto počtu nezapočítávají.

2. Proč má Zapis konstruktor navíc s parametry int a DefaultConstructorMarker?

Tento syntetický konstruktor kompilátor vytváří kvůli podpoře výchozích hodnot parametrů v Kotlinu. V našem případě má parametr poznamka výchozí hodnotu "", takže konstruktor může pomocí bitové masky typu int určit, zda se má použít výchozí hodnota, a DefaultConstructorMarker odlišuje tento konstruktor od běžného konstruktoru. Pokud výchozí hodnotu odstraníme, tento pomocný konstruktor již nebude potřeba.

3. Jaký je první parametr metody zaokrouhlenoNaDesetiny() a co to znamená pro polymorfismus?

Prvním parametrem je double, který představuje původní hodnotu typu Double, na níž byla rozšiřující funkce zavolána. Kotlin ji přeloží na statickou metodu PitvaKt.zaokrouhlenoNaDesetiny(double), nikoliv na skutečnou metodu instance třídy Double. Proto tato funkce nepodporuje polymorfní výběr metody podle skutečného typu objektu za běhu.

4. Jaký typ má druhý parametr funkce prumer() a co to prozrazuje o JVM?

Druhý parametr má typ kotlin.jvm.functions.Function1, který představuje funkci přijímající jeden argument typu Zapis a vracející hodnotu typu Double. JVM tedy může funkci předávat jako objekt implementující rozhraní s metodou invoke(). V dekompilovaném kódu se proto výběrová funkce volá přes vyber.invoke(var6) a její výsledek se převádí na double.

5. Co se stalo s návratovým typem funkce nactiPoZdrzeni() a proč se objevil switch?

Původní návratový typ List<Zapis> se v JVM signatuře změnil na Object, protože funkce suspend může při pozastavení vrátit speciální hodnotu COROUTINE_SUSPENDED a výsledek dodat později. Kompilátor vytvořil stavový automat, který pomocí pole label eviduje, ve které fázi vykonávání se funkce nachází. Příkaz switch podle této značky rozlišuje první spuštění a obnovení funkce po dokončení delay(50).