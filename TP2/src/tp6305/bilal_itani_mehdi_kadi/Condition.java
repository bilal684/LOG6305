package tp6305.bilal_itani_mehdi_kadi;

/**
 * This class represents a condition to be satisfied.
 * @author bitani & mkadi
 *
 */
public class Condition {

	/**
	 * This enumerator contains all the supported operators : (l : lower than, e : equal, g : greater than, le : lower or equal than, ge : greater or equal than, ne : not equal)
	 * @author bitani & mkadi
	 *
	 */
	public enum Operator {
		l, e, g, le, ge, ne
	}

	/**
	 * This enumerator contains all the operands supported by the condition, it consists or side1, side2, side3, side1plus2, side1plus3, side2plus3, zero
	 * @author bitani & mkadi
	 *
	 */
	public enum Operand {
		side1, side2, side3, side1plus2, side1plus3, side2plus3, zero
	}

	private Operator operator;
	private Operand leftOperand;
	private Operand rightOperand;

	/**
	 * @return the condition operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @return the left operand of the condition
	 */
	public Operand getLeftOperand() {
		return leftOperand;
	}

	/**
	 * sets the left operand
	 * @param leftOperand 
	 */
	public void setLeftOperand(Operand leftOperand) {
		this.leftOperand = leftOperand;
	}

	/**
	 * @return the right operand of the condition
	 */
	public Operand getRightOperand() {
		return rightOperand;
	}

	/**
	 * sets the right operand
	 * @param rightOperand 
	 */
	public void setRightOperand(Operand rightOperand) {
		this.rightOperand = rightOperand;
	}

	/**
	 * sets the operator
	 * @param operator 
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	
	/**
	 * Param constructor
	 * @param left left operand
	 * @param operator operator
	 * @param right right operand
	 */
	public Condition(Operand left, Operator operator, Operand right) {
		this.operator = operator;
		this.leftOperand = left;
		this.rightOperand = right;
	}

}
