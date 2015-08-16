package org.tds.sgh.system;

import java.util.GregorianCalendar;
import java.util.List;

import org.tds.sgh.business.CadenaHotelera;
import org.tds.sgh.business.Cliente;
import org.tds.sgh.business.EstadoReserva;
import org.tds.sgh.business.Reserva;
import org.tds.sgh.dtos.ClienteDTO;
import org.tds.sgh.dtos.HotelDTO;
import org.tds.sgh.dtos.ReservaDTO;
import org.tds.sgh.dtos.DTO;
import org.tds.sgh.infrastructure.*;

public class Controller implements IHacerReservaController, IModificarReservaController, ITomarReservaController, ICancelarReservaController{
	private CadenaHotelera cadenaHotelera;
	private final DTO DTO = org.tds.sgh.dtos.DTO.getInstance();
	private Cliente cliente;
	private Reserva reserva;

	// Constructors (public) ----------------------------------------------------------------------
	
	public Controller(CadenaHotelera cadenaHotelera)
	{
		this.cadenaHotelera = cadenaHotelera;
	}
	
	@Override
	public List<ClienteDTO> buscarCliente(String patronNombreCliente) {
		 return DTO.mapClientes(cadenaHotelera.buscarClientes(patronNombreCliente));
	}


	@Override
	public ClienteDTO seleccionarCliente(String rut) throws Exception {
		 this.cliente = cadenaHotelera.buscarCliente(rut);
		 return DTO.map(this.cliente);
	}


	@Override
	public boolean confirmarDisponibilidad(String nombreHotel,
			String nombreTipoHabitacion, GregorianCalendar fechaInicio,
			GregorianCalendar fechaFin) throws Exception {
		return cadenaHotelera.confirmarDisponibilidad(nombreHotel, nombreTipoHabitacion, fechaInicio, fechaFin);
	}


	@Override
	public List<HotelDTO> sugerirAlternativas(String pais,
			String nombreTipoHabitacion, GregorianCalendar fechaInicio,
			GregorianCalendar fechaFin) throws Exception {
		return DTO.mapHoteles(this.cadenaHotelera.obtenerAlternativas(pais,nombreTipoHabitacion, fechaInicio, fechaFin));
	}


	@Override
	public ReservaDTO registrarReserva(String nombreHotel,
			String nombreTipoHabitacion, GregorianCalendar fechaInicio,
			GregorianCalendar fechaFin, boolean modificablePorHuesped)
			throws Exception {
		    Reserva reserva = this.cadenaHotelera.registrarReserva(this.cliente, nombreHotel, nombreTipoHabitacion, fechaInicio, fechaFin, modificablePorHuesped);
		    confirmarReserva(reserva);
		    this.reserva = reserva;
		    return DTO.map(reserva);
	}

    public void confirmarReserva(Reserva reserva) {
    	Infrastructure.getInstance().getSistemaMensajeria().enviarMail(reserva.getCliente().getMail(), "Reserva confirmada", "Su reserva ha sido confirmada.");
    }
    
    public void cancelarReserva(String mailCliente) {
    	Infrastructure.getInstance().getSistemaMensajeria().enviarMail(mailCliente, "Reserva cancelada", "Su reserva ha sido cancelada.");     	
    }

    
    public void modificarReserva(String mailCliente) {
    	Infrastructure.getInstance().getSistemaMensajeria().enviarMail(mailCliente, "Reserva modificada", "Su reserva ha sido modificada.");     	
    }

    public void iniciarEstadia(ReservaDTO reserva) {
    	Infrastructure.getInstance().getSistemaFacturacion().iniciarEstadia(reserva);
    }
	
	@Override
	public ClienteDTO registrarCliente(String rut, String nombre,
			String direccion, String telefono, String mail) throws Exception {
		this.cliente = cadenaHotelera.registrarCliente(rut, nombre, direccion, telefono, mail);
		return DTO.map(this.cliente);
	}

	@Override
	public List<ReservaDTO> buscarReservasDelCliente() throws Exception {
		return cadenaHotelera.buscarReservasDelCliente(this.cliente.getRut());
	}
	
	@Override
	public List<ReservaDTO> buscarReservasPendientes(String nombreHotel) throws Exception {
		
		return cadenaHotelera.buscarReservasPendientes(nombreHotel);
	}


	@Override
	public ReservaDTO seleccionarReserva(long codigoReserva) throws Exception {
		if (this.cliente == null){
			this.reserva = cadenaHotelera.seleccionarReserva(codigoReserva);
		}else{
			this.reserva = cadenaHotelera.seleccionarReservaCliente(codigoReserva, this.cliente.getRut());
		}
		return DTO.map(reserva);
	}


	@Override
	public ReservaDTO registrarHuesped(String nombre, String documento)
			throws Exception {
		this.reserva.registrarHuesped(nombre, documento);
		return DTO.map(reserva); 
	}


	@Override
	public ReservaDTO tomarReserva() throws Exception { 
		this.reserva = cadenaHotelera.tomarReserva(this.reserva);
		confirmarReserva(reserva);
		iniciarEstadia(DTO.map(reserva));
		return DTO.map(reserva);
	}

	@Override
	public ReservaDTO cancelarReservaDelCliente() throws Exception {
		  this.reserva = cadenaHotelera.cancelarReservaDelCliente(this.reserva);
		  cancelarReserva(reserva.getCliente().getMail());
		  return DTO.map(this.reserva);
	}	
	
	@Override
	public ReservaDTO modificarReserva(String nombreHotel,
			String nombreTipoHabitacion, GregorianCalendar fechaInicio,
			GregorianCalendar fechaFin, boolean modificablePorHuesped) throws Exception {
		  this.reserva = cadenaHotelera.modificarReserva(this.reserva, nombreHotel,nombreTipoHabitacion,fechaInicio,fechaFin,modificablePorHuesped);
		  modificarReserva(reserva.getCliente().getMail());
		  return DTO.map(this.reserva);
	}
}
