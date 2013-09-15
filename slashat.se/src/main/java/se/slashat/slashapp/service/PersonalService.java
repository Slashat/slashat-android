package se.slashat.slashapp.service;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Personal;
import se.slashat.slashapp.model.Personal.Type;

public class PersonalService {

    public static Personal[] getPersonal(Type type) {

        // Create static entries for every person.
        if (type == Type.SHOW) {
            Personal[] personal = new Personal[1];

            personal[0] = new Personal("Om Slashat.se", "Sveriges största teknikpodcast", Type.SHOW, R.drawable.slashat, "slashat@slashat.se", "slashat", "http://www.slashat.se",
                    "Slashat.se är en svensk teknikpodcast som drivs av Jezper Söderlund och Tommie Podzemski. Podcasten, som är dryga timmen lång, utkommer varje tisdag kväll och i showen diskuteras nyheter främst rörande teknik, prylar, mjukvara, hårdvara och internet."
                            + "\n\nSedan starten 2009 har Slashat-redaktionen fått tillökning av Magnus Jonasson och Johan Larsson som är givna röster vid våra liverapporteringar från t.ex. Apple events och liknande. De dyker dessutom allt som oftast upp i eftersnacken på tisdagarna, i våra specialavsnitt och syns också i reportage från mässor och events som vi besöker."
                            + "Slashat.se spelas in live inför kamera varje tisdag klockan 20.00 med försnack från klockan 19.30 och lyssnare är välkomna att titta och vara en del i diskussionerna via livechatten."
                            + "\n\nÖvriga intressen innefattar musik, film, resor, gott öl och vin, motorcyklar, golf och egentligen allt som hör livsnjutning till.");

            return personal;
        }
        if (type == Type.HOST) {
            Personal[] personal = new Personal[2];

            personal[0] = new Personal("Jezper Söderlund", "Apple fanboy", Type.HOST, R.drawable.jezper, "kontakt@jezper.se", "jezperse", "http://www.jezper.se",
                    "En glad och bekväm herre, årsmodell 1980. Jobbar som originalare men är också aktiv inom dansmusikscenen som producenten och DJ:n Airbase."
                            + "\n\nHan spenderar orimligt mycket tid framför datorer och har numer helt och hållet lämnat PC-land får en hundraprocentig Apple-miljö."
                            + "Några Linux-dojor har han aldrig velat ha. Intresset för datorer och teknik har hängt med sedan Amiga 500:an."
                            + "\n\nÖvriga intressen innefattar musik, film, resor, gott öl och vin, motorcyklar, golf och egentligen allt som hör livsnjutning till.");

            personal[1] = new Personal(
                    "Tommie Podzemski",
                    "Borde avlönas av Google",
                    Type.HOST,
                    R.drawable.tommie,
                    "tommie@tommie.nu",
                    "tommienu",
                    "http://www.tommie.nu",
                    "Tommie en tjötig och sportig serie-entreprenör med visst huvud för kod."
                            + "Vilket allt som oftast innebär att Tommie för en idé på fredagen, spenderar hela helgen på att koda den för att sedan lägga ner allt på måndagen."
                            + "\n\nKör uteslutande PC (Ubuntu och Windows), och är vad man kallar en Android-fanboy."
                            + "\n\nHan har en viss förkärlek till att pyssla med linux-kärror och jobbar som IT-ansvarig på ett medelstort svenskt företag och säger sällan, t.om aldrig, nej till en öl.");
            return personal;
        }
        if (type == Type.ASSISTANT){
            Personal[] personal = new Personal[2];
            personal[0] = new Personal("Magnus Jonasson", "Prylnörden", Type.ASSISTANT, R.drawable.magnus, "magnus@magnusjonasson.com", "magnusjonasson", "http://www.magnusjonasson.com",
                    "Killen som drar upp medelåldern på Slashat-redaktionen, då han är den enda i gänget som är född på 70-talet. Magnus jobbar till vardags som konsultchef och tekniker och arbetar med affärssystem och Windows Server-miljöer men föredrar Mac privat."
                            + "\n\nAlltid på jakt efter nya prylar att integrera i sitt digitala liv. Förutom teknik så har en kärlek till matlagning funnits länge och inte mycket slår hemlagad mat med en kall öl. Eller två.");
            personal[1] = new Personal(
                    "Johan Larsson",
                    "Kottrageare",
                    Type.ASSISTANT,
                    R.drawable.johan,
                    "johan@johanl.se",
                    "kottkrig",
                    "http://johanl.se",
                    "Älskar allt som är interaktivt. Utvecklar till vardags multimediaproduktioner och till sängdags så utvecklar jag annat smått och gott."
                            + "\n\nÄr biOS så jag använder (och utvecklar) lika gärna med Android som iOS. Det finns styrkor och svagheter med allt men jag försöker att använda det som känns mest spännande för stunden."
                            + "\n\nArbetar hellre i OS X och Linux än i Windows när jag vill vara produktiv. Använder hellre Windows än annat när det kommer till lek & spel.");
            return personal;
        }

        if (type == Type.DEV) {
            Personal[] personal = new Personal[2];
            personal[0] = new Personal("Nicklas Löf", "Android-kung", Type.DEV, R.drawable.nicklas, "nicklaslof76@gmail.com", "nicklas.lof", "http://www.soundcloud.com/snukey/",
                    "Relativt ny Slashat-lyssnare som varit datorintresserad sedan en Vic20 med 12\" svartvit TV uppenbarade sig under granen för många herrans år sedan. Efter detta har vägen gått via C64, Amiga och PC till att bli en trogen Mac-användare i snart 10 år. Testade därför iOS under 1 år men gick över till Android efter 1 år."
                            + "\n\nJobbat som sysadmin och nättekniker men jobbar sedan några år tillbaka som programmerare på heltid. Mest Java och C#. Har hjälpt till med utvecklingen av Android-versionen av Slashats app."
                            + "\n\nAktiv Xbox-spelare och har tillsammans med sambon kört igenom nästan varenda co-op-spel med splitscreen. Försöker även att producera elektronisk musik med varierande framgång.");
            personal[1] = new Personal("Erika Thorsen", "iOS-drottning", Type.DEV, R.drawable.erika, "erika@akiri.se", "", "http://www.akiri.se/",
                    "Erika har ställt upp med kod och programmeringgsupport i iOS-appen. Till vardags jobbar hon som approgrammerare på en mellanstor konsultfirma. På fritiden slukar hon TV-serier à la science fiction eller springer i skogen med scouterna."
                    + "\n\nHon gillar och kör Mac, både på jobbet och privat, men programmerar lika gärna appar till iOS som till Android.");
            return personal;
        }

        return new Personal[0];

    }

}
