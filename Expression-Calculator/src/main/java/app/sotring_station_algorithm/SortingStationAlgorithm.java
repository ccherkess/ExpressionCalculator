package app.sotring_station_algorithm;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Stack;


/*
 * Алгоритм сортировочной станции
 * Задача: перевод инфиксной нотации в постфиксную
 * 
 * Выражение в инфиксной нотации: (5 + 10) * 5
 * Выражение в постфиксной нотации: 5 10 + 5 *
 */
public class SortingStationAlgorithm {
	
	
	
	//проверка является ли символ цифрой
	private static boolean isOperand(char value) {
		
		return 48 <= (int) value && (int) value <= 57;
		
	}
	
	//считывание оператора
	private static Operator inputOperator(int i, String input) throws ThereIsNoSuchOperatorException {
		
		StringBuilder tmp = new StringBuilder();
		
		while (i < input.length()
				&& !isOperand(input.charAt(i))
				&& input.charAt(i) != ' '
				&& input.charAt(i) != '('
				&& input.charAt(i) != ')') {
			
			tmp.append(input.charAt(i));
			
			i++;
			
		}
		
		String str = tmp.toString();
		
		Operator operator = null;
		
		for (Operator j: Operator.values()) {
			
			if (str.equals(j.str)) {
				
				operator = j;
				
			}
	
		}
		
		//если такого оператора нет в списке - кидаем исключение
		if (operator == null) throw new ThereIsNoSuchOperatorException(str);
		
		return operator;
		
	}
	
	//парсинг инфиксной нотации в постфиксную нотацию
	public static Queue<Object> parsingPostfixForm(String input) throws ErrorInExpressionException, ThereIsNoSuchOperatorException {
		
		Stack<Operator> stack = new Stack<>(); //стэк для операторов
		
		Queue<Object> output = new LinkedList<>(); //выходная очередь
		
		for (int i = 0; i < input.length(); i++) {
			
			//если текущий символ цифра - считываем число
			if (isOperand(input.charAt(i))) { 
				
				Double operand = (double) (input.charAt(i++) - 48); //считывание первой цифры числа
				
				int fractionalPart = 10;
				boolean fractionalFlag = false;
				int fractionalLen = 0;
				
				//считывание следующих цифр числа
				while (i < input.length() 
						&& (isOperand(input.charAt(i)) || input.charAt(i) == '.')) {
					
					if (input.charAt(i) == '.') {
						fractionalFlag = true;
					} else {
					
						if (!fractionalFlag) operand = operand * 10 + (input.charAt(i) - 48);
						else {
							operand += (input.charAt(i) - 48) / (double) fractionalPart;
							fractionalPart *= 10;
							fractionalLen++;
						}
						
					}
					
					i++;
						
				}
				
				/*
				 * нужно вернуть указатель на одну позицую назад,
				 * так как после считывания числа он указывает на первый токен после числа,
				 * которой не будет обработан!
				 */
				i--;
				
				//отбрасывание "мусора" после запятой
				operand = Double.parseDouble(String.format(Locale.ROOT, "%." + fractionalLen + "f", operand));
				
				if (stack.size() == 1 && output.isEmpty() && stack.peek() == Operator.MINUS) {
					
					stack.pop();
					
					operand *= -1;
					
				}
				
				//вывод числа в выходную очередь
				output.add(operand); 
				
			} else if (input.charAt(i) == '(') { 
				
				stack.push(Operator.OPENING_PARENTHESIS);
				
			} else if (input.charAt(i) == ')') {
				
				//пока на вершине стэка не лежит ( - выводим операторы в выходную учередь
				while (!stack.isEmpty() && stack.peek() != Operator.OPENING_PARENTHESIS) {
					
					output.add(stack.pop());
					
				}
				
				//если стэк пуст после вывода всех операторов, то кидаем исключение, так как отсутствует (
				if (stack.isEmpty()) throw new ErrorInExpressionException("the opening parenthesis is missing");
				
				stack.pop(); //выводим ( из стэка
				
				//если на вершине стэка лежит функция (приоритет функций равен 4) - выводим её в выходную очередь
				if (!stack.isEmpty() && stack.peek().priority == 4) output.add(stack.pop());
				
			} else if (input.charAt(i) != ' ') { //если встетили начало возможного оператора
				
				Operator operator = inputOperator(i, input); //считываем оператор
				
				i += operator.str.length() - 1; //перемещаём указатель на конец оператора
				
				if (operator.priority != 4) {  //если оператор не функция (приоритет функций равен 4)
				
					/*
					 * пока на вершине стэка лежит оператор,
					 * у которого приоритет >= приоритета считанного оператора,
					 * отправляем оператор с вершины стэка в выходную очередь
					 * 
					 * если на вершине стэка лежит открывающая скобка,
					 * то заканчиваем цикл
					 */
					while (!stack.isEmpty() 
							&& stack.peek().priority >= operator.priority
							&& stack.peek() != Operator.OPENING_PARENTHESIS) {
						
						output.add(stack.pop());
						
					}
					
				}
				
				stack.push(operator); //помещаем считанный оператор на вершину стэка
				
			}
			
		}
		
		
		/*
		 * пока стэк не пуст - помещаем операторы из стэка в выходную очередь
		 * если встретилась ( - то отсутствует закрывающая скобка
		 */
		while (!stack.isEmpty()) {
			
			if (stack.peek() == Operator.OPENING_PARENTHESIS)
				new ErrorInExpressionException("the opening parenthesis is missing");
			
			output.add(stack.pop());
			
		}
		
		return output;
		
	}
	
	/*
	 * Данная функция принимает на вход очередь объектов,
	 * которая представляет собой выражение в постфиксной нотации,
	 * и возвращает результат, посчитанного выражения
	 */
	public static double calculateExpression(Queue<Object> expression) throws NotEnoughArgumentsException {
		
		Stack<Double> stack = new Stack<Double>(); //стэк для хранения операндов
		
		while (!expression.isEmpty()) {
			
			//если встретился операнд, то помещаем его в стэк
			if (expression.peek().getClass() == Double.class) stack.push( (Double) expression.poll()); 
			else { //если встретился оператор
				
				Operator operator = (Operator) expression.poll(); //извлекаем оператор из стэка
				
				double[] operands = new double[operator.countOperand]; //создаем массив под операнды
				
				//заполняем массив в обратном порядке, так как они хранятся в стэке
				for (int i = operator.countOperand - 1; i >= 0 ; i--) {
					
					if (stack.isEmpty()) throw new NotEnoughArgumentsException(operator.countOperand - (i + 1));
					
					operands[i] = (Double) stack.pop();
				}
				
				//помещаем результат в стэк
				stack.push(Operator.executeOperator(operator, operands));
				
			}
			
		}
		
		return stack.pop(); //последней элемент в стэке - результат выражения
		
	}
}

