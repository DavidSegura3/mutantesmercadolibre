package com.mercadolibre.mutant.restapi.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ArrayInvalidoException extends RuntimeException 
{
	public ArrayInvalidoException()
	{
		super();
	}
	
	public ArrayInvalidoException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public ArrayInvalidoException(String message)
	{
		super(message);
	}
	
	public ArrayInvalidoException(Throwable cause)
	{
		super(cause);
	}


	private static final long serialVersionUID = 4776821567759178115L;
	
}