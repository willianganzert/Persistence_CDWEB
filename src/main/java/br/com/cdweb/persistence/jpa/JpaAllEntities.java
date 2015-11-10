package br.com.cdweb.persistence.jpa;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.message.internal.StringBuilderUtils;

import br.com.cdweb.persistence.entities.ComunEntidades;
import br.com.cdweb.persistence.helper.EntityManagerHelper;
import br.com.cdweb.persistence.jpa.exceptions.JpaException;
import br.com.cdweb.persistence.type.OrderType;
import br.com.cdweb.persistence.util.UtlEntity;
import br.com.cdweb.persistence.vo.FieldValuesVo;
import br.com.cdweb.persistence.vo.OrderParametersVo;
import br.com.cdweb.persistence.vo.ResultFilterVo;

/**
 * 
 * @author willian
 */
public class JpaAllEntities {

    public static <T extends ComunEntidades> T merge(T entity){
		EntityManager em = EntityManagerHelper.getEntityManager();
		T t = em.merge(entity);
		em.close();
		return t;
	}
    
    public static void  detach(ComunEntidades entity){
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.detach(entity);
		em.close();
	}
    
    /**
     * Método que executa um refresh na entidade enviada, útil para casos onde
     * foi atualizado valores de entidades associadas e deseja-se atualizar o estado
     * da entidade principal. Prefira chamar este método ao utilizar diretamente o EntityManager
     * para que eventuais erros sejam apropriadamente logados na camada jpa.
     * 
     * @param <T>
     * @param entity
     * @return entity atualizada
     */
    public static <T extends ComunEntidades> T refresh(T entity){
    	EntityManager em=null;
    	try{
	    	em = EntityManagerHelper.getEntityManager();
	    	entity = em.merge(entity);
	    	em.refresh(entity);
	    	em.detach(entity);
    	}catch (Exception e) {
    		String msg="Exceção em refresh, parâmetros: entity="
			    +entity==null?null:entity.getClass().getName();
		          System.out.println(msg + " - Exception: " + e);
		    throw new JpaException(msg, e);
		}finally{
			if(em!=null && em.isOpen())
 		    	em.close();
		}
    	return entity;
    }
    
    /*public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(int firstResult, int maxResult, Class<T> entity, FieldValuesVo... fieldValuesVos){
    	OrderParametersVo orderParametersVo = new OrderParametersVo(UtlEntity.getIdFieldName(entity));
    	return JpaAllEntities.doFilter(firstResult, maxResult, entity, orderParametersVo, false, fieldValuesVos);
    }
    
    public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(int firstResult, int maxResult, Class<T> entity,boolean doCount,FieldValuesVo... fieldValuesVos){
    	OrderParametersVo orderParametersVo = new OrderParametersVo(UtlEntity.getIdFieldName(entity));
    	return JpaAllEntities.doFilter(firstResult, maxResult, entity, orderParametersVo, doCount, fieldValuesVos);
    }*/
    
    public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(Class<T> entity, FieldValuesVo... fieldValuesVos){
    	OrderParametersVo orderParametersVo = new OrderParametersVo(UtlEntity.getIdFieldName(entity));
    	return JpaAllEntities.doFilter(0, Integer.MAX_VALUE, entity, orderParametersVo, false, fieldValuesVos);
    }
    
    public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(Integer firstResult, Integer maxResult, Class<T> entity, FieldValuesVo... fieldValuesVos){
    	OrderParametersVo orderParametersVo = new OrderParametersVo(UtlEntity.getIdFieldName(entity));
    	return JpaAllEntities.doFilter(firstResult, maxResult, entity, orderParametersVo, false, fieldValuesVos);
    }
    
    public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(Integer firstResult, Integer maxResult, Class<T> entity,boolean doCount,FieldValuesVo... fieldValuesVos){
    	OrderParametersVo orderParametersVo = new OrderParametersVo(UtlEntity.getIdFieldName(entity));
    	return JpaAllEntities.doFilter(firstResult, maxResult, entity, orderParametersVo, doCount, fieldValuesVos);
    }
    
