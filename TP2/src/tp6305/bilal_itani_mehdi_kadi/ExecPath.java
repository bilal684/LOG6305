package tp6305.bilal_itani_mehdi_kadi;

import java.util.ArrayList;
import java.util.List;

import tp6305.bilal_itani_mehdi_kadi.Condition.Operand;
import tp6305.bilal_itani_mehdi_kadi.Condition.Operator;

/**
 * This class represents an execution path to be tested.
 * @author bitani & mkadi
 *
 */
public class ExecPath {
	private List<Condition> allConditions = new ArrayList<>(); //This arraylist contains all the conditions of the execution path

	/**
	 * 
	 * @return all conditions of the execution path
	 */
	public List<Condition> getAllConditions() {
		return allConditions;
	}

	/**
	 * Sets all conditions of the execution path.
	 * @param allConditions
	 */
	public void setAllConditions(List<Condition> allConditions) {
		this.allConditions = allConditions;
	}
 
	/**
	 * This method adds a condition to the execution path.
	 * @param left left operand of the condition
	 * @param op operator used for the condition
	 * @param right right operand of the condition
	 */
	public void addPathCondition(Operand left, Operator op, Operand right) {
		allConditions.add(new Condition(left, op, right));
	}
}
