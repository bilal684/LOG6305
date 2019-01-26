package tp6305.your_name;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import tp6305.CoverageTest;

public class RandomCoverageTest extends CoverageTest {

	private static final int MIN = -10;
	private static final int MAX = 10;
	private double coverage = 0.0;
	
	public RandomCoverageTest()
	{
	}
	
	protected void generateTestData(StringBuilder builder, float[] testData) {
		randomData(builder, testData);
	}

	// TODO : Generate random numbers to each elements in testData
	protected void randomData(StringBuilder builder, float[] testData) 
	{	
		for (int i = 0; i < testData.length; i++) 
		{
			// Initialization of the random seed 
			Random rand = new Random();
			// Generate a random float number between -10 and 10
			float randomNumber = MIN + rand.nextFloat() * (MAX - MIN);
			// Round the generated number to 2 decimals 
			// used to increase the probability for getting more similar numbers 
			randomNumber = round(randomNumber, 2);
			// Insert the generated number in the testData
			testData[i] = randomNumber;
			// Put the generated number in the StringBuilder
			builder.append(randomNumber + ", ");
		}
	}
	
	private float round(float d, int decimalPlace) 
	{
		// function used to truncate a float number to 2 decimals.
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}

	// TODO: Compute branch coverage accumulatively.
	protected double computeBranchCoverage(List<String> instrumentingOutputs, String testData) 
	{
		// For each instrumented output check if it contains trace then add it to the set
		for(String s : instrumentingOutputs)
		{
			if(s.contains("trace"))
			{
				this.branchesTested.add(s);
			}
		}
		// Calculate current coverage accumulatively
		double currCoverage = ((double) this.branchesTested.size() / (double)TOTAL_BRANCH_NUM);
		// Only system.out.println(testData) when they contribute to increase the coverage
		if( currCoverage > coverage)
		{
			coverage = currCoverage;
			System.out.println(testData);
		}
		return coverage;
	}

	@Override
	protected double computeRACC(List<String> outputs, String testData) {
		return 0;
	}

}
