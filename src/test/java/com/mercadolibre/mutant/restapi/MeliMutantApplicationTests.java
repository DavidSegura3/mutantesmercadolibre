package com.mercadolibre.mutant.restapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.mercadolibre.mutant.restapi.controllers.HumanoController;
import com.mercadolibre.mutant.restapi.entities.Humano;
import com.mercadolibre.mutant.restapi.exception.DivisionException;
import com.mercadolibre.mutant.restapi.repositories.HumanoRepository;
import com.mercadolibre.mutant.restapi.services.HumanoServiceImpl;

@SpringBootTest
class MeliMutantApplicationTests 
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	private WebApplicationContext appContext;
	@Autowired
	private HumanoController humanoController;
	@Autowired
	private HumanoRepository humanoRepo;
	
	
	@Mock
	private HumanoRepository humanoRepository;
	
	@InjectMocks
	private HumanoServiceImpl humanoService; 
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() 
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.appContext).build();
	}
	
	@Test
	@DisplayName("Testeando que el controlador tenga por lo menos un método")
	void contextLoads() 
	{
		assertThat(humanoController).isNotNull();
	}
	
	@Test
	@DisplayName("Testeando el guardado de un humano para posteriormente buscarlo y actualizarlo.")
	void testGuardrHumano()
	{
		Humano humano = new Humano();
		humano.setAdn("TTTT");
		humano.setMutante(true);
		
		when(humanoRepository.save(ArgumentMatchers.any(Humano.class))).thenReturn(humano);
		
		Humano humanoCreado = humanoService.guardarHumano(humano);
		
		assertThat(humanoCreado.getAdn()).isSameAs(humano.getAdn());
		verify(humanoRepository).save(humano);
	}
	
	@Test
	@Rollback(false)
	@DisplayName("Testeando la actualización de un humano y cambiandolo  a mutante.")
	void testActualizarHumano() 
	{
	    Humano humano = humanoRepo.findByAdn("TTTT");
		humano.setMutante(true);
		humanoRepo.save(humano);
		
		Humano humanoActualizado = humanoRepo.findByAdn("TTTT");
		assertThat(humanoActualizado.getMutante()).isEqualTo(true);
	}
	
	@Test
	@DisplayName("Testeando que exista por lo menos un valor en BD para poder sacar las estadisticas.")
	void testListaHumanos() 
	{
	    List<Humano> humanos = (List<Humano>) humanoRepo.findAll();
	    assertThat(humanos).size().isGreaterThan(0);
	}
	
	/*@Test
	@DisplayName("Testeando que por lo menos exista una registro en la tabla.")
	void dividePorCero() 
	{
		assertEquals(2, humanoService.calcularPromedio(5, 10), 0);
	}*/
	
	
	@Test
	@DisplayName("Testeando que por lo menos exista una registro en la tabla.")
	void dividePorCero() 
	{
		expectedException.expect(DivisionException.class);
		expectedException.expectMessage("No se puede dividir por cero");

		humanoService.calcularPromedio(10, 5);
	}
	
	@Test
	@DisplayName("Testeando caracteres validos de base nitrogenada del ADN.")
	void testCaracteresValidos() 
	{
		Boolean test = true;
		String [] adn = {"sCTGCGA", "CCGTAA", "TTCAGT", "AGACGG", "CCCCTA", "TCACTG"};
		
		for (String str : adn)
        {
            if(!(str.matches("[AaTtCcGg]+")))
                test = false;
        }
		assertFalse(test);
	}
	
	@Test
	@DisplayName("Testeando que el el tamano del array sea NxN.")
	void testArrayNxN() 
	{
		String [] adn = {"sCTGCGA", "CCGTAA", "TTCAGT", "AGACGG", "CCCCTA", "TCACTG"};
		Boolean test = true;
		Integer cantidadFilas = adn.length;
		
		for (String str : adn) 
		{
			if(str.length() != cantidadFilas)
				test = false;
		}
		assertFalse(test);
	}
}