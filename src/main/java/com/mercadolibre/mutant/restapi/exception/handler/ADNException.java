package com.mercadolibre.mutant.restapi.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mercadolibre.mutant.restapi.exception.CaracterInvalidoException;
import com.mercadolibre.mutant.restapi.exception.DivisionException;
import com.mercadolibre.mutant.restapi.exception.EstructuraArrayInvalidoException;
import com.mercadolibre.mutant.restapi.exception.TamanoArrayException;

@ControllerAdvice
public class ADNException 
{
	@ExceptionHandler(value = EstructuraArrayInvalidoException.class)
	public ResponseEntity<Object> estructuraException(EstructuraArrayInvalidoException exception)
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("error", "La estructura del Array debe ser NxN");
		return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = CaracterInvalidoException.class)
	public ResponseEntity<Object> caracterException(CaracterInvalidoException exception)
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("error", "Los caracteres ingresados en el array solo pueden ser: (A, T, C, G)");
		return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = TamanoArrayException.class)
	public ResponseEntity<Object> tamanoException(TamanoArrayException exception)
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("error", "El tamaño del array debe ser mínimo de 3 posiciones");
		return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = DivisionException.class)
	public ResponseEntity<Object> divisionException(DivisionException exception)
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("error", "Debe existir por lo menos un registro en la base de datos, no se puede dividir por cero.");
		return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
	}
}