import java.util.ArrayList;

public class MovieReviewsMatrix {
	ArrayList<String> allRootWords = new ArrayList<String>();
	ArrayList<Integer> allRootWordsFrequency = new ArrayList<Integer>();
	ArrayList<Double> allReviewsRootWordIDF = new ArrayList<Double>();
	
	public MovieReviewsMatrix(ArrayList<TrainReview> trainReviews) {
		// Makes a single ArrayList with all root words in it.
		ArrayList<String> allRootWordsOfReview;
		for (int i = 0; i < trainReviews.size(); i++) {
			allRootWordsOfReview = trainReviews.get(i).allRootWords;
			for (String word : allRootWordsOfReview)
				if (!this.allRootWords.contains(word)) {
					this.allRootWords.add(word);
					this.allRootWordsFrequency.add(1);//
				} else {//
					int index = this.allRootWords.indexOf(word);//
					this.allRootWordsFrequency.set(index, 1 + this.allRootWordsFrequency.get(index));//
				}//
		}
		
		// Calculates the idf of all of them.
		for (int i = 0; i < allRootWords.size(); i++) {
			allReviewsRootWordIDF.add(Math.log(((double)(trainReviews.size()))/allRootWordsFrequency.get(i)));
		}

		// calculates our final frequency values using tf * idf.
		for (TrainReview review : trainReviews) {
			for (int i = 0; i < review.rootTotalFrequency.size(); i++) {
				String word = review.allRootWords.get(i);
				int IDFIndex = this.allRootWords.indexOf(word);
				double weight = allReviewsRootWordIDF.get(IDFIndex);
				double currentTotalFrequency = review.rootTotalFrequency.get(i);
				review.rootTotalFrequency.set(i, currentTotalFrequency * weight);
			}
		}
	}
}