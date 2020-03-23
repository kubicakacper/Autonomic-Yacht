# masterThesis
This is my master thesis called: "Design and simulation tests of a control system for a semi-autonomous cat sailing vessel".

The project is being made completely in Java 8 in Intellij IDEA enviroment using Maven and Lombok.
The project is developed using the Agile system and the TDD philosophy.
The aim of the project is presented in thesis's topic.
The following paraghraphs contain specification of the app such as classes description and the way it works. It is a part of the paper, so it is in Polish.

Z CZEGO SKŁADA SIĘ PROGRAM?

Program składa się z dwóch głównych klas:

Simulation - ta klasa symuluje środowisko, w którym porusza się łódź, a także określa warunki symulacji; zawiera takie składowe, jak:
- okres próbkowania,
- gęstość wody (ośrodek działania płetwy sterowej),
- gęstość powietrza (ośrodek działania żagla),
- prędkość wiatru,
- kierunek wiatru;
klasa ta definiuje również następujące metody (funkcje) statyczne:
- log2 - funkcja obliczająca logarytm o podstawie 2
- windGradient - metoda obliczająca współczynnik siły wiatru w stosunku do wiatru na poziomie wody (im wyżej, tym wiatr jest silniejszy)
Funkcja gradientu wiatru (windGradient) została opracowana na podstawie danych z: [Aerodynamika Żagla, s. 172]

Yacht - bardzo obszerna klasa będąca modelem jachtu; zawiera 3 klasy zagnieżdżone opisane w kolejnej części podrozdziału:
- Sail (żagiel),
- Rudder (płetwa sterowa),
- WindIndicator (wskaźnik prędkości i kierunku wiatru).
W skład tej klasy wchodzą również następujące składowe charakteryzujące statek oraz jego stan:
- masa,
- moment bezwładności względem środka bocznego oporu (punkt obrotu podczas skrętu łodzi)
- kurs względem północy geograficznej,
- kurs względem kierunku wiatru,
- prędkość liniowa,
- przyspieszenie liniowe,
- prędkość kątowa,
- przyspieszenie kątowe.
Klasa ta zawiera metodę, która na podstawie otrzymanej siły bocznej wytwarzanej przez płetwę sterową oraz siły ciągu wytwarzanej przez żagiel oblicza 4 zmienne stanu łodzi w kolejnej iteracji (prędkość i przyspieszenie liniowe, a także prędkości i przyspieszenie kątowe)

Klasa Sail zawiera z kolei 7 podklas (opisane w następnej części) reprezentujących określone elementy konstrukcyjne żagla:
- SailController (ogólny regulator pracy żagla),
- Car (model wózka szotowego)
- CarEngine (silnik wózka szotowego)
- CarEngineController (regulator pracy silnika wózka szotowego),
- Sheet (szot żagla),
- SheetEngine (silnik szota),
- SheetEngineController (regulator pracy silnika szota).
Klasa Sail posiada pięć niezmiennych wartości określanych przez konstruktor:
- area (powierzchnia),
- footHeight (wysokość liku dolnego żagla),
- headHeight (wysokość głowicy żagla),
- maxTrimAngle (największy możliwy kąt trymu),
- maxTwistAngle (największy możliwy skręt).
- Poza powyższymi wartościami, klasa składa się z dwóch tablic charakteryzujących określony obiekt:
- liftCoefficientArray (tablica współczynników siły nośnej),
- dragCoefficientArray (tablica współczynników siły oporu).
Te tablice są wypełniane wartościami odczytanymi z odpowiedniego pliku. Dzieje się to w konstruktorze przy użyciu trzech metod będących częścią klasy ReadSailCoefficients opisanej w dalszej części rozdziału.
Kolejnymi wartościami w klasie są zmienne charakteryzujące aktualny stan obiektu:
- stateOfSail (zmienna enumeracyjna typu StatesOfSail mówiąca o tym, czy żagiel jest na lewej (PORT) czy prawej burcie (STARBOARD), bądź czy właśnie się to zmienia (BEING_CHANGED)),
- currentTrimAngle (aktualny kąt trymu),
- currentTwistAngle (aktualny skręt),
- currentHeadPosition (aktualny kąt trymu głowicy żagla).
Ponadto klasa zawiera metodę, która na podstawie aktualnego stanu wózka szotowego oraz talii żagla oblicza siłę ciągu wytwarzaną przez żagiel.

