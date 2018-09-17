package com.org.util.safe;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.org.util.enumeration.OperatorType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"operatorType", "valueHolderSet"})
public class GroupedFilter implements Serializable {

    private static final long serialVersionUID = 6085906157892025603L;

    protected OperatorType operatorType = OperatorType.AND;
    protected Set<ValueHolder> valueHolderSet;

    public Set<ValueHolder> getValueHolderSet() {
        if (valueHolderSet == null) {
            valueHolderSet = new HashSet<ValueHolder>(0);
        }
        return valueHolderSet;
    }

    public void addValueHolder(ValueHolder valueHolder) {
        getValueHolderSet().add(valueHolder);
    }

    public void clear() {
        getValueHolderSet().clear();
    }

}
