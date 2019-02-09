package tp6305.bilal_itani_mehdi_kadi;

import java.util.ArrayList;
import java.util.List;

import tp6305.bilal_itani_mehdi_kadi.Condition.operand;
import tp6305.bilal_itani_mehdi_kadi.Condition.operator;

public class ExecPath {
	private List<Condition> allConditions = new ArrayList<>();

	public List<Condition> getAllConditions() {
		return allConditions;
	}

	public void setAllConditions(List<Condition> allConditions) {
		this.allConditions = allConditions;
	}

	public void addPathCondition(operand left, operator op, operand right) {
		allConditions.add(new Condition(left, op, right));
	}
}