    public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(Integer firstResult, Integer maxResult, 
    		Class<T> entity, OrderParametersVo orderParametersVo, boolean doCount, FieldValuesVo... fieldValuesVos){
    	
    	Query q = null;
    	StringBuilder sb = new StringBuilder();
    	StringBuilder[] sbOrderArray = null;
    	String entityName;
    	List<T> resultado = null;
    	Long count=null;
    	ResultFilterVo<T> resultFilterVo=null;
    	IntWrapper countParam= new IntWrapper();
    	EntityManager em=null;
    	try {
    		entityName =entity.getSimpleName();
    		
    		sbOrderArray = processOrderParametersVo(entity,orderParametersVo);
    		
    		sb.append("select e from ").append(entityName).append(" e ").append(sbOrderArray[0]);
    		//se fieldValuesVos==null significa um "ListAll" e portando não adiciona o where
    		if(fieldValuesVos!=null){
    			sb.append(" where (");
    			//itera pelos fieldValuesVos
    			for(int i=0;i<fieldValuesVos.length;i++){
    				
    				switch (fieldValuesVos[i].getRule()) {
					case EQUAL:
						sb.append(createEqual(fieldValuesVos[i],countParam));
						break;
					case LIKE:
						sb.append(createLike(fieldValuesVos[i],countParam));
						break;
					case NOT_EQUAL:
						sb.append(createNotEqual(fieldValuesVos[i],countParam));
						break;
					case BETWEEN:
						sb.append(createBetween(fieldValuesVos[i],countParam));
						break;
					case IS_NULL:
						sb.append(createIsNull(fieldValuesVos[i]));
						break;
					case IS_NOT_NULL:
						sb.append(createIsNotNull(fieldValuesVos[i]));
						break;
					case GREATER_THAN:
						sb.append(createGreaterThan(fieldValuesVos[i],countParam));
						break;
					case GREATER_OR_EQUAL_THAN:
						sb.append(createGreaterOrEqualThan(fieldValuesVos[i],countParam));
						break;
					case LESS_THAN:
						sb.append(createLessThan(fieldValuesVos[i],countParam));
						break;
					case LESS_OR_EQUAL_THAN:
						sb.append(createLessOrEqualThan(fieldValuesVos[i],countParam));
						break;
					default:
						break;
					}
    				
    				//se não for o último parâmetro adiciona um "and" ou "or"
    				if(i != (fieldValuesVos.length-1)){
    					switch (fieldValuesVos[i].getNextParamRule()) {
    					case AND:
    						sb.append(" and ");
    						break;
    					case OR:
    						sb.append(" )or( ");//formata desta forma:  (rule1 and rule2 and rule3) or (rule4 and rule5) or (rule6)
    						break;
    					default:
    						break;
    					}
    				}
    			}
    			sb.append(" )");
    		}
    		sb.append(" order by ");
    		//sb.append(createOrderBy(orderParametersVo));
    		sb.append(sbOrderArray[1]);
    		
    		System.out.println("Warning - Query criada antes de setar os valores: " + sb.toString());
    		
    		em = EntityManagerHelper.getEntityManager();
    		q = em.createQuery(sb.toString(),entity);
    		setParams(q, fieldValuesVos);
    		
    		if(firstResult !=null)
		    	q.setFirstResult(firstResult);
		    if(maxResult !=null)
		    	q.setMaxResults(maxResult);

		    resultado = q.getResultList();
	    
            for (T t : resultado)
                em.detach(t);
    
            //usa a mesma query para realizar um count;
            if(doCount){
	            sb.replace(7, 8, "count(e)");
	            q = em.createQuery(sb.toString());
	            setParams(q, fieldValuesVos);
	            count = (Long)q.getSingleResult();
            }
            
            resultFilterVo = new ResultFilterVo<T>(resultado, count);
    		
		} catch (Exception e) {
			String msg = "Exceção em doFilter, entity:"+entity==null?null:entity.getName()+" ,";
		    if(fieldValuesVos!=null)
			    for(int i=0;i<fieldValuesVos.length;i++){
			    	if(fieldValuesVos[i]!=null){
						msg+= "\nfield: "+fieldValuesVos[i].getField()+", ";
						for(int x=0;x<fieldValuesVos[i].getValues().length;x++)
						    msg+= "\n\tvalues: "+fieldValuesVos[i].getValues()[x]+", ";
			    	}
			    }
		    System.out.println(msg + " - Exception: " + e);
		    throw new JpaException(msg, e);
		}finally{
		    if(em!=null && em.isOpen())
		    	em.close();
		}
				
    	return resultFilterVo;
    }
    
