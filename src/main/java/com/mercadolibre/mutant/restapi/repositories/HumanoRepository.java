package com.mercadolibre.mutant.restapi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mercadolibre.mutant.restapi.entities.Humano;

@Repository
public interface HumanoRepository extends PagingAndSortingRepository<Humano, Long> 
{
	public Humano findByAdn(String adn);
}