Klasa SailController jest modelem regulatora proporcjonalnego. Zawiera następujące zmienne:
- proportionalCoefficientForTrim (wzmocnienie układu regulującego kąt trymu żagla),
- proportionalCoefficientForTwist (wzmocnienie układu regulującego skręt żagla),
- timeTakenIntoAccountWhileCountingAverageWindInSeconds (badany okres brany pod uwagę podczas obliczania uśrednionej siły wiatru w aktualnym momencie, liczony w sekundach),
- windDirectionAtFoot (kierunek wiatru przy liku dolnym),
- windDirectionAtHead (kierunek wiatru przy głowicy żagla),
- windDirectionAtFootHistory (lista ostatnich wartości kierunku wiatru przy liku dolnym, jest to obiekt typu LinkedList<Double>, czyli lista jednokierunkowa)
- windDirectionAtHeadHistory (analogiczna lista dla kierunku wiatru przy głowicy),
- averageWindDirectionAtFoot (średni kierunek wiatru przy liku dolnym),
- averageWindDirectionAtHead (średni kierunek wiatru przy głowicy),
a także:
- trimAnglesForMaxThrust (tablica kątów trymu dających największą siłę ciągu dla kolejnych kierunków żeglugi względem wiatru - wartość obliczona w konstruktorze na podstawie tablic współczynników siły nośnej oraz oporu będących składowymi klasy zewnętrznej - Sail).
Metodami klasy SailController są:
- countAverageAtFoot (oblicza uśredniony kierunek wiatru przy liku dolnym żagla - metoda powiązana z windDirectionAtFootHistory oraz windDirectionAtFoot)
- countAverageAtHead (analogiczna metoda dla kierunku wiatru przy głowicy)
- countRequiredTrim (oblicza wymagany kąt trymu żagla, czyli taki, który pozwala osiągnąć maksymalną siłę ciągu)
- countRequiredTwist (analogiczna metoda dla kierunku wiatru przy głowicy)
Ponadto, klasa zawiera 2 metody główne korzystające z powyższych, odpowiednio dla liku dolnego i głowicy. Metody te otrzymują zmierzony kierunek wiatru (aktualizacja wiatru) oraz zmierzone trym oraz skręt żagla (by porównać do pożądanych). Metoda ta wywołuje wszystkie powyższe metody. Zwracają natomiast wartości sterowań przekazywane kolejno do CarEngineController oraz SheetEngineController.

Klasa CarEngineController przedstawia model regulatora trójpołożeniowego z histertezą. Zawiera trzy zmienne:
- hysteresis (histereza),
- offset (przesunięcie),
- currentStateOfCarEngine (zmienna enumeraycjna typu StatesOfCarEngine, określająca aktualny stan silnika wózka szotowego - obiektu klasy carEngine, zmienna enumeracyjna mówiąca o tym, czy wózek szotowy przesuwa się do osi jachtu (SHEET_IN), do burty (SHEET_OUT), czy jest nieruchomy (STAND_BY)),
a także metodę główną, która zamienia otrzymaną wartość sterowania na wartość zadaną dla silnika, czyli obiektu klasy CarEngine.