    /**
     * No jpa dado duas entidades A e B. Tendo uma associação manyToOne de A para B. quando se deseja listar A ordenado por um campo de B
     * 
     * @param <T>
     * @param entity
     * @param orderParametersVo
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    private  static <T extends ComunEntidades> StringBuilder[] processOrderParametersVo(Class<T> entity, OrderParametersVo orderParametersVo) throws SecurityException, NoSuchFieldException{
    	
    	StringBuilder sbFrom = new StringBuilder();
    	StringBuilder sbOrder = new StringBuilder();
    	int internalCount=0;
    	List<Object[]> o=orderParametersVo.getLista();
    	boolean isFirst=true;
    	
		for(Object[] ob:o){
			String s =(String) ob[0];
			String s2=null;
			String s3=null;
			//o tratamento diferenciado ocorre apenas em uma associação direta, ex.: a.b.field
			//se for a.b.c.field ou mais o tratamento não ocorre
			if(s.contains(".") && s.indexOf(".") == s.lastIndexOf(".")){
				s2=s.substring(0,s.indexOf("."));
				s3=s.substring(s.indexOf("."));
			
				Field f = entity.getDeclaredField(s2);
				Class<?> c = f.getType();
				sbFrom.append(" left join e.").append(s2).append(" as c").append(internalCount).append(" ");
				if(!isFirst)
					sbOrder.append(", ");
				else
					isFirst=false;
				sbOrder.append("c").append(internalCount++).append(s3).append(" ").append(((OrderType)ob[1]).getOrder());
							
			}else{
				if(!isFirst)
					sbOrder.append(", ");
				else
					isFirst=false;
				sbOrder.append("e.").append(ob[0]).append(" ").append(((OrderType)ob[1]).getOrder());
			}
		}
		return new StringBuilder[]{sbFrom,sbOrder};
    }
    
    private static void setParams(Query q, FieldValuesVo... fieldValuesVos){
    	Integer internalCount=0;
    	boolean ehString=false;
    	//se for nulo é um listAll
    	if(fieldValuesVos!=null){
	    	for (FieldValuesVo vo : fieldValuesVos) {
				for (Object ob : vo.getValues()) {
					
					if(ob instanceof String )
						ehString=true;
					else 
						ehString=false;
					
					switch (vo.getRule()) {
					case EQUAL:
						if(ehString)
							q.setParameter(("param"+internalCount++), ((String)ob).toUpperCase() );
						else
							q.setParameter(("param"+internalCount++), ob);
						break;
					case LIKE:
						if(ehString)
							q.setParameter(("param"+internalCount++), "%"+((String)ob).toUpperCase()+"%");
						else
							q.setParameter(("param"+internalCount++), ob);
						break;
					case NOT_EQUAL:
						if(ehString)
							q.setParameter(("param"+internalCount++), ((String)ob).toUpperCase() );
						else
							q.setParameter(("param"+internalCount++), ob);
						break;
					case BETWEEN:
						q.setParameter(("param"+internalCount++), ob);
						break;
					default:
						q.setParameter(("param"+internalCount++), ob);
						break;
					}
				}
			}
    	}
    }
    
    private static String createBetween(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s;
    	if(fieldValuesVo.getValues().length!=2)
    		throw new JpaException("Exceção em createBetween, parâmetro fieldValuesVo inválido, values deve ter tamanho=2");
    	
    	s = " e."+fieldValuesVo.getField()+" BETWEEN "+(":param"+countParam.inc())+" and "+(":param"+countParam.inc());
    	return s;
    }
    
    private static String createEqual(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		Object param = fieldValuesVo.getValues()[i];
    		
    		//caso seja String usa upper()
    		if(param instanceof String)
    			s += " UPPER(e."+fieldValuesVo.getField()+") = "+(":param"+countParam.inc());
    			
    		else
    			s += " e."+fieldValuesVo.getField()+" = "+(":param"+countParam.inc());	
    		    		
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or";
    	}
    	s+=" )";
    	return s;
    }
    
    private static String createLike(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	if(!(fieldValuesVo.getValues()[0] instanceof String))
    		throw new JpaException("Exceção em createLike, parâmetro values de fieldValuesVo inválido, não contém Strings. fieldValuesVo="
    				+fieldValuesVo.getValues()[0].getClass().getSimpleName());
    	
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		Object param = fieldValuesVo.getValues()[i];
    		
    		s += " UPPER(e."+fieldValuesVo.getField()+") like "+(":param"+countParam.inc());
    		
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or";
    	}
    	s+=" )";
    	return s;
    }
    
    private static String createNotEqual(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		Object param = fieldValuesVo.getValues()[i];
    		
    		//caso seja String usa upper()
    		if(param instanceof String)
    			s += " UPPER(e."+fieldValuesVo.getField()+") <> "+(":param"+countParam.inc());
    			
    		else
    			s += " e."+fieldValuesVo.getField()+" <> "+(":param"+countParam.inc());	
    		    		
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or";
    	}
    	s+=" )";
    	return s;
    }
    
    private static String createIsNull(FieldValuesVo fieldValuesVo){
    	String s="";
    	
    	s = "e."+fieldValuesVo.getField()+" is null ";	
    	return s;
    }
    
    private static String createIsNotNull(FieldValuesVo fieldValuesVo){
    	String s="";
    	
    	s = " e."+fieldValuesVo.getField()+" is not null ";
    	return s;
    }
    
    private static String createGreaterThan(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		s += " e."+fieldValuesVo.getField()+" > "+(":param"+countParam.inc());	    		
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or ";
    	}
    	s+=" )";
    	return s;
    }
    
    private static String createGreaterOrEqualThan(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		s += " e."+fieldValuesVo.getField()+" >= "+(":param"+countParam.inc());	    		
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or ";
    	}
    	s+=" )";
    	return s;
    }
    
    private static String createLessThan(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		s += " e."+fieldValuesVo.getField()+" < "+(":param"+countParam.inc());
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or ";
    	}
    	s+=" )";
    	return s;
    }
    
    private static String createLessOrEqualThan(FieldValuesVo fieldValuesVo, IntWrapper countParam){
    	String s="( ";
    	
    	for(int i=0;i<fieldValuesVo.getValues().length;i++){
    		s += " e."+fieldValuesVo.getField()+" <= "+(":param"+countParam.inc());	   		    		
    		//se não for o último parâmetro adiciona um "or"
    		if(i != (fieldValuesVo.getValues().length-1))
    			s+=" or ";
    	}
    	s+=" )";
    	return s;
    }
    
    @Deprecated
    private static String createOrderBy(OrderParametersVo orderParametersVo){
    	String s="";
    	for(Object[] objs:orderParametersVo.getLista())
    		s+="e."+objs[0]+" "+((OrderType)objs[1]).getOrder()+", ";
    	    	
	    s=s.substring(0, s.length() - 2);
    	
    	return s;
    }
    
    private static class IntWrapper{
		private int value;

		public IntWrapper(int initval) {
			value = initval;
		}

		public IntWrapper() {
			value = 0;
		}

		public synchronized int getVal() {
			return value;
		}

		public synchronized int setVal(int newval) {
			value = newval;
			return value;
		}

		public int inc(int inc) {
			return setVal(value + inc);
		}

		public int inc() {
			int i = value;
			inc(1);
			return i;
		}

		public int dec(int dec) {
			return inc(-dec);
		}

		public int dec() {
			int i = value;
			dec(1);
			return i;
		}
    }
    
    /**
     * Regras:
     * Os parâmetros devem pertencer a entidade pesquisada, ex.:
     * entidade.id, entidade.nome, entidade.entidade2
     * NÃO PODE SER
     * entidade.entidade2.entidad3,  entidade.entidade2.nome
     *
     * A ordenação do resultado é sempre pela PK da entidade pesquisada
     *
     * Nos parâmetros:
     * Se for String usa UPPER() e LIKE %param%
     * Se for número ou entidade usa =param
     * Se for uma data usa =param, se forem duas datas usa between
     * Se for notEqual usa upper para String, mas nao usa %
     * Para listar todos mandar o FieldValuesVo nulo
     * Para validar se nulo (isnull) adicionar um value do FieldValuesVo nulo
     *
     *
     * @param <T>
     * @param firstResult
     * @param maxResult
     * @param entity
     * @param fieldValuesVos
     * @return ResultFilterVo que contem a lista de resultado e o total de registros
     */
    @SuppressWarnings("unchecked")
	/*public static <T extends ComunEntidades> ResultFilterVo<T> doFilter(Integer firstResult, Integer maxResult, Class<T> entity,OrderParametersVo orderParametersVo, boolean doCount, FieldValuesVo... fieldValuesVos){
		Query q = null;
		List<T> lista = null;
		StringBuilder sb = new StringBuilder("");
		ResultFilterVo<T> resultFilterVo=null;
		List<String> params = new ArrayList<String>();
		String entityName =entity.getName().substring(entity.getName().lastIndexOf(".")+1);

		try{
		    em = EntityManagerHelper.getEntityManager();
		    //zera o contador que criará strings formato ":value1", ":value2" etc que representam os parametros na query
		    int paramNumber=0;
		    if(fieldValuesVos!=null)
			    for(int i=0;i<fieldValuesVos.length;i++)
					for(int x=0;x<fieldValuesVos[i].getValues().length;x++)
					    params.add(":value"+paramNumber++);
		    paramNumber=0;//contador zerado para agora adicionar os "params" à query
		    
		    sb.append("select e from ").append(entityName).append(" e");
		    if(fieldValuesVos!=null){//se for null significa um "ListAll" e portando não adiciona o where
		    	sb.append(" where");
	
			    for(int i=0;i<fieldValuesVos.length;i++){
					sb.append(" (");
					//caso seja DateFieldValuesVo e RULES=BETWEEN ja utiliza os dois valores, nao precisando entrar no loop getValues()
					//caso seja RULES=EQUALS pode ser tratado da mesma forma que numeros e entidades (usando =) o que ocorre no loop getValues()
					if(fieldValuesVos[i] instanceof DateFieldValuesVo &&
					    (fieldValuesVos[i]).getRule() == FieldValuesVo.COMPARATOR_RULE.BETWEEN){
			
					    sb.append(" e.").append(fieldValuesVos[i].getField()).append(" BETWEEN ")
						.append(params.get(paramNumber++)).append(" and").append(params.get(paramNumber++)).append(") and");
					}else{
					    //para valores do mesmo parâmetro usa vários "or" ao inves do in() pq in() não aceita entidades como valores
					    for(int x=0;x<fieldValuesVos[i].getValues().length;x++){
					    	//se null
					    	if(fieldValuesVos[i].getValues()[x] == null){
					    		sb.append(" e.").append(fieldValuesVos[i].getField()).append(" is null").append(" or");
					    		paramNumber++;
					    	}
					    	else{
					    	//deve ser "like"
								if( (fieldValuesVos[i]).getRule() == FieldValuesVo.COMPARATOR_RULE.LIKE)
								    sb.append(" UPPER(e.").append(fieldValuesVos[i].getField()).append(") LIKE ").append(params.get(paramNumber++)).append(" or");
								else 
									if( (fieldValuesVos[i]).getRule() == FieldValuesVo.COMPARATOR_RULE.NOT_EQUAL){
										//se for String usa upper (testa apenas a primeira posição pois todas deve ser do mesmo tipo)
										if(fieldValuesVos[i].getValues()[0] instanceof String)
											sb.append(" UPPER(e.").append(fieldValuesVos[i].getField()).append(") <> ").append(params.get(paramNumber++)).append(" or");
										else
											sb.append(" e.").append(fieldValuesVos[i].getField()).append(" <> ").append(params.get(paramNumber++)).append(" or");
									}
									else//supõe-se que seja um numero ou uma entidade, logo valor deve ser igual (=)
									    sb.append(" e.").append(fieldValuesVos[i].getField()).append("=").append(params.get(paramNumber++)).append(" or");
					    	}
					    }
					    //para parâmetros diferentes usa AND (remove o ultimo OR)
					    sb.replace(sb.length()-3, sb.length(), ") and");
					}
			    }
			    sb.replace(sb.length()-4, sb.length(), "");//remove o ultimo AND que está orfao
		    }
		    sb.append(" order by ");
		    
		    Set<Entry<String, OrderType>> set = orderParametersVo.getMap().entrySet();
		    Iterator<Entry<String, OrderType>> i = set.iterator();
		    while(i.hasNext()){
		    	Entry<String, OrderType> entry = i.next();
		    	sb.append("e.").append(entry.getKey()).append(" ").append(entry.getValue().getOrder()).append(", ");
		    }
		    sb.replace(sb.length() - 2, sb.length() - 1, "");
	
		    q = em.createQuery(sb.toString(),entity);
		    logger.warn("query criada antes de setar os valores: "+ sb.toString());
		    paramNumber=0;//zerado novamente, agora usado para setar os valores reais no lugar dos nomes criados anteriormente
		    
		    //seta os parametros
		    if(fieldValuesVos!=null)
		    	setParams(q,paramNumber,params,fieldValuesVos);

		    if(firstResult !=null)
		    	q.setFirstResult(firstResult);
		    if(maxResult !=null)
		    	q.setMaxResults(maxResult);

            lista = q.getResultList();
	    
            for (T t : lista)
                em.detach(t);
    
            Long count=null;
            if(doCount){
	            //usa a mesma query para realizar um count;
	            sb.replace(7, 8, "count(e)");
	            q = em.createQuery(sb.toString());
	            if(fieldValuesVos!=null)
	            	setParams(q,paramNumber,params,fieldValuesVos);
	            count = (Long)q.getSingleResult();
            }
            
            resultFilterVo = new ResultFilterVo<T>(lista, count);
            
		}catch(Exception e){
		    String msg = "Exceção em doFilter, entity:"+entity==null?null:entity.getName()+" ,";
		    if(fieldValuesVos!=null)
			    for(int i=0;i<fieldValuesVos.length;i++){
					msg+= "\nfield: "+fieldValuesVos[i].getField()+", ";
					for(int x=0;x<fieldValuesVos[i].getValues().length;x++)
					    msg+= "\n\tvalues: "+fieldValuesVos[i].getValues()[x]+", ";
			    }
		    logger.error(msg, e);
		    throw new JpaException(msg, e);
		}finally{
		    if(em!=null && em.isOpen())
		    	em.close();
		}
		return resultFilterVo;
    }
    
    private static void setParams(Query q, int paramNumber, List<String> params, FieldValuesVo... fieldValuesVos){
    	//seta os parametros
	    for(int i=0;i<fieldValuesVos.length;i++){
			for(int x=0;x<fieldValuesVos[i].getValues().length;x++){
			    if(fieldValuesVos[i] instanceof DateFieldValuesVo){
					if(fieldValuesVos[i].getValues()[x] instanceof Date)//seta date
					    q.setParameter(params.get(paramNumber++).substring(1), (Date)fieldValuesVos[i].getValues()[x],
						((DateFieldValuesVo)fieldValuesVos[i]).getTemporalType());
					else//seta calendar
					    q.setParameter(params.get(paramNumber++).substring(1), (Calendar)fieldValuesVos[i].getValues()[x],
						((DateFieldValuesVo)fieldValuesVos[i]).getTemporalType());
			    }else{
			    	//se nulo
			    	if(fieldValuesVos[i].getValues()[x] == null)		    		
			    		paramNumber++;//não faz nada pois não precisa setar um valor (já foi adicionado o "is null")
			    	else{
			    		//se for String concatena os %% do like
						if(fieldValuesVos[i].getValues()[x] instanceof String && fieldValuesVos[i].getRule() == FieldValuesVo.COMPARATOR_RULE.LIKE)
						    q.setParameter(params.get(paramNumber++).substring(1), "%"+((String)fieldValuesVos[i].getValues()[x]).toUpperCase()+"%");
						else
							if(fieldValuesVos[i].getRule() == FieldValuesVo.COMPARATOR_RULE.NOT_EQUAL){
								//se String adiciona upper
								if(fieldValuesVos[i].getValues()[x] instanceof String)
									q.setParameter(params.get(paramNumber++).substring(1), ((String)fieldValuesVos[i].getValues()[x]).toUpperCase());
								else//se nao for string apenas seta o parametro
									q.setParameter(params.get(paramNumber++).substring(1), fieldValuesVos[i].getValues()[x]);
							}
							else
							    q.setParameter(params.get(paramNumber++).substring(1), fieldValuesVos[i].getValues()[x]);
			    	}
			    }
			}
	    }
    }*/
    
   
    /**
     * Retorna uma entidade do id/tipo informado, apenas para entidades
     * onde a pk é um long
     * 
     * @param idRegistro id do registro
     * @param entidade pesquisada
     * @return entidade (registro) encontrado.
     * @throws JpaException para qualquer exceção interna ou própria do método,
     * podendo encapsular a exceção original, para isto use o getCause();
     */
    public static <T> T findById(long idRegistro, Class<T> entidade){
    	EntityManager em=null;
		T entity;
	
		try {
		    em = EntityManagerHelper.getEntityManager();
		    entity = em.find(entidade, idRegistro);
	
		}catch(Exception e){
		    String msg="Exceção em findById, parâmetros: idRegistro="
			    +idRegistro+"entidade="+entidade==null?null:entidade.getName();
		    System.out.println(msg + " - Exception: " + e);
		    throw new JpaException(msg, e);
		} finally {
		    if(em!=null && em.isOpen())
		    	em.close();
		}
		return entity;
    }

