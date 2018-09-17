package com.org.util.safe;

import java.io.Serializable;
import java.util.Date;

import com.org.util.enumeration.OperationType;
import com.org.util.enumeration.OperatorType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = {"operatorType", "name", "operator", "value", "value2", "fecha1", "fecha2"})
public class ValueHolder implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6622720316247931219L;
    public final static String _EQ = "eq";
    public final static String _ILIKE = "like";
    public final static String _IN = "in";
    public final static String _BETWEEN = "between";

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String label;

    @Getter
    protected String operator; // enum

    @Setter
    @Getter
    private String operatorDescription; // enum

    @Getter
    @Setter
    //private List<Object> value = new ArrayList<Object>();
    private Object value;

    @Getter
    @Setter
    private Object value2;

    @Getter
    @Setter
    private Date fecha1;

    @Getter
    @Setter
    private Date fecha2;

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private OperatorType operatorType;
    
    @Getter
    @Setter
    private String field;

    public ValueHolder() {
    }

    public ValueHolder(String attr, Object value) {
        this.name = attr;
        this.operator = _EQ;
        this.value = value;
        //this.value.add(value);
    }

    public ValueHolder(String attr, String oper, Object value) {
        this.name = attr;
        this.operator = oper;
        //this.value.addAll(Arrays.asList(value));
        this.value = value;
    }
    public ValueHolder(String attr, String oper, Object value,OperatorType operType) {
        this.name = attr;
        this.operator = oper;
        //this.value.addAll(Arrays.asList(value));
        this.value = value;
        this.operatorType=operType;
    }

    public ValueHolder(Integer id, String attr, String oper, Object value, Object value2, String label, Date fecha1, Date fecha2) {
        this.id = id;
        this.name = attr;
        this.operator = oper;
        //this.value.addAll(Arrays.asList(value));
        this.value = value;
        this.value2 = value2;
        this.label = label;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
    }

    public boolean isMultiValor() {
        return _IN.equals(operator) || _BETWEEN.equals(operator);
    }

    @Override
    public String toString() {
        return "Holder: " + name + " " + operator + " " + value.toString();
    }

    public void setOperator(String operator) {
        if (operator != null) {
            setOperatorDescription(OperationType.getOperationType(operator).getDescription());
        }
        this.operator = operator;
    }

}
