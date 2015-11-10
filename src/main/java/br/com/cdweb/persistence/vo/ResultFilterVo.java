package br.com.cdweb.persistence.vo;



import java.util.List;

/**
 * @author willian
 *
 * @param <T>
 */
public class ResultFilterVo <T> {

	private Long total;
	private List<T> resultQuery;
	
	public ResultFilterVo(List<T> resultQuery, Long total){
		this.total=total;
		this.resultQuery=resultQuery;
	}

	public List<T> getResultQuery() {
		return resultQuery;
	}

	public Long getTotal() {
		return total;
	}
	
}

