package org.tds.sgh.test.max;

import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tds.sgh.dtos.ClienteDTO;
import org.tds.sgh.dtos.HabitacionDTO;
import org.tds.sgh.dtos.HotelDTO;
import org.tds.sgh.dtos.HuespedDTO;
import org.tds.sgh.dtos.ReservaDTO;
import org.tds.sgh.dtos.TipoHabitacionDTO;
import org.tds.sgh.system.ICadenaController;
import org.tds.sgh.system.IHacerReservaController;
import org.tds.sgh.system.ITomarReservaController;
import org.tds.sgh.test.TestBase;
import org.tds.sgh.test.stubs.SistemaMensajeriaStub.Mail;

@RunWith(JUnit4.class)
public class TomarReservaTest extends TestBase
{
	// curso alternativo con modificar reserva ................................
	
	@Test
	public void TomarReservaCursoAlternativoConModificarReserva() throws Exception
	{
		ClienteDTO cliente1 = generarClienteConNombre("Fulanito");
		
		HotelDTO hotel1 = generarHotel();
		
		TipoHabitacionDTO tipoHabitacion1 = generarTipoHabitacion();
		
		HabitacionDTO habitacion1 = generarHabitacion(hotel1, tipoHabitacion1);
		
		HuespedDTO huesped1 = generarHuesped();
		HuespedDTO huesped2 = generarHuesped();
				
		GregorianCalendar fecha1 = calendario.getHoy();
		GregorianCalendar fecha2 = generarFecha(fecha1);
		GregorianCalendar fecha3 = generarFecha(fecha1);

	
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarCliente(cliente1);
		
		cadenaController.agregarHotel(hotel1);
		
		cadenaController.agregarTipoHabitacion(tipoHabitacion1);
		
		cadenaController.agregarHabitacion(habitacion1);

		
		IHacerReservaController hacerReservaController = controllerFactory.createHacerReservaController();
		
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente(".*lani.*");
		
		assertTrue("El cliente devuelto debe ser el cliente1.",
				   clientes != null && clientes.size() == 1 && cliente1.equals(clientes.get(0)));
		
		ClienteDTO clienteX = hacerReservaController.seleccionarCliente(clientes.get(0));
		
		assertTrue("El cliente seleccionado debe ser el cliente1.",
				   cliente1.equals(clienteX));
		
		
		boolean disponibilidad = hacerReservaController.confirmarDisponibilidad(hotel1, tipoHabitacion1, fecha1, fecha2);
		
		assertTrue("El hotel debe tener disponibilidad ya que no tiene ninguna reserva que interfiera.",
				   disponibilidad);
		
		
		ReservaDTO reservaX = hacerReservaController.registrarReserva(hotel1, tipoHabitacion1, fecha1, fecha2, true);
		
		ReservaDTO reserva = new ReservaDTO(
			reservaX.getCodigo(),
			cliente1.getRut(),
			hotel1.getNombre(),
			tipoHabitacion1.getNombre(),
			fecha1,
			fecha2,
			true,
			"Pendiente",
			null
		);

		assertTrue("Los datos de la reserva no coinciden con los provistos al momento de registrar la reserva.",
				   reserva.equals(reservaX));

		
		
		ITomarReservaController tomarReservaController = controllerFactory.createTomarReservaController();
		
		List<ReservaDTO> reservas = tomarReservaController.buscarReservasPendientes(hotel1);
		
		assertTrue("La �nica reserva pendiente del hotel debe ser la reserva registrada.",
				   reservas != null && reservas.size() == 1 && reserva.equals(reservas.get(0)));
		
		
		ReservaDTO reservaY1 = tomarReservaController.seleccionarReserva(reservas.get(0));
		
		assertTrue("La reserva seleccionada debe ser la reserva registrada.",
				   reserva.equals(reservaY1));
		
		disponibilidad = tomarReservaController.confirmarDisponibilidad(hotel1, tipoHabitacion1, fecha1, fecha3);
		
		assertTrue("Debe haber disponibilidad ya que hay una sola habitaci�n y una sola reserva (la que est� siendo modificada).",
				   disponibilidad);
		
		ReservaDTO reservaY = tomarReservaController.modificarReserva(hotel1, tipoHabitacion1, fecha1, fecha3, true);
		
		ReservaDTO reserva2 = new ReservaDTO(
			reservaX.getCodigo(),
			cliente1.getRut(),
			hotel1.getNombre(),
			tipoHabitacion1.getNombre(),
			fecha1,
			fecha3,
			true,
			"Pendiente",
			null
		);
		
		assertTrue("La reserva no coincide con la esperada.",
				   reserva2.equals(reservaY));
		
		
		ReservaDTO reservaZ1 = tomarReservaController.registrarHuesped(huesped1);

		ReservaDTO reserva3 = new ReservaDTO(
			reservaX.getCodigo(),
			cliente1.getRut(),
			hotel1.getNombre(),
			tipoHabitacion1.getNombre(),
			fecha1,
			fecha3,
			true,
			"Pendiente",
			null,
			huesped1
		);
		
		assertTrue("La reserva debe registrar el hu�sped.",
				   reserva3.equals(reservaZ1));
		
		
		ReservaDTO reservaZ2 = tomarReservaController.registrarHuesped(huesped2);
		
		ReservaDTO reserva4 = new ReservaDTO(
			reservaX.getCodigo(),
			cliente1.getRut(),
			hotel1.getNombre(),
			tipoHabitacion1.getNombre(),
			fecha1,
			fecha3,
			true,
			"Pendiente",
			null,
			huesped1,
			huesped2
		);
		
		assertTrue("La reserva debe registrar el hu�sped.",
				   reserva4.equals(reservaZ2));
		
		
		ReservaDTO reservaZ3 = tomarReservaController.tomarReserva();
		
		ReservaDTO reserva5 = new ReservaDTO(
			reservaX.getCodigo(),
			cliente1.getRut(),
			hotel1.getNombre(),
			tipoHabitacion1.getNombre(),
			fecha1,
			fecha3,
			true,
			"Tomada",
			habitacion1.getNombre(),
			huesped1,
			huesped2
		);
		
		assertTrue("Los datos de la reserva tomada no coinciden con los esperados.",
				   reserva5.equals(reservaZ3));
			
		
		List<Mail> mails = sistemaMensajeria.getMailEnviados();
		
		assertTrue("El sistema debe mandar mails al cliente confirmando la reserva (al registrar, al modificar y al tomar la reserva).",
				   mails.size() == 3);
		
		assertTrue("El destinatario de los mails debe ser el cliente de la reserva.",
				   mails.stream().allMatch(mail -> mail.getDestinatario().equals(cliente1.getMail())));

		
		assertTrue("El sistema de facturaci�n no recibi� la notificaci�n de que se tom� una reserva.",
				   !sistemaFacturacion.getReservas().isEmpty());
		
		assertTrue("El sistema de facturaci�n recibi� m�s de una notificaci�n de que se tom� una reserva.",
				   sistemaFacturacion.getReservas().size() == 1);
		
		assertTrue("La reserva que recibi� el sistema de facturaci�n no coincide con la reserva tomada.",
				   reserva5.equals(sistemaFacturacion.getReservas().get(0)));
	}
	
	
	// curso alternativo con modificar reserva con reserva no modificable .....
	
