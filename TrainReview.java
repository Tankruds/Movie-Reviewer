import java.util.ArrayList;

public class TrainReview {
	public String rating;
	public String review;
	public String reviewUpperNoPunc;
	ArrayList<String> allRootWords = new ArrayList<String>();
	ArrayList<Integer> rootFrequency = new ArrayList<Integer>();
	ArrayList<Double> rootTotalFrequency = new ArrayList<Double>();

	public TrainReview(String review) {
		// Separates the review from the rating.
		this.review = review.substring(review.indexOf("1") + 1, review.length() - 4).trim();
		// Separates the rating from the review.
		this.rating = review.substring(review.indexOf("1") - 1, review.indexOf("1"));
		// Takes the review and makes it all caps, removes punctuation, and some html I saw.
		this.reviewUpperNoPunc = this.review.toUpperCase().replaceAll("-", " ").replaceAll("`", "").replaceAll("<BR />", "").replaceAll("<BR >", "").replaceAll(">", "").replaceAll("<", "").replaceAll("'", "").replaceAll("\\p{P}", " ");
		// Separates all the words into separate strings.
		String[] allWords = this.reviewUpperNoPunc.split(" ");
		// Find the root of a word, if it's not already in allRootWords, then add it.
		String stem;
		// Loop through all words in this review
		for (String word : allWords) {
				// If the word is comprised of only english letters we stem it, else we skip everything else
				if (onlyLetters(word))
					stem = PorterStemmer.getStem(word.trim());
				else 
					continue;
				// If the word hasn't showed up we add it to allRootWords with a frequency of 1.
				// else we incresase the frequency of the word.
				if (!this.allRootWords.contains(stem)) {
					this.allRootWords.add(stem);
					rootFrequency.add(1);
				} else {
					int index = allRootWords.indexOf(stem);
					rootFrequency.set(index, rootFrequency.get(index) + 1);
				}
		}
		
		// We remove words that appear less than or equal to 4 times to reduce the size and runtime.
		for (int i = 0; i < allRootWords.size(); i++) {
			if (rootFrequency.get(i) <= 4) {
				rootFrequency.remove(i);
				allRootWords.remove(i);
				i--;
			}
		}

		// Takes the frequencies and turns them into tf so we can use it to get tf * idf later.
		double length = allWords.length;
		for (int i = 0; i < rootFrequency.size(); i++)
			rootTotalFrequency.add(i, rootFrequency.get(i) / length);
	}
	
	// Takes a string word and checks if it's comprised of only english letters.
	public static boolean onlyLetters(String word) {
		for (int i = 0; i < word.length(); i++)
			if ((int)word.charAt(i) < 65 || (int)word.charAt(i) > 90)
				return false;
		return true;
	}
}