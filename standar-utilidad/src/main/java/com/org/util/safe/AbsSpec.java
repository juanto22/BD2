package com.org.util.safe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.mvel2.MVEL;
import org.springframework.util.NumberUtils;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.TemporalExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.org.util.enumeration.OperationType;
import com.org.util.enumeration.OperatorType;
import com.org.util.service.BaseService;

import lombok.extern.java.Log;

@Log
public class AbsSpec {

	protected static Predicate toPredicate(String expression, Map<String, Object> vars) {
		return (Predicate) MVEL.eval(expression, vars);
	}

	protected static Predicate[] toPredicate(Collection<Predicate> collection) {
		if (collection.isEmpty()) {
			return null;
		}
		return (Predicate[]) collection.toArray(new Predicate[collection.size()]);
	}

	public static BooleanBuilder builder(Map<String, Object> specs, boolean any, boolean custom,
			Set<ValueHolder> conditions, Set<GroupedFilter> groupedFilters, String... raws) {
		BooleanBuilder bb = new BooleanBuilder();
		Collection<Predicate> predicates = new ArrayList<Predicate>();
		String condition = null;
		Map vars = new HashMap<String, Object>();
		vars.putAll(specs);
		if (groupedFilters != null && !groupedFilters.isEmpty()) {
			for (GroupedFilter gf : groupedFilters) {
				if (gf.getValueHolderSet() != null && !gf.getValueHolderSet().isEmpty()) {
					List<Predicate> predicateSet = new ArrayList<Predicate>();
					for (final ValueHolder item : gf.getValueHolderSet()) {
						condition = toCondition(item, vars);
						Predicate pred = toPredicate(condition, vars);
						predicateSet.add(pred);
					}
					if (!predicateSet.isEmpty()) {
						if (gf.getOperatorType().equals(OperatorType.AND)) {
							bb.andAnyOf(toPredicate(predicateSet));
						} else if (gf.getOperatorType().equals(OperatorType.OR)) {
							bb.orAllOf(toPredicate(predicateSet));
						}
					}
				}
			}
		}
		for (final ValueHolder item : conditions) {
			condition = toCondition(item, vars);
			Predicate pred = toPredicate(condition, vars);
			predicates.add(pred);
			if (custom) {
				if (item.getOperatorType() != null) {
					if (item.getOperatorType().equals(OperatorType.AND)) {
						bb.and(pred);
					} else if (item.getOperatorType().equals(OperatorType.OR)) {
						bb.or(pred);
					}
				} else {
					bb.and(pred);
				}
			}

		}

		// Fijos
		for (String raw : raws) {

		}

		for (String expression : raws) {

			Predicate predicatedFixed = toPredicate(expression, specs);
			if (predicatedFixed != null) {
				bb.and(predicatedFixed);
			}
		}
		if (!predicates.isEmpty()) {
			if (!custom) {
				if (any) {
					bb.andAnyOf(toPredicate(predicates));
				} else {
					BooleanBuilder or = new BooleanBuilder();
					or.orAllOf(toPredicate(predicates));
					bb.and(or);
				}
			}
		}

		return bb;
	}

