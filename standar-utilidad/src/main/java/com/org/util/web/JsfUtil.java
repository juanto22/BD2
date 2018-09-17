/*
 * Copyright 2014 - 2015
 * Ministerio de Hacienda
 *
 * Este programa es de uso exclusivo para el Ministerio de Hacienda,
 * y está protegido por las leyes de derechos de autor de la
 * República de El Salvador, se prohibe la copia y distribución
 * total o parcial del mismo.
 */
package com.org.util.web;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public class JsfUtil {

    private static String DEFAULT_DETAIL_SUFFIX = "_detail";

    /**
     * Constante que especifica la dirección de los archivos de
     * Internacionalización.
     */
    public static final String BUNDLE_PATH = "/i18n/safiCommonBundle";

    // public static void addErrorMessage(Exception ex, String defaultMsg) {
    // String msg = ex.getLocalizedMessage();
    // if (msg != null && msg.length() > 0) {
    // addErrorMessage(msg);
    // } else {
    // addErrorMessage(defaultMsg);
    // }
    // }

    // public static void addErrorMessages(List<String> messages) {
    // for (String message : messages) {
    // addErrorMessage(message);
    // }
    // }

    /**
     * Método que muestra un Mensaje de Exito a la Pantalla.
     *
     * @param msg
     *            Mensaje de Error a Mostrar.
     * @param params
     *            the params
     */
    public static void addSuccessMessage(String msg, final Object... params) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Método que muestra un Mensaje de Error a la Pantalla.
     *
     * @param msg
     *            Mensaje de Error a Mostrar
     */
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed(); // Invalidate JSF
                                                                // page if we
                                                                // raise an
                                                                // error message
    }

    /**
     * Método que Internacionaliza el código del Mensaje de Exito especificado
     *
     * @param messsageCode
     *            Código del Mensaje de Error.
     */
    public static void addInternationalizedSuccessMessage(String messsageCode,
            final Object... params) {
        FacesMessage facesMsg = getMessage(messsageCode,
                FacesMessage.SEVERITY_INFO, params);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Método que Internacionaliza el código del Mensaje de Error especificado
     *
     * @param messsageCode
     *            Código del Mensaje de Error.
     */
    public static void addInternationalizedErrorMessage(
            final String messsageCode, final Object... params) {
        FacesMessage facesMsg = getMessage(messsageCode,
                FacesMessage.SEVERITY_ERROR, params);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed(); // Invalidate JSF
                                                                // page if we
                                                                // raise an
                                                                // error message
    }

    /**
     * Retorna el valor del Campo message.
     *
     * @param messsageCode the messsage code
     * @param severity the severity
     * @param params the params
     * @return El valor del Campo message
     */
    public static FacesMessage getMessage(final String messsageCode,
            final FacesMessage.Severity severity, final Object... params) {
        final FacesMessage facesMessage = getMessage(getLocale(), messsageCode,
                params);
        facesMessage.setSeverity(severity);
        return facesMessage;
    }

    /**
     * Retorna el valor del Campo message.
     *
     * @param locale the locale
     * @param messsageCode the messsage code
     * @param params the params
     * @return El valor del Campo message
     */
    public static FacesMessage getMessage(final Locale locale,
            final String messsageCode, final Object... params) {
        String summary = null;
        String detail = null;
        final ResourceBundle bundle = ResourceBundle
                .getBundle(JsfUtil.BUNDLE_PATH,locale);

        try {
            summary = getFormattedText(locale, bundle.getString(messsageCode),
                    params);
        } catch (final MissingResourceException e) {
            summary = messsageCode;
        }

        try {
            detail = getFormattedText(locale,
                    bundle.getString(messsageCode + DEFAULT_DETAIL_SUFFIX),
                    params);
        } catch (final MissingResourceException e) {
            detail = summary;
        }

        return new FacesMessage(summary, detail);
    }

    private static String getFormattedText(final Locale locale,
            final String message, final Object params[]) {
        MessageFormat messageFormat = null;

        if (params == null || message == null) {
            return message;
        }

        messageFormat = locale == null ? new MessageFormat(message)
                : new MessageFormat(message, locale);
        return messageFormat.format(params);
    }

    // public static Throwable getRootCause(Throwable cause) {
    // if (cause != null) {
    // Throwable source = cause.getCause();
    // if (source != null) {
    // return getRootCause(source);
    // } else {
    // return cause;
    // }
    // }
    // return null;
    // }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    // public static boolean isDummySelectItem(UIComponent component, String
    // value) {
    // for (UIComponent children : component.getChildren()) {
    // if (children instanceof UISelectItem) {
    // UISelectItem item = (UISelectItem) children;
    // if (item.getItemValue() == null
    // && item.getItemLabel().equals(value)) {
    // return true;
    // }
    // break;
    // }
    // }
    // return false;
    // }

    public static String getComponentMessages(String clientComponent,
            String defaultMessage) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(fc)
                .findComponent(clientComponent);
        if (component instanceof UIInput) {
            UIInput inputComponent = (UIInput) component;
            if (inputComponent.isValid()) {
                return defaultMessage;
            } else {
                Iterator<FacesMessage> iter = fc.getMessages(inputComponent
                        .getClientId());
                if (iter.hasNext()) {
                    return iter.next().getDetail();
                }
            }
        }
        return "";
    }

    public static Locale getLocale() {
        Locale locale = null;
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.getViewRoot() != null) {
            locale = facesContext.getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();
        }

        return locale;
    }

}