    /**
     * Recupera o registro da tabela informada, apenas para tabelas
     * onde a pk é um long
     *
     * @param idRegistro
     * @param nmTabela nome da tabela (não o nome da entidade)
     * @return Object entidade encontrada
     * @throws JpaException para qualquer exceção interna ou própria do método,
     * podendo encapsular a exceção original, para isto use o getCause();
     */
    public static Object findById(long idRegistro, String nmTabela){
    	EntityManager em=null;
		Object entity;
		Class<?> c = UtlEntity.getEntity(EntityManagerHelper.getEntityManager(),nmTabela);
		try {
		    em = EntityManagerHelper.getEntityManager();
		    entity = em.find(c, idRegistro);
	
		}catch(Exception e){
		    String msg="Exceção em findById, parâmetros: idRegistro="
			    +idRegistro+"nmTabela="+nmTabela;
		    System.out.println(msg + " - Exception: " + e);
		    throw new JpaException(msg, e);
		} finally {
		    if(em!=null && em.isOpen())
		    	em.close();
		}
			return entity;
    }
    
    public static <T> T getMax(String field, String entity){
    	EntityManager em=null;
    	Query q = null;
        T ent=null;
        em = EntityManagerHelper.getEntityManager();
        try {
            q = em.createQuery("select e from "+entity+" e where e."+field+"=(select MAX(a."+field+") from "+entity+" a)");
            //q.getSingleResult();
            ent = ((T) q.getSingleResult());
        }catch(Exception e){
        	String msg="Exceção em getMax";
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
        } finally {
        	if(em!=null && em.isOpen())
        		em.close();
        }
        return ent;
    }
    
