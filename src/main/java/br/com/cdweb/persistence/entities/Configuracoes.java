package br.com.cdweb.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Configuracoes
 *
 */
@Entity

public class Configuracoes extends ComunEntidades implements Serializable{
	
	@Id
	private String chave;
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	private String valor;
	
	private static final long serialVersionUID = 1L;

	public Configuracoes() {
		super();
	}
   
}
