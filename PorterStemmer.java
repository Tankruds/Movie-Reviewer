public class PorterStemmer {
	public static String getStem(String word) {
		String tempWord = word.toUpperCase();
		
		//This section now focuses on the rules laid out by the Porter Stemming algo.
		//Step 1A
		//	CONFESSES -> CONFESS
		//	PONIES -> PONI
		//  CARESS -> CARESS
		//	CATS -> CAT
		if (tempWord.endsWith("SSES"))
			tempWord = tempWord.substring(0, tempWord.length() - 2);
		else if (tempWord.endsWith("IES"))
			tempWord = tempWord.substring(0, tempWord.length() - 2);
		else if (tempWord.endsWith("SS"))
			tempWord = tempWord.substring(0, tempWord.length());
		else if (tempWord.endsWith("S"))
			tempWord = tempWord.substring(0, tempWord.length() - 1);
		
		//Step 1B
		// FEED -> FEED | AGREED -> AGREE	If word has a m > 1 before EED then it will lose D.
		// PLASTERED -> PLASTER | BLED -> BLED	If word contains a vowel before ED it will lose ED.
		// MOTORING -> MOTOR | SING -> SING If word contains a vowel before ING it will lose ING.
		boolean extra1BStep = false;
		if (tempWord.endsWith("EED")) {
			if (getM(tempWord.substring(0, tempWord.length() - 3)) > 0)
				tempWord = tempWord.substring(0, tempWord.length() - 1);
		} else if (tempWord.endsWith("ED")) {
			if (containsVowel(tempWord.substring(0, tempWord.length() - 2)))
				tempWord = tempWord.substring(0, tempWord.length() - 2);
			extra1BStep = true;
		} else if (tempWord.endsWith("ING")) {
			if (containsVowel(tempWord.substring(0, tempWord.length() - 3)))
				tempWord = tempWord.substring(0, tempWord.length() - 3);
			extra1BStep = true;
		}
		//If the second or third portion of step 1B happen, this section must then be carried out.
		if (extra1BStep) {
			if (tempWord.length() > 1 && tempWord.substring(tempWord.length() - 2, tempWord.length()).equals("AT"))
				tempWord += "E";
			if (tempWord.length() > 1 && tempWord.substring(tempWord.length() - 2, tempWord.length()).equals("BL"))
				tempWord += "E";
			if (tempWord.length() > 1 && tempWord.substring(tempWord.length() - 2, tempWord.length()).equals("IZ"))
				tempWord += "E";
			if (tempWord.length() > 1 && !isVowel(tempWord.length() - 1, tempWord) && !isVowel(tempWord.length() - 2, tempWord) && tempWord.charAt(tempWord.length() - 1) == tempWord.charAt(tempWord.length() - 2)) {
				if (!(tempWord.endsWith("L") || tempWord.endsWith("S") || tempWord.endsWith("Z")))
					tempWord = tempWord.substring(0, tempWord.length() - 1);
			} else if (tempWord.length() > 2 && iscvc(tempWord.substring(tempWord.length() - 3)) && getM(tempWord) == 1) {
				if (!(tempWord.endsWith("W") || tempWord.endsWith("X") || tempWord.endsWith("Y")))
					tempWord += 'E';
			}
		}
		
		//This is step 1C
		if (tempWord.endsWith("Y"))
			if (containsVowel(tempWord.substring(0, tempWord.length() - 1)))
				tempWord = tempWord.substring(0, tempWord.length() - 1) + "I";
		
		//This is step 2
		if (tempWord.endsWith("ATIONAL") && getM(tempWord.substring(0, tempWord.length() - 7)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 7) + "ATE";
		else if (tempWord.endsWith("TIONAL") && getM(tempWord.substring(0, tempWord.length() - 6)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 6) + "TION";
		else if (tempWord.endsWith("ENCI") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "ENCE";
		else if (tempWord.endsWith("ANCI") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "ANCE";
		else if (tempWord.endsWith("IZER") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "IZE";
		else if (tempWord.endsWith("ABLI") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "ABLE";
		else if (tempWord.endsWith("ALLI") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "AL";
		else if (tempWord.endsWith("ENTLI") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "ENT";
		else if (tempWord.endsWith("ELI") && getM(tempWord.substring(0, tempWord.length() - 3)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 3) + "E";
		else if (tempWord.endsWith("OUSLI") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "OUS";
		else if (tempWord.endsWith("IZATION") && getM(tempWord.substring(0, tempWord.length() - 7)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 7) + "IZE";
		else if (tempWord.endsWith("ATION") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "ATE";
		else if (tempWord.endsWith("ATOR") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "ATE";
		else if (tempWord.endsWith("ALISM") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "AL";
		else if (tempWord.endsWith("IVENESS") && getM(tempWord.substring(0, tempWord.length() - 7)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 7) + "IVE";
		else if (tempWord.endsWith("FULNESS") && getM(tempWord.substring(0, tempWord.length() - 7)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 7) + "FUL";
		else if (tempWord.endsWith("OUSNESS") && getM(tempWord.substring(0, tempWord.length() - 7)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 7) + "OUS";
		else if (tempWord.endsWith("ALITI") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "AL";
		else if (tempWord.endsWith("IVITI") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "IVE";
		else if (tempWord.endsWith("BILITI") && getM(tempWord.substring(0, tempWord.length() - 6)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 6) + "BLE";
		
		//This is step 3
		if (tempWord.endsWith("ICATE") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "IC";
		else if (tempWord.endsWith("ATIVE") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5);
		else if (tempWord.endsWith("ALIZE") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "AL";
		else if (tempWord.endsWith("ICITI") && getM(tempWord.substring(0, tempWord.length() - 5)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 5) + "IC";
		else if (tempWord.endsWith("ICAL") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "IC";
		else if (tempWord.endsWith("FUL") && getM(tempWord.substring(0, tempWord.length() - 3)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("NESS") && getM(tempWord.substring(0, tempWord.length() - 4)) > 0)
			tempWord = tempWord.substring(0, tempWord.length() - 4);
		
		//This is step 4
		if (tempWord.endsWith("AL") && getM(tempWord.substring(0, tempWord.length() - 2)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 2);
		else if (tempWord.endsWith("ANCE") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4);
		else if (tempWord.endsWith("ENCE") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4);
		else if (tempWord.endsWith("ER") && getM(tempWord.substring(0, tempWord.length() - 2)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 2);
		else if (tempWord.endsWith("IC") && getM(tempWord.substring(0, tempWord.length() - 2)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 2);
		else if (tempWord.endsWith("ABLE") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4);
		else if (tempWord.endsWith("IBLE") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4);
		else if (tempWord.endsWith("ANT") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("EMENT") && getM(tempWord.substring(0, tempWord.length() - 5)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 5);
		else if (tempWord.endsWith("MENT") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4);
		else if (tempWord.endsWith("ENT") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("SION") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "S";
		else if (tempWord.endsWith("TION") && getM(tempWord.substring(0, tempWord.length() - 4)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 4) + "T";
		else if (tempWord.endsWith("OU") && getM(tempWord.substring(0, tempWord.length() - 2)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 2);
		else if (tempWord.endsWith("ISM") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("ATE") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("ITI") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("OUS") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("IVE") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		else if (tempWord.endsWith("IZE") && getM(tempWord.substring(0, tempWord.length() - 3)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 3);
		
		//This is step 5A
		if (tempWord.endsWith("E") && getM(tempWord.substring(0, tempWord.length() - 1)) > 1)
			tempWord = tempWord.substring(0, tempWord.length() - 1);
		else if (tempWord.endsWith("E") && getM(tempWord.substring(0, tempWord.length() - 1)) == 1) {
			tempWord = tempWord.substring(0, tempWord.length() - 1);
			if (tempWord.length() > 2 && iscvc(tempWord.substring(tempWord.length() - 3))) {
				if (!(tempWord.endsWith("W") || tempWord.endsWith("X") || tempWord.endsWith("Y")))
					tempWord += "E";
			}
		}
	
		//This is step 5B
		if (getM(tempWord) > 1 && tempWord.endsWith("LL")) {
			tempWord = tempWord.substring(0, tempWord.length() - 1);
		}

		return tempWord;
	}
	
	public static int getM(String word) {
		int i = 0, m = 0, numOfFirstConsonants = 0;
		while (i < word.length() && !isVowel(i, word)) {
			numOfFirstConsonants++;
			i++;
		}
		int numOfConsonants = numOfFirstConsonants;
		int totalNumOfConsonants = getTotalConsonants(word);
		while (numOfConsonants != totalNumOfConsonants) {
			while (isVowel(i, word))
				i++;
			while (i < word.length() && !isVowel(i, word)) {
				numOfConsonants++;
				i++;
			}
			m++;
		}
		return m;
	}
	
	public static boolean iscvc(String thing) {
		if (isVowel(0, thing))
			return false;
		if (!isVowel(1, thing))
			return false;
		if (isVowel(2, thing))
			return false;
		return true;
	}
	
	public static boolean isVowel(int i, String thing) {
		switch (("" + thing.charAt(i)).toUpperCase()) {
			case "A":
			case "E":
			case "I":
			case "O":
			case "U": return true;
			case "Y": return (i == 0) ? false : !isVowel(i - 1, thing);
		}
		return false;
	}
	
	public static boolean containsVowel(String word) {
		for (int i = 0; i < word.length(); i++)
			if (isVowel(i, word))
				return true;
		return false;
	}
	
	public static int getTotalConsonants(String word) {
		int numOfConsonants = 0;
		for (int i = 0; i < word.length(); i++)
			if (!isVowel(i, word))
				numOfConsonants++;
		return numOfConsonants;
	}
}