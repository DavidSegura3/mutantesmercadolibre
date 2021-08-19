package com.mercadolibre.mutant.restapi.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mercadolibre.mutant.restapi.dto.EstadisticaDTO;
import com.mercadolibre.mutant.restapi.dto.HumanoDTO;
import com.mercadolibre.mutant.restapi.entities.Humano;
import com.mercadolibre.mutant.restapi.exception.CaracterInvalidoException;
import com.mercadolibre.mutant.restapi.exception.DivisionException;
import com.mercadolibre.mutant.restapi.exception.EstructuraArrayInvalidoException;
import com.mercadolibre.mutant.restapi.exception.TamanoArrayException;
import com.mercadolibre.mutant.restapi.repositories.HumanoRepository;


@Service
@CacheConfig(cacheNames={"humanos"})
public class HumanoServiceImpl implements IHumanoService 
{
	public static final Integer CANTIDAD_MINIMA_MUTANTES = 2; 
	
	@Autowired
	private HumanoRepository humanoRepository;
	
	@Override
	@Cacheable
	public Integer cantidadMutantes(HumanoDTO humanoDTO) 
	{
		Integer acumuladorMutantes = 0;
		
		Boolean tamanoArray = longitudArray(humanoDTO.getAdn());
		if(tamanoArray != true)
			throw new TamanoArrayException();
		
		Boolean arrayValido = validarArrayNxN(humanoDTO.getAdn());
		if (arrayValido != true) 
			throw new EstructuraArrayInvalidoException();
		
		Boolean caracteresValidos = validarCaracteresPermitidos(humanoDTO.getAdn());
		if(caracteresValidos != true)
			throw new CaracterInvalidoException();
		
		acumuladorMutantes = validarHorizontal(humanoDTO.getAdn());
		acumuladorMutantes = validarVertical(humanoDTO.getAdn(), acumuladorMutantes);
		acumuladorMutantes = validarOblicuoDerecho(humanoDTO.getAdn(), acumuladorMutantes);
		acumuladorMutantes = validarOblicuoIzquierdo(humanoDTO.getAdn(), acumuladorMutantes);
		
		return acumuladorMutantes;
	}
	
	/**
	 * Método para guardar un humano en la base de datos.
	 * @param adn Adn a guardar.
	 * @author NDSC - 13-08-2021
	 */
	private void guardarHumano(String adn)
	{
		Humano humanoBuscado = buscarHumano(adn);
		if(humanoBuscado == null)
		{
			Humano humano = new Humano();
			humano.setAdn(adn);
			humano.setMutante(false);
			humanoRepository.save(humano);
		}
	}
	
	/**
	 * Método para buscar un humano por el adn.
	 * @param adn Párametro para buscar el adn.
	 * @return Retorna un humano si lo encuentra.
	 * @author NDSC - 13-08-2021.
	 */
	private Humano buscarHumano(String adn)
	{
		Humano humano = humanoRepository.findByAdn(adn);
		return humano;
	}
	
	/**
	 * Método para modificar un humano en la base de datos.
	 * @param mutante Parámetro de cuando encuentra un mutante y lo cambia en la base de datos.
	 * @author NDSC - 13-08-2021
	 */
	private void modificarHumano(String adn)
	{
		Humano humano = buscarHumano(adn);
    	if(humano != null)
    	{
    		humano.setMutante(true);
    		humanoRepository.save(humano);
    	}
	}
	
	
	/**
	 * Método para validar el array de manera horizontal.
	 * @param adn Array a validar.
	 * @return Retorna un entero dependiendo la cantidad de mutantes que encuentre.
	 * @author NDSC - 11-08-2021
	 */
	private Integer validarHorizontal(String[] adn)
	{
		Integer cantidadMutantes = 0;
		
		for(String s : adn)
        {
			guardarHumano(s);
			for(int i = 0; i<s.length() - 3 ; i++)
            {
                if(compararCaracterPorCaracter(s.charAt(i), s.charAt(i+1), s.charAt(i+2), s.charAt(i+3)))
                {
                	cantidadMutantes++;
                	modificarHumano(s);
                	
                	if(cantidadMutantes >= CANTIDAD_MINIMA_MUTANTES)
                    {
                    	return cantidadMutantes;
                    }
                }
            }
        }
		return cantidadMutantes;
	}
	
	/**
     * Método para validar las la longitud mínima permitida del array.
     * @param adn Array con la información a trabajar.
     * @return Retorna un valor booleano dependiendo si cumple o no cumple con la longitud.
     * @author DavidSegura - 05-08-2021.
     */
    private boolean longitudArray(String[] adn)
    {
        Integer tamanoArray = adn.length;
        if(tamanoArray > 3)
            return true;
        else
            return false;
    }
	
