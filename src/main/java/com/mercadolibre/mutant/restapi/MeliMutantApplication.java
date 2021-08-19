package com.mercadolibre.mutant.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MeliMutantApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(MeliMutantApplication.class, args);
	}
}