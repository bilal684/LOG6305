package tp6305.bilal_itani_mehdi_kadi;

public class Condition {
	
	public enum operator {l, e, g, le, ge, ne}
	public enum operand {side1, side2, side3, side1plus2, side1plus3, side2plus3, zero}
	
	private operator operator;
	private operand leftOperand;
	private operand rightOperand;
	
	public operator getOperator()
	{
		return operator;
	}

	public operand getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(operand leftOperand) {
		this.leftOperand = leftOperand;
	}

	public operand getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(operand rightOperand) {
		this.rightOperand = rightOperand;
	}

	public void setOperator(operator operator) {
		this.operator = operator;
	}
	
	public Condition(operand left, operator operator, operand right)
	{
		this.operator = operator;
		this.leftOperand = left;
		this.rightOperand = right;
	}
	
}