    /**
     * Método para comparar caracter por caracter y validar si hay secuencia
     * @param c1 Parámetro caracter 1
     * @param c2 Parámetro caracter 2
     * @param c3 Parámetro caracter 3
     * @param c4 Parámetro caracter 4
     * @return Dependiendo de la comparación de cada caracter por caracter si los cuatro caracteres son iguales
     * retorna un true, de lo contrario retorna un false.
     * @author NDSC - 11-08-2021
     */
	private boolean compararCaracterPorCaracter(char c1, char c2, char c3, char c4)
    {
        return c1 == c2 && c1 == c3 && c1 == c4;
    }
	
	/**
	 * Método para validar el array de manera horizontal.
	 * @param adn Array a validar
	 * @param contadorCantidadMutantes valor de la variable acumulable en los otros metodos.
	 * @return Retorna un valor entero con la cantidad de mutantes encontrados.
	 * @author NDSC - 12-08-2021.
	 */
	public Integer validarVertical(String[] adn, Integer contadorCantidadMutantes)
    {
        if(contadorCantidadMutantes >= CANTIDAD_MINIMA_MUTANTES)
        {
            return contadorCantidadMutantes;
        }
        
        List<String> dna = Arrays.asList(adn);
        
        for(int i = 0; i < dna.size(); i++)
        {
        	for(int j = 0; j < dna.get(i).length() - 3; j++)
            {
        		
        		if(compararCaracterPorCaracter(dna.get(j).charAt(i), dna.get(j+1).charAt(i), dna.get(j+2).charAt(i), dna.get(j+3).charAt(i)))
                {
                	String adnMutante = String.valueOf(dna.get(j).charAt(i)) + String.valueOf(dna.get(j+1).charAt(i)) + String.valueOf(dna.get(j+2).charAt(i)) + String.valueOf(dna.get(j+3).charAt(i));
                	guardarVerticalYOblicuos(adnMutante);
                	
                	contadorCantidadMutantes++;
                    
            		if(contadorCantidadMutantes >= CANTIDAD_MINIMA_MUTANTES)
                    {
                        return contadorCantidadMutantes;
                    }
                }
            }
        }
        return contadorCantidadMutantes;
    }
	
	/**
	 * Método para validar oblicuamente hacia la derecha la cantidad de mutantes.
	 * @param adn Array a validar.
	 * @param contadorCantidadMutantes Acumulador de mutantes.
	 * @return Retorna un valor entero dependiendo la cantidad de mutantes que encuentre.
	 * @author NDSC - 11-08-2021
	 */
	private Integer validarOblicuoDerecho(String[] adn, Integer contadorCantidadMutantes)
    {
		if(contadorCantidadMutantes > CANTIDAD_MINIMA_MUTANTES)
        {
            return contadorCantidadMutantes;
        }
        
        List<String> dna = Arrays.asList(adn);
        
        for(int i = 0; i< dna.size()-3; i++)
        {
            for(int j = 0; j < dna.get(i).length() - 3 ; j++)
            {
                if(compararCaracterPorCaracter(dna.get(i).charAt(j), dna.get(i+1).charAt(j+1), dna.get(i+2).charAt(j+2), dna.get(i+3).charAt(j+3)))
                {
                	String adnMutante = String.valueOf(dna.get(i).charAt(j)) + String.valueOf(dna.get(i+1).charAt(j+1)) + String.valueOf(dna.get(i+2).charAt(j+2)) + String.valueOf(dna.get(i+3).charAt(j+3));
                	guardarVerticalYOblicuos(adnMutante);
                	
                	contadorCantidadMutantes++;
                    if(contadorCantidadMutantes >= CANTIDAD_MINIMA_MUTANTES)
                    {
                        return contadorCantidadMutantes;
                    }
                }
            }
        }
        return contadorCantidadMutantes;
    }
	