    public static <T> T getMin(String field, String entity){
    	EntityManager em=null;
    	Query q = null;
        T ent=null;
        em = EntityManagerHelper.getEntityManager();
        try {
            q = em.createQuery("select e from "+entity+" e where e."+field+"=(select MIN(a."+field+") from "+entity+" a)");
            //q.getSingleResult();
            ent = ((T) q.getSingleResult());
        }catch(Exception e){
        	String msg="Exceção em getMin";
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
        } finally {
        	if(em!=null && em.isOpen())
        		em.close();
        }
        return ent;
    }
    
    public static void insert(ComunEntidades... entidades) {
    	EntityManager em=null;
		em = EntityManagerHelper.getEntityManager();
		try {
			EntityTransaction tx = em.getTransaction();
			if (!tx.isActive())
				tx.begin();
			for(ComunEntidades entidade:entidades)
				em.persist(entidade);
			tx.commit();
			for(ComunEntidades entidade:entidades)
				em.detach(entidade);
			
		}catch(Exception e){
			String msg="Exceção em insert, parâmetros: entidades=";
			if(entidades!=null)
				for(ComunEntidades i:entidades)
					msg+=i.getClass()+"\n";
			else
				msg+=null;
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
		} finally {
			if(em!=null && em.isOpen())
				em.close();
		}
	}
    
