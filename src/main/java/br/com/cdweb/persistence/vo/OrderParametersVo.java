package br.com.cdweb.persistence.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cdweb.persistence.type.OrderType;

/**
 * 
 * @author willian
 *
 */
public class OrderParametersVo {

	private Map<String, OrderType> map;
	private List<Object[]> lista;
	
	/**
	 * @param order
	 * @param orderFields
	 */
	public OrderParametersVo(OrderType order, String...orderFields){
		map = new HashMap<String, OrderType>();
		lista = new ArrayList<Object[]>();
		for(String s:orderFields){
			map.put(s, order);
			lista.add(new Object[]{s, order});
		}
	}
	
	/**
	 * @param orderFields
	 */
	public OrderParametersVo(String...orderFields){
		map = new HashMap<String, OrderType>();
		lista = new ArrayList<Object[]>();
		for(String s:orderFields){
			map.put(s, OrderType.ASC);
			lista.add(new Object[]{s, OrderType.ASC});
		}
	}
        
	@Deprecated
	public OrderParametersVo(Map<String, OrderType> map){
		this.map=map;
	}

	public Map<String, OrderType> getMap() {
		return map;
	}
	
	public OrderParametersVo add(OrderType ordenacao,String atributo){
		lista.add(new Object[]{atributo, ordenacao});
		return this;
	}
	
	public OrderParametersVo add(String atributo){
		lista.add(new Object[]{atributo, OrderType.ASC});
		return this;
	}

	public List<Object[]> getLista() {
		return lista;
	}
	
}
