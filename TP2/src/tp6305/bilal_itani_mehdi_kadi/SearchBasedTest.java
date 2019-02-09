package tp6305.bilal_itani_mehdi_kadi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tp6305.CoverageTest;
import tp6305.bilal_itani_mehdi_kadi.Condition.operand;
import tp6305.bilal_itani_mehdi_kadi.Condition.operator;

public class SearchBasedTest extends CoverageTest {

	private List<ExecPath> allPaths = new ArrayList<>();
	
	private static final int MIN = -1;
	private static final int MAX = 30;
	Map<String, Float> data = new HashMap<>();
	private int pathIdx = 0;
	private double coverage = 0.0;
	

	
	public SearchBasedTest()
	{
		iterationNum=0;
		createBranches();
	}
	
	@Override
	protected void reset() {
		iterationNum=0;
		pathIdx=0;
	}
	
	@Override
	protected void generateTestData(StringBuilder builder, float[] testData) {
		// TODO Auto-generated method stub
		ExecPath path = allPaths.get(pathIdx);
		int satisfiedConditions = 0;
		initializeRandomData();
		int internalIt = 0;
		while (satisfiedConditions < path.getAllConditions().size())
		{
			for(Condition c : path.getAllConditions())
			{
				internalIt++;
				if(internalIt > 5000) // in case we get stuck and can't find an answer.
				{
					internalIt = 0;
					reset();
					initializeRandomData();
					iterationNum++;
					break;
				}
				if(checkForDataInconsistency()) //Checks if we have data that 
				{
					satisfiedConditions = 0; //e.g we have to go back and see older conditions.
					initializeRandomData();
					iterationNum++;
					break;
				}
				float fitness = getFitness(data.get(c.getLeftOperand().name()), c.getOperator(), data.get(c.getRightOperand().name()));
				if(fitness == 0.0f)
				{
					satisfiedConditions++;
				}
				else //we have to re-adjust the values of the sides accordingly.
				{
					if(c.getOperator() == operator.e) 
					{
						if(data.get(c.getRightOperand().name()) > data.get(c.getLeftOperand().name()))
						{
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
						}
						else
						{
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
						}
					}
					else if (c.getOperator() == operator.g)
					{
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
					}
					else if(c.getOperator() == operator.ge)
					{
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
					}
					else if (c.getOperator() == operator.l)
					{
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
					}
					else if(c.getOperator() == operator.le)
					{
						data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
					}
					else if (c.getOperator() == operator.ne)
					{
						if(data.get(c.getRightOperand().name()) > data.get(c.getLeftOperand().name()))
						{
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) - fitness);
						}
						else
						{
							data.put(c.getRightOperand().name(), data.get(c.getRightOperand().name()) + fitness);
						}
					}
					fixSums();
					satisfiedConditions = 0;
					iterationNum++;
					break;
				}
			}
			
		}
		testData[0] = data.get(operand.side1.name());
		testData[1] = data.get(operand.side2.name());
		testData[2] = data.get(operand.side3.name());
		for (int j=0;j<3;j++)
		{
			builder.append(testData[j]).append(", ");
		}
		pathIdx++;
	}

	@Override
	protected double computeBranchCoverage(List<String> instrumentingOutputs, String testData) {
		// TODO Auto-generated method stub
		// For each instrumented output check if it contains trace then add it to the set
		for(String s : instrumentingOutputs)
		{
			if(s.contains("trace"))
			{
				branchesTested.add(s);
			}
		}
		// Calculate current coverage accumulatively
		double currCoverage = ((double) branchesTested.size() / (double)TOTAL_BRANCH_NUM);
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
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void createBranches()
	{
		//PATH 1 IS ALREADY HANDLED BY THE CODE!
		//PATH 2 : {!C33, C36} - ret = ILLEGAL_ARGUMENTS
		ExecPath path2 = new ExecPath();
		path2.addPathCondition(operand.zero, operator.g, operand.side1);
		allPaths.add(path2); //Case where side1 < 0. Could also have been side2 < 0 or side 3 < 0
		
		//PATH 3 : {!C33, !C36, C49, C50} - ret = ILLEGAL
		ExecPath path3 = new ExecPath();
		path3.addPathCondition(operand.zero, operator.l, operand.side1);
		path3.addPathCondition(operand.zero, operator.l, operand.side2);
		path3.addPathCondition(operand.zero, operator.l, operand.side3);
		
		path3.addPathCondition(operand.side1, operator.ne, operand.side2);
		path3.addPathCondition(operand.side2, operator.ne, operand.side3);
		path3.addPathCondition(operand.side1, operator.ne, operand.side3);
		path3.addPathCondition(operand.side1plus2, operator.l, operand.side3);
		allPaths.add(path3);
		
		//PATH 4 : {!C33, !C36, C49, !C50} - ret = SCALENE
		ExecPath path4 = new ExecPath();
		path4.addPathCondition(operand.zero, operator.l, operand.side1);
		path4.addPathCondition(operand.zero, operator.l, operand.side2);
		path4.addPathCondition(operand.zero, operator.l, operand.side3);
		
		path4.addPathCondition(operand.side1, operator.ne, operand.side2);
		path4.addPathCondition(operand.side2, operator.ne, operand.side3);
		path4.addPathCondition(operand.side1, operator.ne, operand.side3);
		path4.addPathCondition(operand.side1plus2, operator.ge, operand.side3);
		path4.addPathCondition(operand.side2plus3, operator.ge, operand.side1);
		path4.addPathCondition(operand.side1plus3, operator.ge, operand.side2);
		allPaths.add(path4);
		
		//PATH 5 : {!C33, !C36, C40, C43, C46, !C49, C56} - ret = EQUILATERAL
		ExecPath path5 = new ExecPath();
		path5.addPathCondition(operand.zero, operator.l, operand.side1);
		path5.addPathCondition(operand.zero, operator.l, operand.side2);
		path5.addPathCondition(operand.zero, operator.l, operand.side3);
		
		path5.addPathCondition(operand.side1, operator.e, operand.side2);
		path5.addPathCondition(operand.side2, operator.e, operand.side3);
		path5.addPathCondition(operand.side1, operator.e, operand.side3);
		allPaths.add(path5);
		
		//PATH 6 : {!C33, !C36, C40, !C49, !C56, C59} - ret = ISOCELES
		ExecPath path6 = new ExecPath();
		path6.addPathCondition(operand.zero, operator.l, operand.side1);
		path6.addPathCondition(operand.zero, operator.l, operand.side2);
		path6.addPathCondition(operand.zero, operator.l, operand.side3);
		
		path6.addPathCondition(operand.side1, operator.e, operand.side2);
		path6.addPathCondition(operand.side2, operator.ne, operand.side3);
		path6.addPathCondition(operand.side1, operator.ne, operand.side3);
		path6.addPathCondition(operand.side1plus2, operator.g, operand.side3);
		allPaths.add(path6);
		
		//PATH 7 : {!C33, !C36, C43, !C49, !C56, !C59, C62} - ret = ISOCELES
		ExecPath path7 = new ExecPath();
		path7.addPathCondition(operand.zero, operator.l, operand.side1);
		path7.addPathCondition(operand.zero, operator.l, operand.side2);
		path7.addPathCondition(operand.zero, operator.l, operand.side3);
		
		path7.addPathCondition(operand.side1, operator.ne, operand.side2);
		path7.addPathCondition(operand.side2, operator.e, operand.side3);
		path7.addPathCondition(operand.side1, operator.ne, operand.side3);
		path7.addPathCondition(operand.side2plus3, operator.g, operand.side1);
		allPaths.add(path7);
		
		//PATH 8 : {!C33, !C36, C46, !C49, !C56, !C59, !C62, C65} - RET = ISOCELES
		ExecPath path8 = new ExecPath();
		path8.addPathCondition(operand.zero, operator.l, operand.side1);
		path8.addPathCondition(operand.zero, operator.l, operand.side2);
		path8.addPathCondition(operand.zero, operator.l, operand.side3);
		
		path8.addPathCondition(operand.side1, operator.ne, operand.side2);
		path8.addPathCondition(operand.side2, operator.ne, operand.side3);
		path8.addPathCondition(operand.side1, operator.e, operand.side3);
		path8.addPathCondition(operand.side1plus3, operator.g, operand.side2);
		allPaths.add(path8);
		
		//PATH 9 : {!C33, !C36, C46, !C49, !C56, !C59, !C62, !C65} - RET = ILLEGAL
		ExecPath path9 = new ExecPath();
		path9.addPathCondition(operand.zero, operator.l, operand.side1);
		path9.addPathCondition(operand.zero, operator.l, operand.side2);
		path9.addPathCondition(operand.zero, operator.l, operand.side3);
		
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
	
	private float getFitness(float a, operator op, float b)
	{
		float k = 1.0f;
		if(op == operator.e)
		{
			if(a == b)
			{
				return 0.0f;
			}
			else
			{
				return Math.abs(a - b);
			}
		}
		else if (op == operator.g)
		{
			if(a > b)
			{
				return 0;
			}
			else
			{
				return b - a + k;
			}
		}
		else if (op == operator.ge)
		{
			if( a >= b)
			{
				return 0.0f;
			}
			else
			{
				return b-a;
			}
		}
		else if (op == operator.l)
		{
			if(a < b)
			{
				return 0.0f;
			}
			else
			{
				return a - b + k;
			}
		}
		else if (op == operator.le)
		{
			if(a <= b)
			{
				return 0.0f;
			}
			else
			{
				return a - b;
			}
		}
		else if (op == operator.ne)
		{
			if(a != b)
			{
				return 0.0f;
			}
			else
			{
				return k;
			}
		}
		return 0.0f;
	}

	
	private void initializeRandomData()
	{
		data.put(operand.zero.name(), 0.0f);
		Random rand = new Random();		
		data.put(operand.side1.name(), round(MIN + rand.nextFloat() * (MAX - MIN), 2));
		rand = new Random(); //To reinitiate the seed.
		data.put(operand.side2.name(), round(MIN + rand.nextFloat() * (MAX - MIN), 2));
		rand = new Random(); //To reinitiate the seed.
		data.put(operand.side3.name(), round(MIN + rand.nextFloat() * (MAX - MIN), 2));
		fixSums();
	}
	
	private boolean checkForDataInconsistency()
	{
		return (
				data.get(operand.side1.name()) > MAX ||
				data.get(operand.side1.name()) < MIN ||
				data.get(operand.side2.name()) > MAX ||
				data.get(operand.side2.name()) < MIN ||
				data.get(operand.side3.name()) > MAX ||
				data.get(operand.side3.name()) < MIN);
	}
	
	private void fixSums()
	{
		data.put(operand.side1plus2.name(), data.get(operand.side1.name()) + data.get(operand.side2.name()));
		data.put(operand.side1plus3.name(), data.get(operand.side1.name()) + data.get(operand.side3.name()));
		data.put(operand.side2plus3.name(), data.get(operand.side2.name()) + data.get(operand.side3.name()));
	}
}
