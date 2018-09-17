package com.org.util.web;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.math.NumberUtils;

@FacesConverter("bigDecimalConverter")
public class BigDecimalConverter implements Converter {

	private static final BigDecimal UPPER_LIMIT = new BigDecimal(999999999);
	private static final BigDecimal LOWER_LIMIT = new BigDecimal(0);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (!NumberUtils.isNumber(value)) {
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Solo se permiten ingresar montos"));
		}
		if (value.contains(".")) {
			String decimalPlace = value.substring(value.indexOf("."));
			if (decimalPlace.length() > 3) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Advertencia",
						"Demasiado decimales despues del punto"));
			}
		}
		BigDecimal convertedValue = new BigDecimal(value).setScale(2,
				RoundingMode.HALF_UP);
		if (convertedValue.compareTo(UPPER_LIMIT) > 0) {
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"El monto no puede ser mayor que " + UPPER_LIMIT));
		}
		if (convertedValue.compareTo(LOWER_LIMIT) < 0) {
			throw new ConverterException(new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"No pueden ingresar montos negativos"));
		}

		convertedValue = convertedValue.setScale(0, BigDecimal.ROUND_DOWN);

		return convertedValue;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
            Number nval = (Number) value;
		return (new BigDecimal(nval.doubleValue())).toString();
	}

}
