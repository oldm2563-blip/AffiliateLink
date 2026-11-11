package ma.youcode.affiliatelink;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CalculatorTest {

    Calculator calculator = new Calculator();

    @ParameterizedTest
    @CsvSource({"12, 5, 17", "10, 2, 12", "1, 2, 3"})
    void addition(int a, int b, int expect){
            assertEquals(expect, calculator.add(a, b));
    }
}
