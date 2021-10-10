package simpleapi.errors;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 1902299253413707980L;

	public String code;
	
	public String errorMessage;
	
	String fix;
	
	@JsonIgnore
	ErrorsEnum erro;
	
	public ErrorMessage(ErrorsEnum erro) {
		this.code = erro.getCode();
		this.errorMessage = erro.getErrorMessage();
		this.fix = erro.getFix();
		this.erro = erro;
	}
	
	public String getMensagemFormatada() {
		return this.getCode() + " - " + this.getErrorMessage() + ": " + this.getFix();
	}

	public ErrorMessage() {
	}

	public String getCode() {
		return code;
	}

	public ErrorMessage setCode(String code) {
		this.code = code;
		return this;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ErrorMessage setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public String getFix() {
		return fix;
	}

	public ErrorMessage setFix(String fix) {
		this.fix = fix;
		return this;
	}

	public ErrorsEnum getErro() {
		return erro;
	}

	public void setErro(ErrorsEnum erro) {
		this.erro = erro;
	}

}