	/**
	 * Método para validar oblicuamente hacia la derecha la cantidad de mutantes.
	 * @param adn Array a validar.
	 * @param contadorCantidadMutantes Acumulador de mutantes.
	 * @return Retorna un valor entero dependiendo la cantidad de mutantes que encuentre.
	 * @author NDSC - 12-08-2021
	 */
	public Integer validarOblicuoIzquierdo(String[] adn, Integer contadorCantidadMutantes)
    {
		if(contadorCantidadMutantes >= CANTIDAD_MINIMA_MUTANTES)
        {
            return contadorCantidadMutantes;
        }
        List<String> dna = Arrays.asList(adn);
        
        for(int i = 0; i < dna.size()-3; i++)
        {
            Integer tamano = dna.get(i).length();
            for(int j = 0; j < tamano-3; j++)
            {
                if(compararCaracterPorCaracter(dna.get(i).charAt(tamano-j-1), dna.get(i+1).charAt(tamano-j-2), dna.get(i+2).charAt(tamano-j-3), dna.get(i+3).charAt(tamano-j-4)))
                {
                	String adnMutante = String.valueOf(dna.get(i).charAt(tamano-j-1)) + String.valueOf(dna.get(i+1).charAt(tamano-j-2)) + String.valueOf(dna.get(i+2).charAt(tamano-j-3)) + String.valueOf(dna.get(i+3).charAt(tamano-j-4));
                	
                	guardarVerticalYOblicuos(adnMutante);
                	contadorCantidadMutantes++;
                    if(contadorCantidadMutantes >= CANTIDAD_MINIMA_MUTANTES)
                    {
                        return contadorCantidadMutantes;
                    }
                }
            }
        }
        return contadorCantidadMutantes;
    }
	
	/**
	 * Método para validar que la matriz sea cuadrada.
	 * @param adn Array con la información a validar
	 * @return Retorna un valor booleano dependiendo de la validación
	 * @author NDSC - 15-08-2021
	 */
	private boolean validarArrayNxN(String[] adn) 
	{
		Integer cantidadFilas = adn.length;
		
		for (String str : adn) 
		{
			if(str.length() != cantidadFilas)
				return false;
			
		}
		return true;
	}
	
	/**
     * Método para validar las letras permitidas en el array.
     * @param adn Array con la información a trabajar.
     * @return Retorna un valor booleano dependiendo si cumple o no cumple.
     * @author DavidSegura - 05-08-2021.
     */
    private boolean validarCaracteresPermitidos(String[] adn)
    {
        for (String str : adn)
        {
            if(!(str.matches("[AaTtCcGg]+")))
                return false;
        }
        return true;
    }
    
    /**
     * Método para guardar o actualizar un humano mutante.
     * @param adn Parámetro ADN del humano para actualziar o crear.
     * @author NDSC - 16-08-2021. 
     */
    private void guardarVerticalYOblicuos(String adn)
    {
    	Humano humano = buscarHumano(adn);
    	if(humano != null)
    	{
    		modificarHumano(adn);
    	}
    	else
    	{
    		humano = new Humano(); 
    		humano.setAdn(adn);
    		humano.setMutante(true);
    		humanoRepository.save(humano);
    	}
    }
    
    @Override
    public EstadisticaDTO estadisticas() 
	{
		List<Humano> humanos = (List<Humano>) humanoRepository.findAll();
		EstadisticaDTO estadistica = new EstadisticaDTO();
		Integer totalHumanos = humanos.size();
		Integer cantidadMutantes = humanos.stream().filter(h -> h.getMutante().equals(true)).collect(Collectors.counting()).intValue();
		Float promedio = calcularPromedio(totalHumanos, cantidadMutantes);
		estadistica.setCantidadHumanos(totalHumanos);
		estadistica.setCantidadMutantes(cantidadMutantes);
		estadistica.setPromedio(promedio);
		return estadistica;
	}
	
	/**
	 * Método para calcular el promedio de un humanos versus mutantes.
	 * @param totalHumanos Parámetro  de consulta de la cantidad de registros en la base de datos.
	 * @param cantidadMutantes Parámetro de consulta de los mutantes que existen en base de datos.
	 * @return Retorna un el valor flotante con el promedio de la tabla.
	 * @author NDSC - 16-08-2021. 
	 */
    @Override
    public Float calcularPromedio(Integer totalHumanos, Integer cantidadMutantes)
	{
		if(totalHumanos != 0)
		{
			Float promedio = (float) cantidadMutantes/totalHumanos;
			return promedio;
		}
		else
		{
			throw new DivisionException();
		}
	}

	@Override
	public List<Humano> findAll() 
	{
		List<Humano> humanos = (List<Humano>) humanoRepository.findAll();
		return humanos;
	}

	@Override
	public Humano guardarHumano(Humano humano) 
	{
		return humanoRepository.save(humano);
	}

	@Override
	public Humano actualizarHumano(Humano humano) 
	{
		Humano humanoModificado = new Humano();
		humanoModificado.setMutante(humano.getMutante());
		humanoRepository.save(humanoModificado);
		return humanoModificado;
	}
}