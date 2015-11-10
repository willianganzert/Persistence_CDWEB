package br.com.cdweb.persistence.vo;


/**
 * 
 * @author fausto
 */
public class FieldValuesVo {

    public static enum COMPARATOR_RULE 
    	{EQUAL, NOT_EQUAL, LIKE, BETWEEN, IS_NULL, IS_NOT_NULL, GREATER_THAN, LESS_THAN, GREATER_OR_EQUAL_THAN, LESS_OR_EQUAL_THAN};
    public static enum NEXT_PARAM_COMPARATOR_RULE {AND, OR};
    
    private String field;
    private Object[] values;
    
    protected COMPARATOR_RULE rule;
    
    protected NEXT_PARAM_COMPARATOR_RULE nextParamRule;
    

    protected FieldValuesVo(){}
    
    public <T> FieldValuesVo(String field, T... values){
    	nextParamRule = NEXT_PARAM_COMPARATOR_RULE.AND;
		this.field = field;
		this.values = values;
		if(values!=null &&values.length>0){
			if(values[0] instanceof String)
				rule = FieldValuesVo.COMPARATOR_RULE.LIKE;
			else
				rule = FieldValuesVo.COMPARATOR_RULE.EQUAL;
		}
    }
    
    public <T> FieldValuesVo(FieldValuesVo.COMPARATOR_RULE rule,String field, T... values){
    	nextParamRule = NEXT_PARAM_COMPARATOR_RULE.AND;
		this.rule = rule;
		this.field = field;
		this.values = values;
    }
    
    public <T> FieldValuesVo(NEXT_PARAM_COMPARATOR_RULE nextParamRule, String field, T... values){
    	
    	if(values!=null &&values.length>0){
			if(values[0] instanceof String)
				rule = FieldValuesVo.COMPARATOR_RULE.LIKE;
			else
				rule = FieldValuesVo.COMPARATOR_RULE.EQUAL;
		}
    	this.nextParamRule = nextParamRule;
		this.field = field;
		this.values = values;
    }
    
    public <T> FieldValuesVo(FieldValuesVo.COMPARATOR_RULE rule, NEXT_PARAM_COMPARATOR_RULE nextParamRule, String field,  T... values){
    	this.nextParamRule = nextParamRule;
		this.rule = rule;
		this.field = field;
		this.values = values;
    }

    public String getField() {
    	return field;
    }

    public Object[] getValues() {
    	return values;
    }

    protected void setField(String field) {
    	this.field = field;
    }

    protected void setValues(Object... values) {
    	this.values = values;
    }

	public COMPARATOR_RULE getRule() {
		return rule;
	}

	public NEXT_PARAM_COMPARATOR_RULE getNextParamRule() {
		return nextParamRule;
	}

}
