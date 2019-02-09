package tp6305.bilal_itani_mehdi_kadi;

import tp6305.bilal_itani_mehdi_kadi.Condition.operator;

public class FitnessFunction {

	public static float getFitness(float a, operator op, float b) {
		float k = 1.0f;
		if (op == operator.e) {
			if (a == b) {
				return 0.0f;
			} else {
				return Math.abs(a - b);
			}
		} else if (op == operator.g) {
			if (a > b) {
				return 0;
			} else {
				return b - a + k;
			}
		} else if (op == operator.ge) {
			if (a >= b) {
				return 0.0f;
			} else {
				return b - a;
			}
		} else if (op == operator.l) {
			if (a < b) {
				return 0.0f;
			} else {
				return a - b + k;
			}
		} else if (op == operator.le) {
			if (a <= b) {
				return 0.0f;
			} else {
				return a - b;
			}
		} else if (op == operator.ne) {
			if (a != b) {
				return 0.0f;
			} else {
				return k;
			}
		}
		return 0.0f;
	}
}
