package se.slashat.slashat.service;

import se.slashat.slashat.R;
import se.slashat.slashat.model.Personal;

public class PersonalService {

	public static Personal[] getPersonal() {

		// Create static entries for every person.
		Personal[] personal = new Personal[4];

		personal[0] = new Personal(
				"Tommie Podziemski",
				R.drawable.tommie,
				"tommie@tommie.nu",
				"tommienu",
				"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
		personal[1] = new Personal(
				"Jezper Sšderlund",
				R.drawable.jezper,
				"kontakt@jezper.se",
				"jezperse",
				"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		personal[2] = new Personal(
				"Magnus Jonasson",
				R.drawable.magnus,
				"magnus@magnusjonasson.com",
				"magnusjonasson",
				"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		personal[3] = new Personal(
				"Johan Larsson",
				R.drawable.johan,
				"johan@johanl.se",
				"kottkrig",
				"Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

		return personal;

	}

}
