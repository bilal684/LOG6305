package tp6305.your_name;

import java.util.List;
import java.util.Random;

import tp6305.CoverageTest;

public class RandomCoverageTest extends CoverageTest {
	
	protected void generateTestData(StringBuilder builder, float[] testData) {
		randomData(builder, testData);
	}

	protected void randomData(StringBuilder builder, float[] testData) {
		
		TODO: 
		
		
		//Generate random numbers to each elements in testData, 
		//then put them in the StringBuilder as:		
		//builder.append(testData[i]).append(", ");
			
	}
	
	protected double computeBranchCoverage(List<String> instrumentingOutputs, String testData) {

		TODO: 
		// Compute it accumulatively.
		// Only system.out.println(testData) when they contribute to increase the coverage.

		return 0.0;
	}

	@Override
	protected double computeRACC(List<String> outputs, String testData) {
		return 0;
	}
	
}
