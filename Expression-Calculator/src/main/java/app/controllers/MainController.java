package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.sotring_station_algorithm.ErrorInExpressionException;
import app.sotring_station_algorithm.NotEnoughArgumentsException;
import app.sotring_station_algorithm.SortingStationAlgorithm;
import app.sotring_station_algorithm.ThereIsNoSuchOperatorException;

@Controller
public class MainController {
	
	@GetMapping()
	public String calculatorPage(Model model) {
		
		return "calculatorPage";
		
	}
	
	@PostMapping("/calculate")
	public String calculateExpression(@RequestParam("expression") String expression, Model model) {
		
		model.addAttribute("expression", expression);
		
		try {
			
			double result = SortingStationAlgorithm.calculateExpression(
					SortingStationAlgorithm.parsingPostfixForm(expression)
					);
			
			model.addAttribute("result", result);
			
		} catch (ErrorInExpressionException e) {
			
			model.addAttribute("error", e.getMessage());
			
		} catch (ThereIsNoSuchOperatorException e) {
			
			model.addAttribute("error", e.getMessage());
			
		}  catch (NotEnoughArgumentsException e) {
			
			model.addAttribute("error", e.getMessage());
			
		} catch (Exception e) {
			
			model.addAttribute("error", "error");
			
		}
		
		return "calculatorPage";
	}

}