Klasa CarEngine prezentuje model silnika wózka szotowego odpowiedzialnego za trym żagla. Klasa ta, podobnie jak SheetEngine dziedziczy od klasy Engine, której obiekt charakteryzują 2 wartości:
- maxVelocity (największa możliwa prędkość rozwijana przez urządzenie napędzane tym silnikiem, ta wartość jest określana w konktruktorze)
- currentVelocity (aktualna prędkość)
Klasa CarEngine jest powiązana ze zmienną enumeracyjną StatesOfCarEngine. Wykorzystuje ją funkcja ustanawiająca obecną prędkość (currentVelocity).
Klasa ta posiada również metodę główną, która zamienia sterowanie z zakresu: -1, 0, 1 na odpowiednią prędkość silnika.

Klasa Car przedstawia wózek szotowy. Zawiera 2 wartości określane w konstruktorze:
- distanceFromMast (odległość wózka od masztu - porusza się on po łuku wokół masztu),
- maxAnglePosition (największy możliwy kąt między prostą przechodzącą przez maszt i wózek a diametralną statku)
oraz 1 zmienną:
- currentPositionInDegrees (obecny kąt w stopniach).
Poza tym, klasa zawiera metodę główną, która otrzymując prędkość wózka szotowego (zmianna klasy CarEngine) oblicza pozycję wózka w kolejnej iteracji.

Klasa SheetEngineController jest analogiczna do klasy CarEngineController z tą różnicą, że zamiast zmiennej enumeracyjnej typu StatesOfCarEngine, używa następującej zmiennej:
- statesOfSheetEngine (zmienna enumeracyjna typu StatesOfSheetEngine, określa stan silnika szota; jej wartości to: wybieranie - skracanie liny (HAUL), stan niezmienny (STAND_BY) oraz luzowanie - wydłużanie liny (EASE)).

Klasa SheetEngine jest z kolei analogiczna do klasy CarEngine. Oczywiście, wykorzystuje zmienną StatesOfSheetEngine.

