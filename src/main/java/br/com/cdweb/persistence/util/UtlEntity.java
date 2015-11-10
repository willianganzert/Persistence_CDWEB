package br.com.cdweb.persistence.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

public class UtlEntity {

	/**
	 * Retorna a nome da tabela (toda em maiúscula) da entidade informada.
	 * @param Class c classe da entidade
	 * @return String nome da tabela
	 */
	public static String getTableName(Class<?> c){
		String name=null;

		if(c.getAnnotation(Entity.class)==null)
		    throw new RuntimeException("Classe informada não é uma entidade, não possui @Entity");

		Table t;
		if((t=(Table)c.getAnnotation(Table.class))==null)
			name = c.getName().substring(c.getName().lastIndexOf(".")+1, c.getName().length()).toUpperCase();
		else
			name = t.name();
		
		return name;
	}
	
	/**
	 * Retorna o nome do atributo (java) usado como chave primária. Note que este nome pode ser
	 * diferente do nome da coluna usada como pk. Não serve  para chave primárias composta.
	 * @param Class c classe da entidade
	 * @return String nome do atributo da chave primária
	 */
	public static String getIdFieldName(Class<?> c){
		String idField=null;

		if(c.getAnnotation(Entity.class)==null)
		    throw new RuntimeException("Classe informada não é uma entidade, não possui @Entity");
		
		Field[] fields = c.getDeclaredFields();
		
		for (Field field : fields) {
			if((field.getAnnotation(Id.class))!=null){
				idField = field.getName();
				break;
			}
		}
				
		return idField;
	}
	
	public static Field getIdField(Class<?> c){

		if(c.getAnnotation(Entity.class)==null)
		    throw new RuntimeException("Classe informada não é uma entidade, não possui @Entity");
		
		Field[] fields = c.getDeclaredFields();
		
		for (Field field : fields) {
			if((field.getAnnotation(Id.class))!=null)
				return field;
		}
		return null;
	}

	/**
	 * Retorna o nome do atributo da classe que corresponde ao nome da coluna informada.
	 * Exemplo: nome da coluna: SCDCEP retorna: scdCep.
	 * @param Class<?> c classe da entidade
	 * @param String columnName
	 * @return String field name
	 */
	public static String getFieldNameFromColumnName(Class<?> c, String columnName){
		String fieldName=null;

		if(c.getAnnotation(Entity.class)==null)
		    throw new RuntimeException("Classe informada não é uma entidade, não possui @Entity");
		
		Field[] fields = c.getDeclaredFields();
		
		for (Field field : fields) {
			if((field.getName().equalsIgnoreCase(columnName))){
				fieldName = field.getName();
				break;
			}
		}
				
		return fieldName;
	}
	
	/**
	 * Retorna todas as entidades de uma persistence unit.
	 * 
	 * @param em um EntityManager já configurado e válido
	 * @return lista no formato List<EntityType<?>> com todas as entidades da persistence unit
	 * utilizada para criar o EntityManager
	 */
	public static List<EntityType<?>> getAllEntitiesFromPersistenceUnit(EntityManager em){
		List<EntityType<?>> entidades=null;
		try{
			EntityManagerFactory emf = em.getEntityManagerFactory();
			LinkedHashSet<EntityType<?>> set = (LinkedHashSet<EntityType<?>>)emf.getMetamodel().getEntities();
			entidades = new ArrayList<EntityType<?>>(Arrays.asList((EntityType<?>[])set.toArray(new EntityType<?>[0])));
		}finally{
			if(em!=null && em.isOpen())
				em.close();
		}
		return entidades;
	}

	/**
	 * Retorna a classe (entidade) baseada no nome da tabela
	 * 
	 * @param nmTabela
	 * @return
	 */
	public static Class<?> getEntity(EntityManager em, String nmTabela) {
		try{
			List<EntityType<?>> entidades = UtlEntity.getAllEntitiesFromPersistenceUnit(em);
			for (EntityType<?> entidade : entidades) {
				if (UtlEntity.getTableName(entidade.getBindableJavaType()).equalsIgnoreCase(nmTabela))
					return entidade.getBindableJavaType();
			}
		}finally{
			if(em!=null && em.isOpen())
				em.close();
		}
		return null;
	}
		
	/**
	 * Retorna uma lista de String com os nomes de todos os atributos da entidade enviada
	 * que correspondem à colunas no banco de dados. NÃO retorna @transient, listas e outros
	 * atributos que não são colunas.
	 * 
	 * Obs.: Pode-se enviar um EntityType já que este implementa ManagedType
	 * 
	 * @param entityType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getColumnsFromManagedType(ManagedType managedType){
		Set s2=managedType.getSingularAttributes();
		List<String> lista = new ArrayList<String>();
		for(Object o:s2.toArray()){
			lista.add(((SingularAttribute)o).getName());
		}
		return lista;
	}
	
	/**
	 * 
	 * @param <T> entidade, que EXISTA no EntityManager informado
	 * @param em na qual será procurado a entity
	 * @param entity a ser pesquisada as colunas
	 * @return lista de strings que são os nomes das colunas da tabela mapeada pela entidade
	 * @throws UtlException caso entidade não exista no EntityManager informado
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<String> getColumnsFromEntity(EntityManager em, Class<T> entity){
		EntityType tipo = null;
		List<EntityType<?>> entidades = UtlEntity.getAllEntitiesFromPersistenceUnit(em);
		
		//recupera a EntityType correspondente à entidade enviada
		for (EntityType<?> entityType : entidades) {
			if(entity.getName().equals(entityType.getBindableJavaType().getName())){
				tipo = entityType;
				break;
			}
		}
		
		if(tipo == null)
                    System.out.println("Exceção em getColumnsFromEntityType, parâmetros: " 
				+"em="+em==null?null:em
				+", entity (nome da classe)="+entity==null?null:entity.getName()
				+"\nO parâmetro entity informado não corresponde à nenhuma entidade do " 
				+"EntityManager informado");
		
		return getColumnsFromManagedType(tipo);
	}
	
}