    /**
     * Realiza um insert ou update nas entidades enviadas. É esperado que toda entidade
     * possua o @ id como um long (primitivo), que será usado na regra (=0 insert !=0 update).
     * Caso não seja um long irá lançar uma exceção pois não está preparada para trabalhar com 
     * ids diferentes de long.
     * 
     * @param entidades a serem inseridas ou atualizadas (pode ser misturado e em qualquer ordem)
     */
    public static void insertOrUpdate(ComunEntidades... entidades) {
    	
    	insertOrUpdate(null,entidades);
	}
    
	/**
	 * Esta classe deve ser enviada ao JpaAllEntities.insertOrUpdate(DoAfter
	 * doAfter,ComunEntidades... entidades) quando for necessário inserir/atualizar uma
	 * entidade que depende que outras sejam inseridas/atualizadas
	 * anteriormente, normalmente para a geração de um ID. o método setValues
	 * deve ser implementado atualizando os valores das entidades e devem
	 * retornar APENAS estas entidades e NAO as presentes na chamada do
	 * insertOrUpdate exemplo para teste (repare que é feito um delete no
	 * final):
	 * 
	 * public static void test133() {
	 * 
	 * EntPesPessoa p1 = new EntPesPessoa(); EntPesTipoPessoa t =
	 * JpaAllEntities.findById(1,EntPesTipoPessoa.class);
	 * EntPesTipoIdentifFiscal i =
	 * JpaAllEntities.findById(1,EntPesTipoIdentifFiscal.class);
	 * p1.setPesTipoPessoa(t); p1.setPesTipoIdentifFiscal(i);
	 * p1.setSnoIdentificadorFiscal("35427053735"); p1.setSnmPessoa("teste");
	 * 
	 * EntPesPessoaJuridica j = new EntPesPessoaJuridica(); EntPesTipoSociedade
	 * s = JpaAllEntities.findById(1,EntPesTipoSociedade.class);
	 * j.setPesPessoa(p1); j.setSnmFantasia("fantasia");
	 * j.setSnoInscricaoEstadual("estadual"); j.setPesTipoSociedade(s);
	 * 
	 * EntPccIncorporador inc = new EntPccIncorporador(); EntParTipoLogradouro l
	 * = JpaAllEntities.findById(2,EntParTipoLogradouro.class); EntParCep cep =
	 * JpaAllEntities.findById(293191,EntParCep.class);
	 * inc.setParTipoLogradouro(l); inc.setParCep(cep); inc.setPesPessoa(p1);
	 * 
	 * final EntPesOperacaoPessoa op = new EntPesOperacaoPessoa();
	 * EntPesTipoOperacaoPessoa top =
	 * JpaAllEntities.findById(4,EntPesTipoOperacaoPessoa.class);
	 * op.setPesTipoOperacaoPessoa(top);
	 * 
	 * JpaAllEntities.insertOrUpdate(new JpaAllEntities.DoAfter() {
	 * 
	 * @Override public ComunEntidades[] setValues(ComunEntidades... arg1) {
	 *           op.setPesPessoa((EntPesPessoa)arg1[0]);
	 *           op.setInoUnicoRegistro((
	 *           (EntPccIncorporador)arg1[2]).getIdIncorporador()); return new
	 *           ComunEntidades[]{op}; } }, p1,j,inc);
	 * 
	 *           System.out.println(p1.log()); System.out.println(j.log());
	 *           System.out.println(inc.log()); System.out.println(op.log());
	 * 
	 *           JpaAllEntities.delete(p1,j,inc,op); }
	 * 
	 * @author fausto
	 * 
	 */
    public static abstract class DoAfter{
    	/**
    	 * Nenhuma chamada a camada JPA é permitida pois ela fechara o EntityManager causando exceção
    	 * @param entidades
    	 * @return
    	 */
    	public abstract ComunEntidades[] setValues(ComunEntidades... entidades);
    	