Klasa Sheet posiada 2 jednokrotnie ustalane wartości:
- gearRatio (przełożenie, iloraz prędkości skracania się szota w stosunku do szybkości wybierania (skracania) liny,
- maxLengthOverMin (największe możliwe wydłużenie się talii, czyli różnica między maksymalną a minimalną jego długością)
oraz jedną zmienną:
- currentLengthOverMin (obecne wydłużenie talii, czyli różnica między obecną a minimalną jej długością).
Dodatkowo, klasa ta zawiera metodę główną analogiczną do metody klasy Car, czyli otrzymującą prędkość wybierania szota przez silnik szota (zmienna klasy SheetEngine) i obliczają długość talii w kolejnej iteracji.

To już wszystkie podklasy należące do klasy Sail.

Klasa Rudder zawiera 3 podklasy:
- RudderController (regulator płetwy sterowej),
- RudderEngineController (regulator pracy silnika płetwy sterowej),
- RudderEngine (silnik płetwy sterowej).
Co więcej, w skład tej klasy wchodzą 2 wartości ustalane podczas tworzenia obiektu:
- area (powierzchnia płetwy),
- maxAngle (największy możliwy kąt wychylenia płetwy z pozycji równoległej do diametralnej łodzi),
a także zmienną:
- currentAngle (aktualny kąt wychylenia płetwy).
Ponadto klasa posiada metodę główną, której rolą jest obliczanie siły bocznej w kolejnej iteracji na podstawie aktualnego wychylenia płetwy oraz jej prędkości kątowej.

Klasa RudderController przedstawia model regulator PID. Posiada zatem zmienne przedstawiające nastawy regulatora:
- proportionalCoefficient (wzmocnienie członu proporcjonalnego),
- integralCoefficient (wzmocnienie członu całkującego), - tutaj IIR
- derivativeCoefficient (wzmocnienie członu różniczkującego),
a także zmienne związane z uchybem:
- errorIntegral (całka z uchybu),
- preError (całka z uchybu w poprzedniej iteracji).
Klasa posiada jedną metodę główną, która otrzymuje 2 wartości: mierzoną (process variable) oraz pożądaną (set point), na podstawie których oblicza uchyb. Następnie wykonuje operacje stosowne dla regulatora PID, aktualizuje wartość całki z uchybu oraz zwraca wartość sterowania (control variable).

Klasa RudderEngineController jest analogiczna do klas CarEngineController oraz SheetEngineController. Różnicę stanowi wykorzystana zmienna enumeracyjna:
- statesOfRudderEngine (zmienna enumeracyjna typu StatesOfRudderEngine, określa stan silnika płetwy sterowej; jej wartości to: ruch w lewo (LEFT), stan niezmienny (STAND_BY) oraz ruch w prawo (RIGHT)).

Klasa RudderEngine jest analogiczna do klas CarEngine i SheetEngine. Korzysta ze zmiennej StatesOfRudderEngine.

Ostatnią klasą zagnieżdżoną klasy zewnętrznej Yacht to WindIndicator. Klasa ta jest symulatorem działania wskaźnika wiatru. Składa się ona wyłącznie z metody głównej. Metoda ta otrzymuje szereg następujących zmiennych:
- prędkość wiatru na poziomie wody,
- kierunek wiatru rzeczywistego,
- prędkość jachtu,
- kierunek ruchu jachtu,
- wysokość na jakiej zamocowany jest czujnik.
Zwraca natomiast zmienną typu Wind, która reprezentuje wiatr pozorny (jego siłę oraz kierunek) występujący dla jednostki w tym stanie na danej wysokości.

JAK PROGRAM SYMULUJE UKŁAD RZECZYWISTY?

Żaglówka, czyli obiekt klasy Yacht zawiera 3 istotne z punktu widzenia projektu urządzenia:
- żagiel (obiekt klasy Sail),
- płetwę sterową (obiekt klasy Rudder),
2/3 wskaźniki prędkości i kierunku wiatru (obiekty klasy WindIndicator).

Żaglówka porusza się po wodzie, która stawia pewien opór zależny od prędkości łodzi. Na żagle działa wiatr, który ma określoną siłę oraz kierunek. Siła (prędkość) wiatru jest zależna od wysokości nad powierzchnią wody. Całe to środowisko jest określone w obiekcie klasy Simulation. Żaglówka otrzymuje od tego obiektu wartości takie jak gęstość wody i powietrza, siłę i kierunek wiatru itd., czyli jak wskazuje nazwa Simulation symulowane są w niej warunki na potrzeby testów i symulacji projektu. W przypadku rzeczywistej implementacji, klasa ta nie zostałaby użyta.

Klasę Simulation oraz klasę Yacht niejako łączy podklasa WindIndicator, ponieważ wskaźnik wiatru (WindIndicator) odbiera informacje w postaci prędkości i kierunku wiatru od obiektu klasy Simulation, a następnie na podstawie stanu obiektu klasy zewnętrznej (Yacht), a konkretnie prędkości i kierunku łodzi, otrzymuje wartości będące jednymi ze zmiennych wejściowych całego układu regulacji żagla, czyli prędkość i kierunek wiatru pozornego w układzie odniesienia żaglówki dla określonej wysokości. Zatem klasa wskaźnika wiatru jest również tylko na potrzeby testów i symulacji. W fizycznym projekcie, tę rolę pełnić będą rzeczywiste wskaźniki, które siłą rzeczy badają wiatr pozorny w układzie odniesienia żaglówki. Pod względem pełnionej funkcji klasa WindIndicator jest bardziej zbliżona do klasy Simulation, natomiast ze względu na fakt, że symulowany wskaźnik wiatru w rzeczywistości stanowi część łodzi, zdecydowano się o umieszczenie tej klasy wewnątrz klasy Yacht.

Pozostała część programu to dwa niezależne układy regulacji: żagla oraz płetwy sterowej. Ich implementacja komputerowa przeznaczona opiera się na układzie sterowania zaprojektowanym dla implementacji fizycznej (patrz rozdział: “Układ sterowania”).
