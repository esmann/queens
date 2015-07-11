# Hovedemner (overhead) #

Udover disse emner har vi et errata-dokument.

## Kort introduktion (meget kort!) ##

  * gentagelse af PF (i datid ;-)), afgrænsning og konklusion. Herunder (ultra kort) hvad er MiG, oc og nq.
  * Status er ganske kort: "Vi har sat beregning igang for n=26, ved at submitte 1000 ud af 120000 job". "Vi vil komme give en grundigere status senere"
  * "Spørg endelig ind"


## Fejl og mangler i rapporten ##

  * Grundigere forklaring af mekanismen bag checkpointing'en og hvorfor det bliver så kompliceret.

> - vi er nødt til at checkpointe det rigtige sted, fordi checkpointing fungerer med serialiserede java-objekter, og programtæller ikke gemmes.

  * Billedtekst figur 9 / senere afsnit 26 skulle have været 10!!!

> - ny udregning af hvor meget checkpoints kan komme til at fylde.

  * Afsnit 8 - Det ville være nemmere at porte og test oc- programmer hvis Migob ikke arvede fra Applet. Glem abstract - det er en tanke-omformulerings-torsk

  * TODO: Har vi andre mangler i forhold til problemformuleringen?


## Status på projektet ##

Grundigere gennemgang af status på projektet.

Vil give en opdateret status for den konkrete beregning af n-dronning-problemet for n=26, og de problemer vi er stødt på.

> - Vi har foretaget en iterativ, arraybaseret implementation, og igangsat beregningen på MiG. Står i rapport.

> - Beregninger for n op til 18 med ? antal opgaver er foretaget på gridet, uden problemer.

> - Vi har submittet 1000 ud af ca. 12000 job for n=26, har indtil videre fået svar på ét bræt. Tog 24timer, ca. 13mia løsninger.

> - problemer med gamle job, vi submittede først alle 13000 job, men fik så timeout før vi fik jobid tilbage.

  * Uden jobid har vi ikke mulighed for at standse dem.

  * Efter timeouten får vi ikke lov til at køre migls på hele kataloget. :-)

  * Status-siden virker ikke i øjeblikket, vi har dog snakket med Martin Rehr, han er ved at ordne det.

> - Ting man skal ligge mærke til:

  * Det der kører er kompileret til minimum Java 1.5.

  * Vi oplever ofte tomme status, stdout og stderr filer på job der er tomme, selvom jobbets status er finished.

  * java.lang.ERROR, MiG status = success!!.

  * Der kan opstå andre fejl, der vil give os errors og MiG status = succes...

  * For gammel jvm-version på klienten

  * Gamle klassefiler

  * Slettede klassefiler

  * IO og andre EXCEPTIONS vi fanger, kan vi kan ikke sætte status - kun skrive i stderr.

> - Mht. hastigheden er vi ikke selv udpræget tilfredse, men det blev i første omgang prioriteret lavt.

  * For n op til 18 har vi set at vores iterative er næsten dobbelt så langtsom som originalen (og den rekursive java kode).

> - Som nævnt mener vi det skyldes

  * i den iterative implementation har vi været nødt til at have flere branches end i den originale, rekursive.

  * Vi formode at overløbscheck i java-arrays er en stor skurk

  * Dog har vi har svært ved at profilere koden, da profileringsværktøjer som regel viser tidsforbrug per funktion, ikke per linie.

> - I forsøget på at undgå overløbscheck har vi implementeret en løsning der bruger en dobbelthægtet liste

  * liste af linieobjekter med bitvektorer.

  * Overvejede java's Stack, men den har et underliggende Array.

  * Ny version kører næsten lige så hurtigt som rekursiv java impl.


  * Hvorfor er vi ikke gået videre med løsningen med intelligente ressourcer? (manglende ipc)

> - Til at begynde med snakkede vi meget om en løsning med intelligente job, der selv tager sig af at hente nye problemer fra gridet, og som samtidig hver især kunne overvåge den samlede beregning, og generere nye problemer (ved at opsplitte de store, initielle problemer) når der var ressourcer til at beregne dem. Vi gik væk fra denne idé, da den er væsentligt mere kompliceret og de fordele en intelligent løsning ville give vil ikke nødvendigvis overstige den ekstra køretid for den mere komplicerede algoritme. Adgang til filer på MiG er ikke atomisk. Heterogene ressourcer gør det svært at sige noget foruftigt om hvordan job kunne opføre sig intelligent.


# Idéer #

## Flere forbedringer til One-Click: ##

  * checkpointing kunne returnere efter serialisering, så vi kan regne videre mens der sendes. (latency-hiding?)

## Whatever else ##

  * schedulering i BOINC osv. Kan vi overføre noget til MiG?

  * skal vi konkludere at det er lige meget med opgavestørrelserne, vi får alligevel kun problemet til sidst, og der har vi så andre projekter, der vil overtage gridet.

  * Vi kunne lave estimat af hvor langt tid det vil tage at køre hhv. den største og den mindste opgave. Vi har pt. en færdig opgave at regne ud fra.

  * Kørselsvejledning?!

  * Havde vi haft mere tid ville vi have foretaget optimeringer af javakoden (stak, mRSL-argumenter). Det første fik vi gjort efter opgaven blev afleveret. Implementation med hhv. dobblethægtet liste ville være næste punkt. Benchmarks af alternative implementationer.


# Vi skal læse op på: (for at kunne besvare spørgsmål :-)) #

  * Problemformuleringen, referencer, dynamisk/statisk orkestrering