	public static String toCondition(ValueHolder item, Map vars) {
		String condition = null;
		String attr = (item.getField() != null ? !item.getField().trim().equals("") ? item.getField() + "." : ""
				: BaseService.ENTITY_VAR + ".") + item.getName();
		String oper = item.getOperator();
		Object attrib = MVEL.eval(attr, vars);
		if (item.isMultiValor()) {
			// TODO: code difference?
		}
		if (attrib instanceof TemporalExpression) {
			Class attrtype = ((TemporalExpression) attrib).getType();
			if (attrtype.equals(java.util.Date.class)) {
				if (item.getFecha1() != null) {
					vars.put("ovalue", item.getFecha1());
					if (oper.trim().equals(OperationType.BETWEEN.getCode())) {
						vars.put("ovalue2", item.getFecha2());
						condition = attr + "." + oper + "(ovalue,ovalue2)";
					} else {
						condition = attr + "." + oper + "(ovalue)";
					}
				} else if (item.getValue() != null) {
					try {
						Date odate = DateUtils.parseDate(item.getValue().toString(), new String[] { "dd/MM/yyyy" });
						vars.put("ovalue", odate);
					} catch (Exception ex) {
						vars.put("ovalue", new Date());
					}
					vars.put("resultingClass", Date.class);
					condition = "com.mysema.query.types.template.DateTemplate.create(resultingClass, \"trunc({0})\", "
							+ attr + ").eq(ovalue)";
				}

			} else if (attrtype.equals(java.util.Calendar.class)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(item.getFecha1());
				vars.put("ovalue", cal);
				if (oper.trim().equals(OperationType.BETWEEN.getCode())) {
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(item.getFecha2());
					vars.put("ovalue2", cal2);
					condition = attr + "." + oper + "(ovalue,ovalue2)";
				} else {
					condition = attr + "." + oper + "(ovalue)";
				}
			}
		} else if (oper.trim().equals(OperationType.NULL.getCode())
				|| oper.trim().equals(OperationType.NOT_NULL.getCode())) {
			condition = attr + "." + oper + "()";
		} else if (oper.trim().equals(OperationType.IN.getCode())) {
			Object[] arrayValue = null;
			if (item.getValue() instanceof Object[]) {
				arrayValue = (Object[]) item.getValue();
			} else if (Collection.class.isAssignableFrom(item.getValue().getClass())) {
				Collection coll = (Collection) item.getValue();
				if (attrib instanceof NumberPath) {
					arrayValue = new Number[coll.size()];
				} else {
					arrayValue = new Object[coll.size()];
				}
				arrayValue = coll.toArray(arrayValue);
			} else if (item.getValue() instanceof String && ((String) item.getValue()).contains(",")) {
				if (attrib instanceof NumberPath) {
					String[] sarrayValue = ((String) item.getValue()).split("\\,");
					if (sarrayValue != null && sarrayValue.length > 0) {
						arrayValue = new Number[sarrayValue.length];
						for (int i = 0; i < sarrayValue.length; i++) {
							arrayValue[i] = NumberUtils.parseNumber(sarrayValue[i], Number.class);
						}
					}
				} else {
					arrayValue = ((String) item.getValue()).split("\\,");
				}
			} else if (item.getValue() instanceof String) {
				if (attrib instanceof NumberPath) {
					String svalue = item.getValue().toString();
					if (svalue != null && !svalue.trim().equals("")) {
						arrayValue = new Number[] { NumberUtils.parseNumber(item.getValue().toString(), Number.class) };
					} else {
						arrayValue = new Number[] { -666 };
					}
				} else {
					arrayValue = new String[] { item.getValue().toString() };
				}
			} else {
				arrayValue = new Object[] { item.getValue() };
			}
			vars.put("arrayValue", arrayValue);
			condition = attr + "." + oper + "(arrayValue)";
		} else if (oper.trim().equals(OperationType.NOT_IN.getCode())) {
			if (item.getValue() instanceof Collection) {
				vars.put("arrayValue", (item.getValue()));
				condition = attr + "." + oper + "(arrayValue)";
			}
		} else if (oper.trim().equals(OperationType.BETWEEN.getCode())) {
			condition = attr + "." + oper + "(" + item.getValue() + "," + item.getValue2() + ")";
		} else if (oper.trim().equals(ValueHolder._ILIKE)) {
			condition = attr + ".upper." + oper + "(\"%" + item.getValue().toString().toUpperCase() + "%\")";
		} else if (attrib instanceof StringPath) {
			condition = attr + "." + oper + "(\"" + item.getValue() + "\")";
		} else {
			condition = attr + "." + oper + "(" + item.getValue() + ")";
		}
		return condition;
	}

}
