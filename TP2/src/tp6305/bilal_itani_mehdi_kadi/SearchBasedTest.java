package tp6305.bilal_itani_mehdi_kadi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tp6305.CoverageTest;
import tp6305.bilal_itani_mehdi_kadi.Condition.Operand;
import tp6305.bilal_itani_mehdi_kadi.Condition.Operator;

/**
 * This class contains the implementation of SearchBasedTesting using Wagener's approach.
 * @author bitani & mkadi
 *
 */
public class SearchBasedTest extends CoverageTest {

	private List<ExecPath> allPaths = new ArrayList<>(); //All paths will be in this list. Each path has a set of conditions
	
	//Min and max boundaries, these were defined randomly.
	private static final int MIN = -1; 
	private static final int MAX = 100;
	Map<String, Float> data = new HashMap<>(); //A hashmap for storing the data (e.g side1, side2, side3, side1plus2, side1plus3, side2plus3 and zero).
	private int pathIdx = 0; //This is used to iterate over the paths in the arraylist containing all the execution path
	private double coverage = 0.0;

	public SearchBasedTest() {
		iterationNum = 0;
		createBranches(); //Create all branches.
	}

	@Override
	protected void reset() {
		iterationNum = 0;
		pathIdx = 0;
	}

	@Override
	protected void generateTestData(StringBuilder builder, float[] testData) {
		// TODO Auto-generated method stub
		ExecPath path = allPaths.get(pathIdx); //We first get a path from the list.
		int satisfiedConditions = 0; //This is used to count all satisfied conditions with the current data in the data hashmap.
		initializeRandomData(); //Here, we generate random data and store that into the data hashmap.
		int internalIt = 0; //This is a counter, it will be used if we ever get lost and not finding a solution.
		while (satisfiedConditions < path.getAllConditions().size()) { 
			for (Condition c : path.getAllConditions()) { //For each condition that consist the execution path
				internalIt++; 
				if (internalIt > 5000) // in case we get stuck and can't find an answer, we restart with a new random solution.
				{
					internalIt = 0;
					pathIdx = 0;
					initializeRandomData();
					satisfiedConditions = 0;
					iterationNum++;
					break;
				}
				if (checkForDataInconsistency()) // Checks if the data inside the hashmap is consistent with the MIN MAX values we defined.
				{
					satisfiedConditions = 0; // e.g we have to rollback and verify all previously satisfied conditions.
					initializeRandomData(); //we generate a new set of data.
					iterationNum++;
					break;
				}
				//This calculates the fitness value for the condition we are currently processing.
				float fitness = FitnessFunction.getFitness(data.get(c.getLeftOperand().name()), c.getOperator(),
						data.get(c.getRightOperand().name()));
				if (fitness == 0.0f) { //If fitness is 0, great we satisfied the condition
					satisfiedConditions++;
				} else // we have to re-adjust the values of the sides accordingly. Notice here that the way we built our conditions helps us a lot. 
					//The right operand can only be a side1, side2 or side3. That way, we only act on the right operand and not modify data such as side1plus2 or side1plus3 when applying the fitness function.
				{
					//For the equal operator, if the right operand is larger than the left operand, that means that the right operand needs to lose fitness to be equal to the left operand.
					//Otherwise, if the right operand is small than the left one, then we have to add the fitness to the right operator so it becomes equal to the left operand.
					//The same approach is done for the other operators, these were all determined by testing each specific case on a paper.
					if (c.getOperator() == Operator.e) {
						if (data.get(c.getRightOperand().name()) > data.get(c.getLeftOperand().name())) {
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
						} else {
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
						}
					} else if (c.getOperator() == Operator.g) {
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
					} else if (c.getOperator() == Operator.ge) {
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
					} else if (c.getOperator() == Operator.l) {
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
					} else if (c.getOperator() == Operator.le) {
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
					} else if (c.getOperator() == Operator.ne) {
						if (data.get(c.getRightOperand().name()) > data.get(c.getLeftOperand().name())) {
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
						} else {
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
						}
					}
					//Once we fix the right operand (side1, side2 or side3 because these are the only possible right operand), 
					//we have to recalculate side1plus2, side1plus3 and side2plus3, hence why we call this method.
					fixSums();
					satisfiedConditions = 0;
					iterationNum++;
					break;
				}
			}
		}
		//Put the accepted data into the testData array
		testData[0] = data.get(Operand.side1.name());
		testData[1] = data.get(Operand.side2.name());
		testData[2] = data.get(Operand.side3.name());
		for (int j = 0; j < 3; j++) {
			builder.append(testData[j]).append(", ");
		}
		pathIdx++; //This is incremented so we can satisfy the next execution path.
	}

