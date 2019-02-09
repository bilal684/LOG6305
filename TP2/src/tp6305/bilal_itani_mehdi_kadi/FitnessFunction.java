package tp6305.bilal_itani_mehdi_kadi;

import tp6305.bilal_itani_mehdi_kadi.Condition.Operator;

/**
 * This class contains the fitness function implementation using Wagener's approach
 * @author bitani & mkadi
 *
 */
public class FitnessFunction {

	/**
	 * This method returns the fitness value based on Wagener's approach.
	 * @param a left float operand
	 * @param op operator
	 * @param b right float operand
	 * @return fitness value
	 */
	public static float getFitness(float a, Operator op, float b) {
		float k = 1.0f; //This k value was chosen randomly. It could also be any other positive value. Choosing a negative value may affect our implementation.
		if (op == Operator.e) {
			if (a == b) {
				return 0.0f;
			} else {
				return Math.abs(a - b);
			}
		} else if (op == Operator.g) {
			if (a > b) {
				return 0;
			} else {
				return b - a + k;
			}
		} else if (op == Operator.ge) {
			if (a >= b) {
				return 0.0f;
			} else {
				return b - a;
			}
		} else if (op == Operator.l) {
			if (a < b) {
				return 0.0f;
			} else {
				return a - b + k;
			}
		} else if (op == Operator.le) {
			if (a <= b) {
				return 0.0f;
			} else {
				return a - b;
			}
		} else if (op == Operator.ne) {
			if (a != b) {
				return 0.0f;
			} else {
				return k;
			}
		}
		return 0.0f;
	}
}
