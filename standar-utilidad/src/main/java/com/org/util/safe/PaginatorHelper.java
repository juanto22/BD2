package com.org.util.safe;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class PaginatorHelper implements Serializable {

    private static final long serialVersionUID = -4593918516954779303L;

    public static final String GF = "globalFilter";
    public static final String UNDEFINED = "undefined";

    public PaginatorHelper() {
    }

    public static Pageable pageable(int pageNumber, int pageSize, List<SortMeta> multiSortMeta) {

        List<Order> orders = new ArrayList<Order>();
        int page = Math.max(pageNumber / pageSize, 0);

        if (multiSortMeta == null || multiSortMeta.isEmpty()) {
            return new PageRequest(page, pageSize);
        }

        for (SortMeta sortMeta : multiSortMeta) {
            String sortField = sortMeta.getSortField();
            if (sortField != null & sortMeta.getSortOrder() != null) {
                if (sortMeta.getSortOrder() == SortOrder.ASCENDING) {
                    orders.add(new Order(Direction.ASC, sortField));
                } else if (sortMeta.getSortOrder() == SortOrder.DESCENDING) {
                    orders.add(new Order(Direction.DESC, sortField));
                }
            }
        }

        if (orders.isEmpty()) {
            return new PageRequest(page, pageSize);
        }

        return new PageRequest(page, pageSize, new Sort(orders));
    }

    public static final Set<ValueHolder> transformer(List<String> attributes, Map<String, ?> stuff, Class entityClass) {

        Set<ValueHolder> conditions = new HashSet<ValueHolder>();

        if (entityClass == null) {
            return transformer(attributes, stuff);
        }

        Boolean hasGlobalFilter = stuff.containsKey(GF) && !((String) stuff.get(GF)).equals(UNDEFINED)
                && !((String) stuff.get(GF)).isEmpty();

        try {

            if (hasGlobalFilter) {
                String value = (String) stuff.get(GF);
                for (String attr : attributes) {
                    conditions.add(new ValueHolder(attr, value));
                }
            } else {
                if (!stuff.isEmpty() && stuff.containsKey(GF)) {
                    stuff.remove(GF);
                }
                for (Map.Entry<String, ?> entry : stuff.entrySet()) {
                    if (entry.getValue() instanceof String[]) {
                        String[] array = (String[]) entry.getValue();
                        conditions.add(new ValueHolder(entry.getKey(), ValueHolder._IN, StringUtils.join(array, ',')));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.String")) {
                        conditions.add(new ValueHolder(entry.getKey(), ValueHolder._ILIKE, (String) entry.getValue()));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.Boolean")) {
                        conditions.add(new ValueHolder(entry.getKey(), Boolean.valueOf((String) entry.getValue())));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.Float")) {
                        conditions.add(new ValueHolder(entry.getKey(), Float.valueOf((String) entry.getValue())));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.Double")) {
                        conditions.add(new ValueHolder(entry.getKey(), Double.valueOf((String) entry.getValue())));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.Integer")) {
                        conditions.add(new ValueHolder(entry.getKey(), Integer.valueOf((String) entry.getValue())));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.Long")) {
                        conditions.add(new ValueHolder(entry.getKey(), Long.valueOf((String) entry.getValue())));
                    } else if (entityClass.getDeclaredField(entry.getKey()).getType().toString()
                            .equals("class java.lang.Short")) {
                        conditions.add(new ValueHolder(entry.getKey(), Short.valueOf((String) entry.getValue())));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conditions;

    }

    public static final Set<ValueHolder> transformer(List<String> attributes, Map<String, ?> stuff) {
    	Set<ValueHolder> conditions = new HashSet<ValueHolder>();
    	
    	if(stuff != null){    	
	        Boolean hasGlobalFilter = stuff.containsKey(GF) && !((String) stuff.get(GF)).equals(UNDEFINED)
	                && !((String) stuff.get(GF)).isEmpty();
	        if (hasGlobalFilter) {
	            String value = (String) stuff.get(GF);
	            for (String attr : attributes) {
	                conditions.add(new ValueHolder(attr, value));
	            }
	        } else {
	            if (!stuff.isEmpty() && stuff.containsKey(GF)) {
	                stuff.remove(GF);
	            }
	            for (Map.Entry<String, ?> entry : stuff.entrySet()) {
	                if (entry.getValue() instanceof String[]) {
	                    String[] array = (String[]) entry.getValue();
	                    if(array.length > 0){
	                    	conditions.add(new ValueHolder(entry.getKey(), ValueHolder._IN, StringUtils.join(array, ',')));                    	
	                    }
	                } else if (entry.getValue() instanceof String) {
	                	//try {
							//Number num = NumberFormat.getInstance().parse(entry.getValue().toString());
							//conditions.add(new ValueHolder(entry.getKey(), num.toString()));
						//} catch (Exception e) {
							conditions.add(new ValueHolder(entry.getKey(), ValueHolder._ILIKE, (String) entry.getValue()));
						//}            
	                } else if (entry.getValue() instanceof Number) {
	                    conditions.add(new ValueHolder(entry.getKey(), ((Number) entry.getValue()).toString()));
	                }
	
	            }
	        }
    	}
        return conditions;
    }
}
