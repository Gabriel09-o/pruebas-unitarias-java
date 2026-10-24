package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
* ENTREGA DEL TALLER
*
* Nombre completo: Jesus Gabriel Cedeño Guillen
*
* 1. Bug encontrado (método y línea de CarritoCompras.java):
*    Método agregarProducto(), en la validación de la cantidad.
*    La condición "if (cantidad < 0)" permite que cantidad = 0 pase sin error,
*    cuando la regla R1 exige cantidad > 0 (debía ser "if (cantidad <= 0)").
*
* 2. Prueba que lo detectó (nombre del método): agregarProducto_cantidadCero_lanzaExcepcion

* 3. Sugerencia de la IA que rechacé o corregí, y por qué
*    (si todas eran correctas: cómo verifiqué una de ellas con las reglas):
*    [completa esto con tu propia experiencia al usar el prompt de la Sección 5 —
*    por ejemplo, si la IA propuso una prueba que asumía "cantidad >= 0" es válido
*    (reproduciendo el bug en vez de la regla), explica que la rechazaste porque
*    la regla R1 dice "mayor a 0", no "mayor o igual a 0"]
*/
class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() { carrito = new CarritoCompras(); }

    // ====== R1: validaciones de agregarProducto ======

    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
        // Arrange
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);

        // Act
        double subtotal = carrito.calcularSubtotal();

        // Assert
        assertEquals(34_000, subtotal, 0.001);
    }

    @Test
    @DisplayName("Rechaza un producto con nombre nulo")
    void agregarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> carrito.agregarProducto(null, 8_000, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con nombre en blanco")
    void agregarProducto_nombreEnBlanco_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> carrito.agregarProducto("   ", 8_000, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con precio cero o negativo")
    void agregarProducto_precioInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> carrito.agregarProducto("Cuaderno", 0, 1));
    }

    @Test
    @DisplayName("Rechaza un producto con cantidad igual a cero")
    void agregarProducto_cantidadCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> carrito.agregarProducto("Cuaderno", 8_000, 0));
    }

    @Test
    @DisplayName("Cuenta correctamente la cantidad de items agregados")
    void cantidadDeItems_dosProductos_retornaDos() {
        carrito.agregarProducto("Cuaderno", 8_000, 1);
        carrito.agregarProducto("Esfero", 2_000, 1);
        assertEquals(2, carrito.cantidadDeItems());
    }

    // ====== R2: subtotal ======

    @Test
    @DisplayName("Carrito vacío tiene subtotal cero")
    void calcularSubtotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularSubtotal(), 0.001);
    }

    // ====== R3, R4, R5: cupones ======

    @Test
    @DisplayName("Acepta el cupón SENA10")
    void aplicarCupon_sena10_noLanzaExcepcion() {
        assertDoesNotThrow(() -> carrito.aplicarCupon("SENA10"));
    }

    @Test
    @DisplayName("Acepta el cupón BIENVENIDO")
    void aplicarCupon_bienvenido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> carrito.aplicarCupon("BIENVENIDO"));
    }

    @Test
    @DisplayName("Rechaza un cupón inválido")
    void aplicarCupon_codigoInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> carrito.aplicarCupon("DESCUENTO50"));
    }

    @Test
    @DisplayName("Sin cupón, el descuento es cero")
    void calcularDescuento_sinCupon_retornaCero() {
        carrito.agregarProducto("Cuaderno", 8_000, 1);
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("SENA10 descuenta el 10% del subtotal")
    void calcularDescuento_sena10_descuenta10PorCiento() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(50_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO descuenta 5000 si el subtotal es >= 50000")
    void calcularDescuento_bienvenidoSubtotalMayorIgual50000_descuenta5000() {
        carrito.agregarProducto("Mochila", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(5_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO no descuenta si el subtotal es < 50000")
    void calcularDescuento_bienvenidoSubtotalMenor50000_noDescuenta() {
        carrito.agregarProducto("Esfero", 49_999, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    // ====== R6: total (IVA + envío) ======

    @Test
    @DisplayName("Carrito vacío tiene total cero")
    void calcularTotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total paga envío si el valor con IVA es menor a 150000")
    void calcularTotal_valorBajo_pagaEnvio() {
        carrito.agregarProducto("Libro", 100_000, 1);
        // 100.000 * 1.19 = 119.000 (< 150.000) → + 12.000 envío = 131.000
        assertEquals(131_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con envío gratis si el valor con IVA es >= 150000")
    void calcularTotal_valorAlto_envioGratis() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        // (500.000 - 50.000) * 1.19 = 535.500 (>= 150.000) → envío gratis
        assertEquals(535_500, carrito.calcularTotal(), 0.001);
    }
}