    	public void insertOrUpdate(EntityManager em, EntityTransaction tx, ComunEntidades... entidades) throws IllegalArgumentException, IllegalAccessException{
    					
			for(int i=0;i<entidades.length;i++){
				
				Field f = UtlEntity.getIdField(entidades[i].getClass());
				
				if(f!=null){
					f.setAccessible(true);
					Object ob = f.get(entidades[i]);
					if(ob != null){
						if(ob instanceof Long){
							Long l = (Long)ob;
							if(l==0){//insert
//								logger.debug("insert em: " + entidades[i].log());
								em.persist(entidades[i]);
							}else{//update
//								logger.debug("update em: " + entidades[i].log());
								entidades[i] = em.merge(entidades[i]);
							}
						}else
							throw new UnsupportedOperationException("Chave primária não é do tipo long para "+entidades[i].getClass()+", informe o responsável pelas entidades");
					}else
						throw new JpaException("Chave primaria nula na entidade (indica não ser tipo primitivo long) "+entidades[i].getClass());
				}else
					throw new JpaException("Chave primaria não encontrada na entidade "+entidades[i].getClass());
			}
    	}
    }
    
    public static void insertOrUpdate(DoAfter doAfter,ComunEntidades... entidades) {
    	EntityManager em=null;
		em = EntityManagerHelper.getEntityManager();
		
		try {
			EntityTransaction tx = em.getTransaction();
			
			if (!tx.isActive())
				tx.begin();
			
			for(int i=0;i<entidades.length;i++){
				
				Field f = UtlEntity.getIdField(entidades[i].getClass());
				
				if(f!=null){
					f.setAccessible(true);
					Object ob = f.get(entidades[i]);
					if(ob != null){
						if(ob instanceof Long){
							Long l = (Long)ob;
							if(l==0){//insert
//								logger.debug("insert em: " + entidades[i].log());
								em.persist(entidades[i]);
							}else{//update
//								logger.debug("update em: " + entidades[i].log());
								entidades[i] = em.merge(entidades[i]);
							}
						}else if(ob instanceof String){
							String l = (String)ob;
							if(l.equals(StringUtils.EMPTY)){//insert
//								logger.debug("insert em: " + entidades[i].log());
								em.persist(entidades[i]);
							}else{//update
//								logger.debug("update em: " + entidades[i].log());
								entidades[i] = em.merge(entidades[i]);
							}
						}
						else
							throw new UnsupportedOperationException("Chave primária não é do tipo long para "+entidades[i].getClass()+", informe o responsável pelas entidades");
					}else
						throw new JpaException("Chave primaria nula na entidade (indica não ser tipo primitivo long) "+entidades[i].getClass());
				}else
					throw new JpaException("Chave primaria não encontrada na entidade "+entidades[i].getClass());
			}
			
			if(doAfter!=null){
				doAfter.insertOrUpdate(em,tx, doAfter.setValues(entidades));
			}
			
			tx.commit();
			
		}catch(Exception e){
			String msg="Exceção em insertOrUpdate, parâmetros: entidades=";
			if(entidades!=null)
				for(ComunEntidades i:entidades)
					msg+=i.getClass()+"\n";
			else
				msg+=null;
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
		} finally {
			if(em!=null && em.isOpen()){
				em.clear();
				em.close();
			}
		}
	}
    
    public static void delete(ComunEntidades... entidades) {
    	EntityManager em=null;
		em = EntityManagerHelper.getEntityManager();
		
		try {
			ComunEntidades[] ents = new ComunEntidades[entidades.length];
			EntityTransaction tx = em.getTransaction();
			if (!tx.isActive())
				tx.begin();
			for(int i=0;i<entidades.length;i++)
				ents[i] = em.merge(entidades[i]);
			
			for(ComunEntidades entidade:ents)
				em.remove(entidade);
			
			tx.commit();
			
		}catch(Exception e){
			String msg="Exceção em delete, parâmetros: entidades=";
			if(entidades!=null)
				for(ComunEntidades i:entidades)
					msg+=i.getClass()+"\n";
			else
				msg+=null;
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
		} finally {
			if(em!=null && em.isOpen())
				em.close();
		}
	}
    
    public static ComunEntidades[] update(ComunEntidades... entidades) {
    	EntityManager em=null;
    	em = EntityManagerHelper.getEntityManager();
		try {
			
			EntityTransaction tx = em.getTransaction();
			if (!tx.isActive())
				tx.begin();
			for(int i=0;i<entidades.length;i++)
				entidades[i] = em.merge(entidades[i]);
			tx.commit();
			for(ComunEntidades entidade:entidades)
				em.detach(entidade);
			
		}catch(Exception e){
			String msg="Exceção em update, parâmetros: entidades=";
			if(entidades!=null)
				for(ComunEntidades i:entidades)
					msg+=i.getClass()+"\n";
			else
				msg+=null;
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
		} finally {
			if(em!=null && em.isOpen())
				em.close();
		}
    	return entidades;
    }
    
