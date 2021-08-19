package com.mercadolibre.mutant.restapi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.mutant.restapi.dto.EstadisticaDTO;
import com.mercadolibre.mutant.restapi.dto.HumanoDTO;
import com.mercadolibre.mutant.restapi.services.IHumanoService;

@RestController
@RequestMapping("/api")
public class HumanoController 
{
	@Autowired
	private IHumanoService humanoService;
	
	/**
	 * Método para validar si un humano es mutante o no.
	 * @param humanoDTO Objeto array con la información a validar. 
	 * @return Retorna un cantidad de humanos mutantes dependiendo las validaciones verticales,
	 * horizontales, oblicuo por izquierdo o por derecho, también depende de la respuesta retorna un 
	 * mensaje de mutante o no mutante y cada uno con su respectivo código.
	 * @author NDSC - 01-08-2021.
	 */
	@PostMapping("/mutant")
	public ResponseEntity<?> buscarMutante(@RequestBody HumanoDTO humanoDTO)
	{
		Map<String, Object> respuesta = new HashMap<String, Object>();
		
		Integer mutantes = humanoService.cantidadMutantes(humanoDTO);
		
		if(mutantes >= 2)
		{
			respuesta.put("mensaje", "Es mutante");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
		}
		else
		{
			respuesta.put("mensaje", "No hay mutantes");
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.FORBIDDEN);
		}
	}
	
	/**
	 * Método para traer las estadisticas de la base de datos con los humanos mutantes.
	 * @return Retorna un objeto DTO con la información de la cantidad de humanos,
	 * la cantidad de mutantes y el promedio de los mutantes almacenados.
	 * @author NDSC - 15-08-2021.
	 */
	@GetMapping("/stats")
	public ResponseEntity<?> verificarEstadisticas()
	{
		EstadisticaDTO estadisticas = humanoService.estadisticas();
		return new ResponseEntity<EstadisticaDTO>(estadisticas, HttpStatus.OK);
	}
}