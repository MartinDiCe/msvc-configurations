package com.diceprojects.msvcconfigurations.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Helper para conversión de montos entre distintas monedas usando una moneda base.
 * <p>
 * La conversión se realiza mediante la fórmula:
 * <br>
 * Monto en moneda destino = monto en moneda origen * (conversionRateDestino / conversionRateOrigen)
 * <br>
 * Se utiliza BigDecimal para asegurar precisión en los cálculos financieros.
 * </p>
 */
public class CurrencyConversionHelper {

    /**
     * Convierte un monto desde una moneda origen a una moneda destino utilizando la moneda base.
     *
     * @param amount                  el monto en la moneda origen.
     * @param conversionRateOrigin    la tasa de conversión de la moneda origen (relacionada con la moneda base).
     * @param conversionRateTarget    la tasa de conversión de la moneda destino (relacionada con la moneda base).
     * @return el monto convertido a la moneda destino.
     * @throws IllegalArgumentException si conversionRateOrigin es 0.
     */
    public static BigDecimal convert(BigDecimal amount, BigDecimal conversionRateOrigin, BigDecimal conversionRateTarget) {
        if (conversionRateOrigin.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El conversionRateOrigin no puede ser 0");
        }
        // Calcula el factor de conversión y multiplica el monto.
        BigDecimal factor = conversionRateTarget.divide(conversionRateOrigin, 10, RoundingMode.HALF_UP);
        return amount.multiply(factor);
    }
}
