package br.com.cdweb.persistence.vo;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.TemporalType;

/**
 * 
 * @author willian
 */
public class DateFieldValuesVo extends FieldValuesVo{

    private final TemporalType temporalType = TemporalType.DATE;


    public DateFieldValuesVo(String field, Date date){
		super();
		this.setField(field);
		this.setValues(date);
		rule=DateFieldValuesVo.COMPARATOR_RULE.EQUAL;
    }
    
    public DateFieldValuesVo(String field, Date date1, Date date2){
		super();
		this.setField(field);
		this.setValues(date1,date2);
		rule=DateFieldValuesVo.COMPARATOR_RULE.BETWEEN;
    }

    public DateFieldValuesVo(String field, Calendar calendar){
		super();
		this.setField(field);
		this.setValues(calendar);
		rule=DateFieldValuesVo.COMPARATOR_RULE.EQUAL;
    }

    public DateFieldValuesVo(String field, Calendar calendar1, Calendar calendar2){
		super();
		this.setField(field);
		this.setValues(calendar1,calendar2);
		rule=DateFieldValuesVo.COMPARATOR_RULE.BETWEEN;
    }

    public TemporalType getTemporalType() {
    	return temporalType;
    }
}