    public static <T extends ComunEntidades> long count(Class<T> entidade) {
    	EntityManager em=null;
        Query q = null;
        long count=0;
        em = EntityManagerHelper.getEntityManager();
        try {
            q = em.createQuery("select count(u) from "+entidade.getSimpleName()+" as u");
            count = ((Long) q.getSingleResult()).longValue();
        }catch(Exception e){
        	String msg="Exceção em count";
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
        } finally {
        	if(em!=null && em.isOpen())
        		em.close();
        }
        return count;
    }
    
    public static <T extends ComunEntidades> List<T> listAll(Class<T> entidade) {
        return listAll(null, null, entidade, OrderType.ASC, UtlEntity.getIdFieldName(entidade));
    }

    public static <T extends ComunEntidades> List<T> listAll(Integer firstResult, Integer maxResult, Class<T> entidade) {
        return listAll(firstResult, maxResult, entidade, OrderType.ASC, UtlEntity.getIdFieldName(entidade));
    }
    
    @SuppressWarnings("unchecked")
	public static <T extends ComunEntidades> List<T> listAll(Integer firstResult, Integer maxResult, Class<T> entity,
			OrderType order, String... orderFields) {
    	
    	OrderParametersVo orderParametersVo = new OrderParametersVo(order, orderFields);
    	EntityManager em=null;
    	
        Query q = null;
        List<T> lista = Collections.EMPTY_LIST;
        try {
        	StringBuilder[] sbOrderArray= processOrderParametersVo(entity, orderParametersVo);
	        em = EntityManagerHelper.getEntityManager();
	
	        StringBuilder sb = new StringBuilder();
	        sb.append("select e from ").append(entity.getSimpleName()).append(" as e ").append(sbOrderArray[0]).append("order by ").append(sbOrderArray[1]);;
	        /*for (String o : orderFields) {
	            sb.append("e.").append(o).append(" ").append(order.getOrder()).append(", ");
	        }
	        sb.replace(sb.length() - 2, sb.length() - 1, "");*/
        
            q = em.createQuery(sb.toString());
            if (firstResult != null && firstResult >= 0) {
                q.setFirstResult(firstResult);
            }
            if (maxResult != null && maxResult > 0) {
                q.setMaxResults(maxResult);
            }
            lista = q.getResultList();
            for (T ih : lista) 
                em.detach(ih);
            
        }catch(Exception e){
        	String msg="Exceção em listAll, parâmetros: firstResult="
        		+firstResult+" maxResult="+maxResult+", order="+order
        		+" entidade="+(entity==null?null:entity.getName());
        	for(String s:orderFields)
        		msg+=", orderFields="+s;
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
        } finally {
        	if(em!=null && em.isOpen())
        		em.close();
        }
        return lista;
    }
    
    public static <T extends ComunEntidades> List<T> getDateBetween2Columns(Class<T> entidade,Date dt, String field1, String field2) {
        return getDateBetween2Columns(null, null, entidade,dt,field1,field2, OrderType.ASC, UtlEntity.getIdFieldName(entidade));
    }

    public static <T extends ComunEntidades> List<T> getDateBetween2Columns(Integer firstResult, Integer maxResult, Class<T> entidade,Date dt, String field1, String field2) {
        return getDateBetween2Columns(firstResult, maxResult, entidade, dt,field1,field2,OrderType.ASC, UtlEntity.getIdFieldName(entidade));
    }
    
    /**
     * Pesquisa por uma data que esteja no intervalo de duas colunas, normalmente duas colunas representando data inicial e data final.
     * Para se comparar UMA COLUNA no intervalo de DUAS datas utilizar o doFilter
     * 
     * @param <T>
     * @param firstResult
     * @param maxResult
     * @param entidade
     * @param dt
     * @param field1
     * @param field2
     * @param order
     * @param orderFields
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T extends ComunEntidades> List<T> getDateBetween2Columns(Integer firstResult, Integer maxResult, Class<T> entidade,
			Date dt, String field1, String field2, OrderType order, String... orderFields) {
    	
        Query q = null;
        List<T> lista = Collections.EMPTY_LIST;
        EntityManager em=null;
        try {
	        em = EntityManagerHelper.getEntityManager();
	
	        StringBuilder sb = new StringBuilder();
	        sb.append("select e from ").append(entidade.getSimpleName()).append(" as e ").append("where :dt").append(" between e.").append(field1).append(" and e.").append(field2).append(" order by ");
	        for (String o : orderFields) {
	            sb.append("e.").append(o).append(" ").append(order.getOrder()).append(", ");
	        }
	        sb.replace(sb.length() - 2, sb.length() - 1, "");
	        System.out.println(dt);
	        System.out.println(sb.toString());
            q = em.createQuery(sb.toString());
            q.setParameter("dt", dt);
            if (firstResult != null && firstResult >= 0) {
                q.setFirstResult(firstResult);
            }
            if (maxResult != null && maxResult > 0) {
                q.setMaxResults(maxResult);
            }
            lista = q.getResultList();
            for (T ih : lista) 
                em.detach(ih);
            
        }catch(Exception e){
        	String msg="Exceção em getBetween, parâmetros: firstResult="
        		+firstResult+" maxResult="+maxResult+", order="+order+"dt="+dt+"field1="+field1+"field2="+field2
        		+"entidade="+(entidade==null?null:entidade.getName());
        	for(String s:orderFields)
        		msg+=", orderFields="+s;
        	System.out.println(msg + " - Exception: " + e);
        	throw new JpaException(msg, e);
        } finally {
        	if(em!=null && em.isOpen())
        		em.close();
        }
        return lista;
    }
}
