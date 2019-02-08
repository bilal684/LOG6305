package tp6305.bilal_itani_mehdi_kadi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tp6305.CoverageTest;
import tp6305.bilal_itani_mehdi_kadi.Condition.operand;
import tp6305.bilal_itani_mehdi_kadi.Condition.operator;

public class SearchBasedTest extends CoverageTest {

	private List<ExecPath> allPaths = new ArrayList<>();
	
	private static final int MIN = -10;
	private static final int MAX = 10;
	
	private int pathIdx = 0;
	
	@Override
	protected void generateTestData(StringBuilder builder, float[] testData) {
		// TODO Auto-generated method stub
		for(int i = 0; i < testData.length; i++)
		{
			Random rand = new Random();
			float randomNumber = MIN + rand.nextFloat() * (MAX - MIN);
			randomNumber = round(randomNumber, 2);
			testData[i] = randomNumber;
		}
		ExecPath path = allPaths.get(pathIdx);
		int processedConditions = 0;
		while (processedConditions < path.getAllConditions().size())
		{
			
		}
		pathIdx++;
	}

	@Override
	protected double computeBranchCoverage(List<String> outputs, String testData) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double computeRACC(List<String> outputs, String testData) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void createBranches()
	{
		//PATH 2 : {!C33, C36} - ret = ILLEGAL_ARGUMENTS
		ExecPath path2 = new ExecPath();
		path2.addPathCondition(operand.side1, operator.l, operand.zero);
		allPaths.add(path2); //Case where side1 < 0. Could also have been side2 < 0 or side 3 < 0
		
		//PATH 3 : {!C33, !C36, C49, C50} - ret = ILLEGAL
		ExecPath path3 = new ExecPath();
		path3.addPathCondition(operand.side1, operator.ge, operand.zero);
		path3.addPathCondition(operand.side2, operator.ge, operand.zero);
		path3.addPathCondition(operand.side3, operator.ge, operand.zero);
		path3.addPathCondition(operand.side1, operator.ne, operand.side2);
		path3.addPathCondition(operand.side2, operator.ne, operand.side3);
		path3.addPathCondition(operand.side1, operator.ne, operand.side3);
		path3.addPathCondition(operand.side1plus2, operator.l, operand.side3);
		allPaths.add(path3);
		
		//PATH 4 : {!C33, !C36, C49, !C50} - ret = SCALENE
		ExecPath path4 = new ExecPath();
		path4.addPathCondition(operand.side1, operator.ge, operand.zero);
		path4.addPathCondition(operand.side2, operator.ge, operand.zero);
		path4.addPathCondition(operand.side3, operator.ge, operand.zero);
		path4.addPathCondition(operand.side1, operator.ne, operand.side2);
		path4.addPathCondition(operand.side2, operator.ne, operand.side3);
		path4.addPathCondition(operand.side1, operator.ne, operand.side3);
		path4.addPathCondition(operand.side1plus2, operator.ge, operand.side3);
		path4.addPathCondition(operand.side2plus3, operator.ge, operand.side1);
		path4.addPathCondition(operand.side1plus3, operator.ge, operand.side2);
		allPaths.add(path4);
		
		//PATH 5 : {!C33, !C36, C40, C43, C46, !C49, C56} - ret = EQUILATERAL
		ExecPath path5 = new ExecPath();
		path5.addPathCondition(operand.side1, operator.ge, operand.zero);
		path5.addPathCondition(operand.side2, operator.ge, operand.zero);
		path5.addPathCondition(operand.side3, operator.ge, operand.zero);
		path5.addPathCondition(operand.side1, operator.e, operand.side2);
		path5.addPathCondition(operand.side2, operator.e, operand.side3);
		path5.addPathCondition(operand.side1, operator.e, operand.side3);
		allPaths.add(path5);
		
		//PATH 6 : {!C33, !C36, C40, !C49, !C56, C59} - ret = ISOCELES
		ExecPath path6 = new ExecPath();
		path6.addPathCondition(operand.side1, operator.ge, operand.zero);
		path6.addPathCondition(operand.side2, operator.ge, operand.zero);
		path6.addPathCondition(operand.side3, operator.ge, operand.zero);
		path6.addPathCondition(operand.side1, operator.e, operand.side2);
		path6.addPathCondition(operand.side2, operator.ne, operand.side3);
		path6.addPathCondition(operand.side1, operator.ne, operand.side3);
		path6.addPathCondition(operand.side1plus2, operator.g, operand.side3);
		allPaths.add(path6);
		
		//PATH 7 : {!C33, !C36, C43, !C49, !C56, !C59, C62} - ret = ISOCELES
		ExecPath path7 = new ExecPath();
		path7.addPathCondition(operand.side1, operator.ge, operand.zero);
		path7.addPathCondition(operand.side2, operator.ge, operand.zero);
		path7.addPathCondition(operand.side3, operator.ge, operand.zero);
		path7.addPathCondition(operand.side1, operator.ne, operand.side2);
		path7.addPathCondition(operand.side2, operator.e, operand.side3);
		path7.addPathCondition(operand.side1, operator.ne, operand.side3);
		path7.addPathCondition(operand.side2plus3, operator.g, operand.side1);
		allPaths.add(path7);
		
		//PATH 8 : {!C33, !C36, C46, !C49, !C56, !C59, !C62, C65} - RET = ISOCELES
		ExecPath path8 = new ExecPath();
		path8.addPathCondition(operand.side1, operator.ge, operand.zero);
		path8.addPathCondition(operand.side2, operator.ge, operand.zero);
		path8.addPathCondition(operand.side3, operator.ge, operand.zero);
		path8.addPathCondition(operand.side1, operator.ne, operand.side2);
		path8.addPathCondition(operand.side2, operator.ne, operand.side3);
		path8.addPathCondition(operand.side1, operator.e, operand.side3);
		path8.addPathCondition(operand.side1plus3, operator.g, operand.side2);
		allPaths.add(path8);
		
		//PATH 9 : {!C33, !C36, C46, !C49, !C56, !C59, !C62, !C65} - RET = ILLEGAL
		ExecPath path9 = new ExecPath();
		path9.addPathCondition(operand.side1, operator.ge, operand.zero);
		path9.addPathCondition(operand.side2, operator.ge, operand.zero);
		path9.addPathCondition(operand.side3, operator.ge, operand.zero);
		path9.addPathCondition(operand.side1, operator.ne, operand.side2);
		path9.addPathCondition(operand.side2, operator.ne, operand.side3);
		path9.addPathCondition(operand.side1, operator.e, operand.side3);
		path9.addPathCondition(operand.side1plus3, operator.le, operand.side2);
		allPaths.add(path9);
		
	}
	
	private float round(float d, int decimalPlace) 
	{
		// function used to truncate a float number to 2 decimals.
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	
	private float getFitness(float a, float b, operator op)
	{
		//TODO implement this.
		if(op == operator.e)
		{
			
		}
		else if (op == operator.g)
		{
			
		}
		else if (op == operator.ge)
		{
			
		}
		else if (op == operator.l)
		{
			
		}
		else if (op == operator.le)
		{
			
		}
		else if (op == operator.ne)
		{
			
		}
		return 0.0f;
	}

}
