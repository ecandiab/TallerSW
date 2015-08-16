package org.tds.sgh.test.db;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tds.sgh.dtos.HotelDTO;
import org.tds.sgh.system.ICadenaController;

@RunWith(JUnit4.class)
public class CadenaHotelesTest extends DbTestBase
{
	@Test
	public void HotelesNoEsNull()
	{
		ICadenaController cadenaController = controllerFactory.createCadenaController();
	
		
		assertTrue("El controlador devolvi� null. Debe devolver una lista de hoteles.",
				   cadenaController.getHoteles() != null);
	}
	
	@Test
	public void HotelesEsVacio()
	{
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		
		assertTrue("El controlador no devolvi� una lista de hoteles vac�a al inicio.",
				   cadenaController.getHoteles().isEmpty());
	}

	@Test
	public void CadenaRegistraHotel() throws Exception
	{
		HotelDTO hotel1 = generarHotel();
		
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarHotel(hotel1);
		
		
		List<HotelDTO> hoteles = cadenaController.getHoteles();
		
		assertTrue("El sistema no registr� al hotel",
				   !hoteles.isEmpty());
		
		assertTrue("El sistema registr� m�s de un hotel.",
				   hoteles.size() == 1);
		
		HotelDTO hotelX = hoteles.get(0);
		
		assertTrue("El sistema devolvi� null. Debe devolver los datos del hotel registrado.",
				   hotelX != null);
		
		assertTrue("El sistema reutiliz� instancias de test; el sistema debe preservar su propia informaci�n.",
				   hotelX != hotel1);
		
		assertTrue("El sistema registr� datos del hotel que no coinciden con los datos provistos al momento de registrar al hotel.",
				   hotelX.equals(hotel1));
	}
	
	@Test
	public void CadenaRegistraDosHoteles() throws Exception
	{
		HotelDTO hotel1 = generarHotel();
		
		HotelDTO hotel2 = generarHotel();
		
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarHotel(hotel1);
		
		cadenaController.agregarHotel(hotel2);
		
		
		List<HotelDTO> hoteles = cadenaController.getHoteles();
		
		assertTrue("El sistema no devolvi� los dos hoteles registrados.",
				   hoteles.size() == 2);
		
		HotelDTO hotelX = hoteles.get(0);
		
		HotelDTO hotelY = hoteles.get(1);
		
		assertTrue("El sistema devolvi� datos de hoteles que no coinciden con los registrados",
				   (hotelX.equals(hotel1) || hotelX.equals(hotel2)) &&
				   (hotelY.equals(hotel1) || hotelY.equals(hotel2)));
	}

	@Test(expected=Exception.class)
	public void CadenaNoRegistraDosHotelesConElMismoNombre() throws Exception
	{
		HotelDTO hotel1 = generarHotel();
		
		HotelDTO hotel2 = generarHotel(hotel1.getNombre());
		
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarHotel(hotel1);
		
		cadenaController.agregarHotel(hotel2);
	}
	
	@Test
	public void CadenaNoPierdeInformacionDeHoteles() throws Exception
	{
		HotelDTO hotel1 = generarHotel();
	
		
		ICadenaController cadenaController1 = controllerFactory.createCadenaController();
		
		assertTrue("El controlador no devolvi� una lista de hoteles vac�a al inicio.",
				   cadenaController1.getHoteles().isEmpty());
				
		cadenaController1.agregarHotel(hotel1);
		
		assertTrue("El sistema no registr� al hotel.",
				   !cadenaController1.getHoteles().isEmpty());
		
		assertTrue("El sistema registr� m�s de un hotel.",
				   cadenaController1.getHoteles().size() == 1);
		
		assertTrue("El sistema registr� datos del hotel que no coinciden con los datos provistos al momento de registrar al hotel.",
				   cadenaController1.getHoteles().get(0).equals(hotel1));
				

		ICadenaController cadenaController2 = controllerFactory.createCadenaController();

		assertTrue("El sistema no registr� al hotel. Usando un segundo controlador, �ste debe devolver el hotel registrado mediante el primer controlador.",
				   !cadenaController2.getHoteles().isEmpty());
		
		assertTrue("El sistema registr� m�s de un hotel. Usando un segundo controlador, �ste debe devolver el �nico hotel registrado mediante el primer controlador.",
				   cadenaController1.getHoteles().size() == 1);

		assertTrue("El sistema registr� datos del hotel que no coinciden con los datos provistos. Los datos del hotel que devuelve el segundo controlador deben ser los mismos que los registrados mediante el primer controlador.",
				   cadenaController2.getHoteles().get(0).equals(hotel1));
	}
}
