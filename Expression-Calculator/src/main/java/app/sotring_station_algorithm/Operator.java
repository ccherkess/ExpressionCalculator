package app.sotring_station_algorithm;

//перечисление, описывающие операторы
public enum Operator {
	
	PLUS(1, "+", 2),  // +
	MINUS(1, "-", 2), // -
	MULTIPLICATION(2, "*", 2), // *
	DIVISION(2, "/", 2),  // /
	EXPONENTION(3, "^", 2), // ^
	SIN(4, "sin", 1), // sin
	COS(4, "cos", 1), // cos
	OPENING_PARENTHESIS(5, "(", 0), // (
	CLOSING_PARENTHESIS(5, ")", 0); // )
	
	public int priority; // приоритет оператора
	public String str; // строковое представление оператора
	public int countOperand; //количество необходимых операндов
	
	Operator(int priority, String str, int countOperand) {
		
		this.priority = priority;
		this.str = str;
		this.countOperand = countOperand;
		
	}
	
	//функция, выполняющая оператор
	public static double executeOperator(Operator operator, double[] operands) {
		
		double result = 0;
		
		switch(operator) {
		
		case PLUS:
			result = operands[0] + operands[1];
			break;

		case MINUS:
			result = operands[0] - operands[1];
			break;

		case MULTIPLICATION:
			result = operands[0] * operands[1];
			break;

		case DIVISION:
			result = operands[0] / operands[1];
			break;

		case EXPONENTION:
			result = Math.pow(operands[0], operands[1]);
			break;
		
		case SIN:
			result = Math.sin(operands[0]);
			break;

		case COS:
			result = Math.cos(operands[0]);
			break;
		default:
			break;
		}
		
		return result;
		
	}
	
	@Override
	public String toString() {
		
		return str;
		
	}
}
	
	