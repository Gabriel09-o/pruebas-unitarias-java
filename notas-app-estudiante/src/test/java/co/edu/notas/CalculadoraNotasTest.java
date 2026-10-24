package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraNotasTest {

    private CalculadoraNotas calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraNotas(); // objeto nuevo para cada prueba
    }

    @Test
    @DisplayName("Un estudiante con 3.5 aprueba")
    void estaAprobado_notaMayorATres_retornaTrue() {
        assertTrue(calculadora.estaAprobado(3.5));
    }

    @Test
    @DisplayName("Un estudiante con 2.9 reprueba")
    void estaAprobado_notaMenorATres_retornaFalse() {
        assertFalse(calculadora.estaAprobado(2.9));
    }

    @Test
    @DisplayName("Caso límite: exactamente 3.0 aprueba")
    void estaAprobado_notaExactamenteTres_retornaTrue() {
        assertTrue(calculadora.estaAprobado(3.0));
    }

    @Test
    @DisplayName("Calcula la nota final ponderada 30/30/40")
    void calcularNotaFinal_notasValidas_retornaPromedioPonderado() {
        // Arrange
        double seguimiento = 4.0, parcial = 3.0, proyecto = 5.0;
        // Act
        double resultado = calculadora.calcularNotaFinal(seguimiento, parcial, proyecto);
        // Assert
        assertEquals(4.1, resultado, 0.001);
    }
    // ↓ Las pruebas de los pasos 7, 8 y 9 se pegan AQUÍ, antes de la última llave }
}