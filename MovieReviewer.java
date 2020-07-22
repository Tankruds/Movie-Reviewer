import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MovieReviewer {
	public static void main(String[] args) throws IOException, InterruptedException {		
		//Opens the training data file to read in the info and store the reviews in trainReviews
		File trainFile = new File("C:\\Users\\Dennis\\JavaWorkspace\\MovieReviewer\\src\\train.dat");
		BufferedReader fileReader = new BufferedReader(new FileReader(trainFile));
		ArrayList<TrainReview> trainReviews = new ArrayList<TrainReview>();
		
		//Gets each individual review and adds them into the trainReviews
		String trainReview;
		while ((trainReview = fileReader.readLine()) != null) {
			trainReviews.add(new TrainReview(trainReview));
		}
		fileReader.close();

		// Sets up the final IDF weights and then multiplies all the total frequencies in the trainReviews
		MovieReviewsMatrix trainMatrix = new MovieReviewsMatrix(trainReviews);
		
		// Opens test.dat to read them and add them to testReviews, and opens output.dat to put in my results
		File testFile = new File("C:\\Users\\Dennis\\JavaWorkspace\\MovieReviewer\\src\\test.dat");
		File outputFile = new File("C:\\Users\\Dennis\\JavaWorkspace\\MovieReviewer\\src\\output.dat");
		fileReader = new BufferedReader(new FileReader(testFile));
		BufferedWriter buff = new BufferedWriter(new FileWriter(outputFile));
		ArrayList<TestReview> testReviews = new ArrayList<TestReview>();
		
		// Adds the testReviews to testReviews
		String testReview;
		while ((testReview = fileReader.readLine()) != null) {
			testReviews.add(new TestReview(testReview, trainReviews, trainMatrix));
		}
		
		// Puts all of the results into output.dat
		for (int i = 0; i < testReviews.size(); i++)
			buff.write(testReviews.get(i).rating + "1" + System.lineSeparator());
		fileReader.close();
		buff.close();
	}
}