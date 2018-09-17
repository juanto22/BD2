 package com.org.util.enumeration;

import java.util.EnumSet;
import java.util.Set;

public enum OperationType {

    GT("gt", "Mayor que"),
    GE("goe", "Mayor o igual que"),
    LT("lt", "Menor que"),
    LE("loe", "Menor o igual que"),
    BETWEEN("between", "Entre"),
    IN("in", "Dentro"),
    NOT_IN("notIn", "No dentro"),
    EQ("eq", "Igual"),
    CT("contains", "Contiene"),
    SW("startsWith", "Inicia Con"),
    EW("endsWith", "Finaliza Con"),
    SI("isTrue", "SI"),
    NO("isFalse", "NO"),
    NULL("isNull","Nulo"),
    NOT_NULL("isNotNull","NO Nulo"),
    ;
    
    String code;
    String description;
    private static Set typesForString = EnumSet.range(EQ, EW);
    private static Set typesForNumber = EnumSet.range(GT, EQ);
    private static Set typesForBoolean = EnumSet.range(SI, NO);

    private OperationType(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OperationType getOperationType(final String code) {
        OperationType ret = null;
        for (OperationType ienum : values()) {
            if (ienum.getCode().equals(code)) {
                ret = ienum;
                break;
            }
        }
        return ret;
    }

    public static Set getOptionsForType(String type) {
        Set typesToReturn;
        switch (type.trim().toLowerCase()) {
		case "integer":
                case "double":
                case "float":
                case "long":
                case "short":
                case "calendar":
                case "date":
			typesToReturn= typesForNumber;
                        break;
 
		case "string":
			typesToReturn= typesForString;
                        break;
 
		case "boolean":
                        typesToReturn= typesForBoolean;
                        break;
 
		default:
			typesToReturn= typesForNumber;
                        break;
		}
        return typesToReturn;
    }
}