	@Override
	protected double computeBranchCoverage(List<String> instrumentingOutputs, String testData) {
		// TODO Auto-generated method stub
		// For each instrumented output check if it contains trace then add it to the
		// set
		for (String s : instrumentingOutputs) {
			if (s.contains("trace")) {
				branchesTested.add(s);
			}
		}
		// Calculate current coverage accumulatively
		double currCoverage = ((double) branchesTested.size() / (double) TOTAL_BRANCH_NUM);
		// Only system.out.println(testData) when they contribute to increase the
		// coverage
		if (currCoverage > coverage) {
			coverage = currCoverage;
			System.out.println(testData);
		}
		return coverage;
	}

	@Override
	protected double computeRACC(List<String> outputs, String testData) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void createBranches() {
		
		//Pay attention to the right operand of each condition in the paths, we have made it so it could only be side1, side2 or side3, this is important when applying
		//the fitness function, as we do not want to apply the fitness to a sum of sides, or even to zero.
		
		// PATH 1 {C33} IS ALREADY HANDLED BY THE CODE!, we do not have to manually add an execution path to check if data.length != 3.
		// PATH 2 : {!C33, C36} - ret = ILLEGAL_ARGUMENTS
		ExecPath path2 = new ExecPath();
		path2.addPathCondition(Operand.zero, Operator.g, Operand.side1);
		allPaths.add(path2); // Case where side1 < 0. Could also have been side2 < 0 or side 3 < 0

		// PATH 3 : {!C33, !C36, C49, C50} - ret = ILLEGAL
		ExecPath path3 = new ExecPath();
		path3.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path3.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path3.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path3.addPathCondition(Operand.side1, Operator.ne, Operand.side2);
		path3.addPathCondition(Operand.side2, Operator.ne, Operand.side3);
		path3.addPathCondition(Operand.side1, Operator.ne, Operand.side3);
		path3.addPathCondition(Operand.side1plus2, Operator.l, Operand.side3);
		allPaths.add(path3);

		// PATH 4 : {!C33, !C36, C49, !C50} - ret = SCALENE
		ExecPath path4 = new ExecPath();
		path4.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path4.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path4.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path4.addPathCondition(Operand.side1, Operator.ne, Operand.side2);
		path4.addPathCondition(Operand.side2, Operator.ne, Operand.side3);
		path4.addPathCondition(Operand.side1, Operator.ne, Operand.side3);
		path4.addPathCondition(Operand.side1plus2, Operator.ge, Operand.side3);
		path4.addPathCondition(Operand.side2plus3, Operator.ge, Operand.side1);
		path4.addPathCondition(Operand.side1plus3, Operator.ge, Operand.side2);
		allPaths.add(path4);

		// PATH 5 : {!C33, !C36, C40, C43, C46, !C49, C56} - ret = EQUILATERAL
		ExecPath path5 = new ExecPath();
		path5.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path5.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path5.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path5.addPathCondition(Operand.side1, Operator.e, Operand.side2);
		path5.addPathCondition(Operand.side2, Operator.e, Operand.side3);
		path5.addPathCondition(Operand.side1, Operator.e, Operand.side3);
		allPaths.add(path5);

		// PATH 6 : {!C33, !C36, C40, !C49, !C56, C59} - ret = ISOCELES
		ExecPath path6 = new ExecPath();
		path6.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path6.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path6.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path6.addPathCondition(Operand.side1, Operator.e, Operand.side2);
		path6.addPathCondition(Operand.side2, Operator.ne, Operand.side3);
		path6.addPathCondition(Operand.side1, Operator.ne, Operand.side3);
		path6.addPathCondition(Operand.side1plus2, Operator.g, Operand.side3);
		allPaths.add(path6);

		// PATH 7 : {!C33, !C36, C43, !C49, !C56, !C59, C62} - ret = ISOCELES
		ExecPath path7 = new ExecPath();
		path7.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path7.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path7.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path7.addPathCondition(Operand.side1, Operator.ne, Operand.side2);
		path7.addPathCondition(Operand.side2, Operator.e, Operand.side3);
		path7.addPathCondition(Operand.side1, Operator.ne, Operand.side3);
		path7.addPathCondition(Operand.side2plus3, Operator.g, Operand.side1);
		allPaths.add(path7);

		// PATH 8 : {!C33, !C36, C46, !C49, !C56, !C59, !C62, C65} - RET = ISOCELES
		ExecPath path8 = new ExecPath();
		path8.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path8.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path8.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path8.addPathCondition(Operand.side1, Operator.ne, Operand.side2);
		path8.addPathCondition(Operand.side2, Operator.ne, Operand.side3);
		path8.addPathCondition(Operand.side1, Operator.e, Operand.side3);
		path8.addPathCondition(Operand.side1plus3, Operator.g, Operand.side2);
		allPaths.add(path8);

		// PATH 9 : {!C33, !C36, C46, !C49, !C56, !C59, !C62, !C65} - RET = ILLEGAL
		ExecPath path9 = new ExecPath();
		path9.addPathCondition(Operand.zero, Operator.l, Operand.side1);
		path9.addPathCondition(Operand.zero, Operator.l, Operand.side2);
		path9.addPathCondition(Operand.zero, Operator.l, Operand.side3);

		path9.addPathCondition(Operand.side1, Operator.ne, Operand.side2);
		path9.addPathCondition(Operand.side2, Operator.ne, Operand.side3);
		path9.addPathCondition(Operand.side1, Operator.e, Operand.side3);
		path9.addPathCondition(Operand.side1plus3, Operator.le, Operand.side2);
		allPaths.add(path9);

	}

