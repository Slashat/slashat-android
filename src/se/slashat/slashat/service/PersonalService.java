package se.slashat.slashat.service;

import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;

public class PersonalService {

	public static Personal[] getPersonal() {

		// Create static entries for every person.
		Personal[] personal = new Personal[5];

		personal[0] = new Personal(
				"Jezper Söderlund",
				R.drawable.jezper,
				"kontakt@jezper.se",
				"jezperse",
				"http://www.jezper.se",
				"En glad och bekväm herre, årsmodell 1980. Jobbar som originalare men är också aktiv inom dansmusikscenen som producenten och DJ:n Airbase."
						+ "\n\nHan spenderar orimligt mycket tid framför datorer och har numer helt och hållet lämnat PC-land får en hundraprocentig Apple-miljö."
						+ "Några Linux-dojor har han aldrig velat ha. Intresset för datorer och teknik har hängt med sedan Amiga 500:an."
						+ "\n\nÖvriga intressen innefattar musik, film, resor, gott öl och vin, motorcyklar, golf och egentligen allt som hör livsnjutning till.");

		personal[1] = new Personal(
				"Tommie Podzemski",
				R.drawable.tommie,
				"tommie@tommie.nu",
				"tommienu",
				"http://www.tommie.nu",
				"Tommie en tjötig och sportig serie-entreprenör med visst huvud för kod."
						+ "Vilket allt som oftast innebär att Tommie för en idé på fredagen, spenderar hela helgen på att koda den för att sedan lägga ner allt på måndagen."
						+ "\n\nKör uteslutande PC (Ubuntu och Windows), och är vad man kallar en Android-fanboy."
						+ "\n\nHan har en viss förkärlek till att pyssla med linux-kärror och jobbar som IT-ansvarig på ett medelstort svenskt företag och säger sällan, t.om aldrig, nej till en öl.");

		personal[2] = new Personal(
				"Magnus Jonasson",
				R.drawable.magnus,
				"magnus@magnusjonasson.com",
				"magnusjonasson",
				"http://www.magnusjonasson.com",
				"Killen som drar upp medelåldern på Slashat-redaktionen då han är den enda i gänget som är född på 70-talet."
						+ "\n\nMagnus jobbar till vardags som IT-tekniker och konsult med affärssystem och Windows Server-miljöer som specialitet, men föredrar faktiskt Mac privat."
						+ "\n\nBeskriver sig själv som en prylnörd och är alltid hungrig på nya prylar att integrera med sitt digitala liv."
						+ "\n\nFörutom teknik så har en kärlek till matlagning har funnits länge och inte mycket slår hemlagat med en kall öl till. Eller två.");
		personal[3] = new Personal(
				"Johan Larsson",
				R.drawable.johan,
				"johan@johanl.se",
				"kottkrig",
				"http://johanl.se",
				"Älskar allt som är interaktivt. Utvecklar till vardags multimediaproduktioner och till söngdags så utvecklar jag annat smått och gott."
						+ "\n\när biOS så jag använder (och utvecklar) lika gärna med Android som iOS. Det finns styrkor och svagheter med allt men jag försöker att använda det som känns mest spännande för stunden."
						+ "\n\nArbetar hellre i OS X och Linux än i Windows när jag vill vara produktiv. Använder hellre Windows än annat när det kommer till lek & spel.");
		personal[4] = new Personal(
				"Team Slashat Development",
				R.drawable.ic_launcher,
				"no@email.com",
				"slashatse",
				"http://slashat.se",
				"Nicklaslof osv osv osv. Här skall namnen stå på alla som varit delaktiga i utvecklingen.");

		return personal;

	}

}
