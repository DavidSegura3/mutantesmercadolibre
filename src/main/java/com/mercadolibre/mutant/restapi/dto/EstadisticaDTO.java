package com.mercadolibre.mutant.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaDTO 
{
	private Integer cantidadMutantes;
	private Integer cantidadHumanos;
	private Float promedio;
}