	@Test(expected=Exception.class)
	public void TomarReservaCursoAlternativoConModificarReservaDeReservaNoModificable() throws Exception
	{
		ClienteDTO cliente1 = generarClienteConNombre("Fulanito");
		
		HotelDTO hotel1 = generarHotel();
		
		TipoHabitacionDTO tipoHabitacion1 = generarTipoHabitacion();
		
		HabitacionDTO habitacion1 = generarHabitacion(hotel1, tipoHabitacion1);
		
		GregorianCalendar fecha1 = calendario.getHoy();
		GregorianCalendar fecha2 = generarFecha(fecha1);
		GregorianCalendar fecha3 = generarFecha(fecha1);

	
		ICadenaController cadenaController = controllerFactory.createCadenaController();
		
		cadenaController.agregarCliente(cliente1);
		
		cadenaController.agregarHotel(hotel1);
		
		cadenaController.agregarTipoHabitacion(tipoHabitacion1);
		
		cadenaController.agregarHabitacion(habitacion1);

		
		IHacerReservaController hacerReservaController = controllerFactory.createHacerReservaController();
		
		
		List<ClienteDTO> clientes = hacerReservaController.buscarCliente(".*lani.*");
		
		assertTrue("El cliente devuelto debe ser el cliente1.",
				   clientes != null && clientes.size() == 1 && cliente1.equals(clientes.get(0)));
		
		ClienteDTO clienteX = hacerReservaController.seleccionarCliente(clientes.get(0));
		
		assertTrue("El cliente seleccionado debe ser el cliente1.",
				   cliente1.equals(clienteX));
		
		
		boolean disponibilidad = hacerReservaController.confirmarDisponibilidad(hotel1, tipoHabitacion1, fecha1, fecha2);
		
		assertTrue("El hotel debe tener disponibilidad ya que no tiene ninguna reserva que interfiera.",
				   disponibilidad);
		
		
		// reserva no modificable por hu�sped
		ReservaDTO reservaX = hacerReservaController.registrarReserva(hotel1, tipoHabitacion1, fecha1, fecha2, false);
		
		ReservaDTO reserva = new ReservaDTO(
			reservaX.getCodigo(),
			cliente1.getRut(),
			hotel1.getNombre(),
			tipoHabitacion1.getNombre(),
			fecha1,
			fecha2,
			false,
			"Pendiente",
			null
		);

		assertTrue("Los datos de la reserva no coinciden con los provistos al momento de registrar la reserva.",
				   reserva.equals(reservaX));

		
		
		ITomarReservaController tomarReservaController = controllerFactory.createTomarReservaController();
		
		List<ReservaDTO> reservas = tomarReservaController.buscarReservasPendientes(hotel1);
		
		assertTrue("La �nica reserva pendiente del hotel debe ser la reserva registrada.",
				   reservas != null && reservas.size() == 1 && reserva.equals(reservas.get(0)));
		
		
		ReservaDTO reservaY1 = tomarReservaController.seleccionarReserva(reservas.get(0));
		
		assertTrue("La reserva seleccionada debe ser la reserva registrada.",
				   reserva.equals(reservaY1));
		
		
		disponibilidad = tomarReservaController.confirmarDisponibilidad(hotel1, tipoHabitacion1, fecha1, fecha3);
		
		assertTrue("Debe haber disponibilidad ya que hay una sola habitaci�n y una sola reserva (la que est� siendo modificada).",
				   disponibilidad);
		
		// debe fallar ya que se intenta modificar una reserva que no es modificable por el hu�sped
		tomarReservaController.modificarReserva(hotel1, tipoHabitacion1, fecha1, fecha3, true);
	}
}
