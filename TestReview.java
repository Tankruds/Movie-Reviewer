import java.util.ArrayList;

public class TestReview {
	public String rating;
	public String review;
	public String reviewUpperNoPunc;
	ArrayList<String> allRootWords = new ArrayList<String>();
	ArrayList<Integer> rootFrequency = new ArrayList<Integer>();
	ArrayList<Double> rootTotalFrequency = new ArrayList<Double>();
	
	public TestReview(String review, ArrayList<TrainReview> trainReviews, MovieReviewsMatrix trainMatrix) {
		// Separates the review from the rating.
		this.review = review.substring(review.indexOf("1") + 1, review.length() - 4).trim();
		// Gets the review as uppercase and no punctuation.
		this.reviewUpperNoPunc = this.review.toUpperCase().replaceAll("-", " ").replaceAll("`", "").replaceAll("<BR />", "").replaceAll("<BR >", "").replaceAll(">", "").replaceAll("<", "").replaceAll("'", "").replaceAll("\\p{P}", " ");
		// Gets all of the words
		String[] allWords = this.reviewUpperNoPunc.split(" ");
		String stem;
		// Goes through all of the words.
		for (String word : allWords) {
			// if the word is comprised only of english letters we add the word to the list and keep track of their frequencies.
			if (onlyLetters(word)) {
				stem = PorterStemmer.getStem(word.trim());
				if (!this.allRootWords.contains(stem)) {
					if (trainMatrix.allRootWords.contains(stem)) {
						this.allRootWords.add(stem);
						rootFrequency.add(1);
					}
				} else {
					int index = allRootWords.indexOf(stem);
					rootFrequency.set(index, rootFrequency.get(index) + 1);
				}
			}
		}

		// Calculates the total frequencies of all words.
		double length = allWords.length;
		for (int i = 0; i < rootFrequency.size(); i++) {
			String word = allRootWords.get(i);
			int IDFIndex = trainMatrix.allRootWords.indexOf(word);
			double weight = trainMatrix.allReviewsRootWordIDF.get(IDFIndex);
			rootTotalFrequency.add(i, rootFrequency.get(i) / length * weight);
		}
		
		// Gets the 20 closest neighbors using euclidean distance as it gave me more accuracy then cosine similarity
		knn(20, trainReviews, trainMatrix);
	}
	
	// Calculates the (neighbors) closest neighbors, and keeps track of them.
	public void knn(int neighbors, ArrayList<TrainReview> trainReviews, MovieReviewsMatrix trainMatrix) {
		// Creates two arrays to keep track of the distance and the id of the closest neighbors
		int[] closestNeighbors = new int[neighbors];
		double[] closestDistances = new double[neighbors];
		// Initializes all values to -1.
		for (int i = 0; i < neighbors; i++) {
			closestNeighbors[i] = -1;
			closestDistances[i] = -1;
		}
		// Goes through all of the trainReviews.
		for (int i = 0; i < trainReviews.size(); i++) {
			// Calculates the distance from trainReviews.get(i)
			double distance = distance(trainReviews.get(i));
			// If the distance is less than the farthest of the closest neighbors or it's at -1 then
			// we go through them and find where in the list it goes, and moves the rest of the values down.
			if (distance < closestDistances[neighbors - 1] || closestNeighbors[neighbors - 1] == -1) {
				int j = neighbors;
				while (distance > closestDistances[neighbors - j] && closestNeighbors[neighbors - j] != -1)
					j--;
				for (int k = 1; k < j; k++) {
					closestNeighbors[neighbors - k] = closestNeighbors[neighbors - k - 1];
					closestDistances[neighbors - k] = closestDistances[neighbors - k - 1];
				}
				closestNeighbors[neighbors - j] = i;
				closestDistances[neighbors - j] = distance;
			}
		}
		
		//Count the number of positive vs negative reviews.
		int numOfPositive = 0;
		int numOfNegative = 0;
		for (int i = 0; i < neighbors; i++) {
			if (trainReviews.get(closestNeighbors[i]).rating.equals("+"))
				numOfPositive++;
			else
				numOfNegative++;
		}
		
		// If the number of positive reviews is greater than or equal to negative then we make it positive, else negative.
		if (numOfPositive >= numOfNegative)
			this.rating = "+";
		else 
			this.rating = "-";
	}
	
	// Calculates the euclidean distance between our testReview and the given trainReview.
	public double distance(TrainReview trainReview) {
		double summation = 0;
		double p = 0;
		double q = 0;
		String word;
		for (int i = 0; i < rootTotalFrequency.size(); i++) {
			word = allRootWords.get(i);
			p = rootTotalFrequency.get(i);
			if (trainReview.allRootWords.contains(word))
				q = trainReview.rootTotalFrequency.get(trainReview.allRootWords.indexOf(word));
			else 
				q = 0;
			summation += (p - q) * (p - q);
		}
		for (int i = 0; i < trainReview.rootTotalFrequency.size(); i++) {
			word = trainReview.allRootWords.get(i);
			if (!allRootWords.contains(word)) {
				p = 0;
				q = trainReview.rootTotalFrequency.get(i);
			}
			summation += (p - q) * (p - q);
		}
		double distance = Math.sqrt(summation);
		return distance;
	}
	
	// Takes a string word and checks if it's comprised of only english letters.
		public static boolean onlyLetters(String word) {
			for (int i = 0; i < word.length(); i++)
				if ((int)word.charAt(i) < 65 || (int)word.charAt(i) > 90)
					return false;
			return true;
		}
}