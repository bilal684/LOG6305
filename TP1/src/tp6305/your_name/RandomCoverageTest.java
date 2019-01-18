package tp6305.your_name;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import tp6305.CoverageTest;

public class RandomCoverageTest extends CoverageTest {

	private static final int MIN = -10;
	private static final int MAX = 10;
	private Random rand;
	private double coverage = 0.0;
	
	public RandomCoverageTest()
	{
		rand = new Random();
	}
	
	protected void generateTestData(StringBuilder builder, float[] testData) {
		randomData(builder, testData);
	}

	protected void randomData(StringBuilder builder, float[] testData) {

		// TODO:
		// Generate random numbers to each elements in testData,
		// then put them in the StringBuilder as:
		// builder.append(testData[i]).append(", ");
		for (int i = 0; i < testData.length; i++) {
			float randomNumber = MIN + rand.nextFloat() * (MAX - MIN);
			randomNumber = round(randomNumber, 2);
			testData[i] = randomNumber;
			builder.append(randomNumber + ", ");
		}

	}
	
	private float round(float d, int decimalPlace) {
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}

	protected double computeBranchCoverage(List<String> instrumentingOutputs, String testData) {

		// TODO:
		// Compute it accumulatively.
		// Only system.out.println(testData) when they contribute to increase the
		// coverage.
		for(String s : instrumentingOutputs)
		{
			if(s.contains("trace"))
			{
				this.branchesTested.add(s);
			}
		}
		double currCoverage = ((double) this.branchesTested.size() / (double)this.TOTAL_BRANCH_NUM);
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
