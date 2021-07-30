package ec.edu.uce.emerscience.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RedondeoUtil {

	public static double redondearDecimales(double numero, int decimales) {
		BigDecimal redondeado = new BigDecimal(numero)
			.setScale(decimales, RoundingMode.HALF_UP);
		return redondeado.doubleValue();
	}
	
}
