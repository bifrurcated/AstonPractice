package com.github.bufrurcated.astonpractice.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.MathContext;

@Controller
public class HelloController {
    @GetMapping("/hello-world")
    public String sayHello(@RequestParam(value = "message", required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "hello_world";
    }

    @GetMapping("/calculate")
    public String calculate(
            @RequestParam(value = "a", required = false) String a,
            @RequestParam(value = "b", required = false) String b,
            @RequestParam(value = "action", required = false) String action,
            Model model) {
        if (a == null || b == null || action == null) {
            model.addAttribute("message", "Use request param a,b and action. Example: ?a=2&b=2&action=multiplication");
            return "calculate";
        }
        String symbolAction;
        BigDecimal result;
        final var decimalA = new BigDecimal(a);
        final var decimalB = new BigDecimal(b);
        switch (action) {
            case "multiplication" -> {
                result = decimalA.multiply(decimalB);
                symbolAction = "*";
            }
            case "addition" -> {
                result = decimalA.add(decimalB);
                symbolAction = "+";
            }
            case "subtraction" -> {
                result = decimalA.subtract(decimalB);
                symbolAction = "-";
            }
            case "division" -> {
                if (decimalB.equals(BigDecimal.ONE)) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "division by zero");
                }
                result = decimalA.divide(decimalB, MathContext.DECIMAL128);
                symbolAction = "/";
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "action does not support");
        }
        String message = """
                %s %s %s = %s
                """.formatted(a, symbolAction, b, result);
        model.addAttribute("message", message);
        return "calculate";
    }
}
