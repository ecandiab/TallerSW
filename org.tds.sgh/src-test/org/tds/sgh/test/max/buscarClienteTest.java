package org.tds.sgh.test.max;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tds.sgh.dtos.ClienteDTO;
import org.tds.sgh.system.ICadenaController;
import org.tds.sgh.system.IIdentificarClienteEnRecepcionController;
import org.tds.sgh.test.TestBase;

@RunWith(JUnit4.class)
public class buscarClienteTest extends TestBase
{
	@Test
	public void SistemaNoBuscaClientesConPatronNull() throws Exception
	{
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		hacerReservaController.buscarCliente(null);
	}
	
	@Test
	public void BuscarClientesNoEsNull()
	{
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente("");
		
		assertTrue("El resultado de la b�squeda no debe ser null.",
				   clientes != null);
	}
	
	@Test
	public void BuscarClientesEsVacioCuandoNoHayClientesRegistrados()
	{
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente(".*");
		
		assertTrue("La b�squeda no debe devolver clientes ya que no hay clientes registrados.",
				   clientes.isEmpty());
	}
	
	@Test
	public void BuscarClientesEsVacioCuandoClientesRegistradosNoCoincidenConElPatron() throws Exception
	{
		ClienteDTO cliente1 = generarClienteConNombre("Alicia");
		ClienteDTO cliente2 = generarClienteConNombre("Martina");
		ClienteDTO cliente3 = generarClienteConNombre("Patricia");
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarCliente(cliente1);
		cadenaController.agregarCliente(cliente2);
		cadenaController.agregarCliente(cliente3);
		
		
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente("^Moriana$");
		
		assertTrue("La b�squeda no debe devolver clientes ya que los clientes registrados no coinciden con el patr�n.",
				   clientes.isEmpty());
	}
	
	@Test
	public void BuscarClientesDevuelveTodosLosClientesParaPatronUniversal() throws Exception
	{
		ClienteDTO cliente1 = generarClienteConNombre("Alicia");
		ClienteDTO cliente2 = generarClienteConNombre("Martina");
		ClienteDTO cliente3 = generarClienteConNombre("Patricia");
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarCliente(cliente1);
		cadenaController.agregarCliente(cliente2);
		cadenaController.agregarCliente(cliente3);
		
		
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente(".*");
		
		assertTrue("La b�squeda debe devolver todos los dos clientes registrados.",
				   clientes.size() == 3);
		
		ClienteDTO clienteX = clientes.get(0);
		ClienteDTO clienteY = clientes.get(1);
		ClienteDTO clienteZ = clientes.get(2);
		
		assertTrue("La b�squeda debe devolver el cliente1.",
				   cliente1.equals(clienteX) || cliente1.equals(clienteY) || cliente1.equals(clienteZ));

		assertTrue("La b�squeda debe devolver el cliente2.",
				   cliente2.equals(clienteX) || cliente2.equals(clienteY) || cliente2.equals(clienteZ));
		
		assertTrue("La b�squeda debe devolver el cliente3.",
				   cliente3.equals(clienteX) || cliente3.equals(clienteY) || cliente3.equals(clienteZ));
	}
	
	@Test
	public void BuscarClientesDevuelveSoloLosClientesQueCoincidenConElPatron() throws Exception
	{
		ClienteDTO cliente1 = generarClienteConNombre("Alicia");
		ClienteDTO cliente2 = generarClienteConNombre("Martina");
		ClienteDTO cliente3 = generarClienteConNombre("Patricia");
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarCliente(cliente1);
		cadenaController.agregarCliente(cliente2);
		cadenaController.agregarCliente(cliente3);
		
		
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente(".*ici.*");
		
		assertTrue("La b�squeda debe devolver los dos clientes registrados que coinciden con el patr�n.",
				   clientes.size() == 2);
		
		ClienteDTO clienteX = clientes.get(0);
		ClienteDTO clienteY = clientes.get(1);
		
		assertTrue("La b�squeda debe devolver el cliente1 ya que coincide con el patr�n.",
				   cliente1.equals(clienteX) || cliente1.equals(clienteY));

		assertTrue("La b�squeda debe devolver el cliente2 ya que coincide con el patr�n.",
				   cliente3.equals(clienteX) || cliente3.equals(clienteY));
	}
	
	@Test
	public void BuscarClientesDevuelveUnClienteCuandoElPatronEsExacto() throws Exception
	{
		ClienteDTO cliente1 = generarClienteConNombre("Alicia");
		ClienteDTO cliente2 = generarClienteConNombre("Martina");
		ClienteDTO cliente3 = generarClienteConNombre("Patricia");
		
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarCliente(cliente1);
		cadenaController.agregarCliente(cliente2);
		cadenaController.agregarCliente(cliente3);
		
		
		IIdentificarClienteEnRecepcionController hacerReservaController = controllerFactory.createHacerReservaController();
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente("^Martina$");
		
		assertTrue("La b�squeda debe devolver solamente un cliente.",
				   clientes.size() == 1);
		
		assertTrue("La b�squeda debe devolver al cliente2.",
				   clientes.get(0).equals(cliente2));
	}
}