	/**
	 * Method used to truncate a float number to 2 decimals.
	 * @param d - the float value we want to round
	 * @param decimalPlace - number of decimals
	 * @return Rounded float.
	 */
	private float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	/**
	 *This method initializes random data for side1, side2 and side3 in the data hashmap. 
	 */
	private void initializeRandomData() {
		data.put(Operand.zero.name(), 0.0f);
		Random rand = new Random();
		data.put(Operand.side1.name(), round(MIN + rand.nextFloat() * (MAX - MIN), 2)); //side 1 is a random float between MIN and MAX
		rand = new Random(); // To reinitiate the seed.
		data.put(Operand.side2.name(), round(MIN + rand.nextFloat() * (MAX - MIN), 2)); //side 2 is a random float between MIN and MAX
		rand = new Random(); // To reinitiate the seed.
		data.put(Operand.side3.name(), round(MIN + rand.nextFloat() * (MAX - MIN), 2)); //side 3 is a random float between MIN and MAX
		fixSums();
	}

	//
	/**
	 * This method ensures that the data in the data hashmap are consistent with the boundaries MIN and MAX.
	 * @return true when inconsistent, false otherwise.
	 */
	private boolean checkForDataInconsistency() {
		return (data.get(Operand.side1.name()) > MAX || data.get(Operand.side1.name()) < MIN
				|| data.get(Operand.side2.name()) > MAX || data.get(Operand.side2.name()) < MIN
				|| data.get(Operand.side3.name()) > MAX || data.get(Operand.side3.name()) < MIN);
	}

	/**
	 * This method is used to update side1plus2, side1plus3 and side2plus3 so these values are consistent with the actual data of side1, side2 and side3 in the data hashmap.
	 */
	private void fixSums() {
		data.put(Operand.side1plus2.name(), data.get(Operand.side1.name()) + data.get(Operand.side2.name()));
		data.put(Operand.side1plus3.name(), data.get(Operand.side1.name()) + data.get(Operand.side3.name()));
		data.put(Operand.side2plus3.name(), data.get(Operand.side2.name()) + data.get(Operand.side3.name()));
	}
}
