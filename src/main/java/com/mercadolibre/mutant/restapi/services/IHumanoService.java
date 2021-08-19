package com.mercadolibre.mutant.restapi.services;

import java.util.List;

import com.mercadolibre.mutant.restapi.dto.EstadisticaDTO;
import com.mercadolibre.mutant.restapi.dto.HumanoDTO;
import com.mercadolibre.mutant.restapi.entities.Humano;

public interface IHumanoService 
{
	public Integer cantidadMutantes(HumanoDTO humanoDTO);
	public EstadisticaDTO estadisticas();
	public List<Humano> findAll();
	public Humano guardarHumano(Humano humano);
	public Humano actualizarHumano(Humano humano);
	public Float calcularPromedio(Integer totalHumanos, Integer cantidadMutantes);
}