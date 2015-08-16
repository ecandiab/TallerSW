package org.tds.sgh.business;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.tds.sgh.dtos.DTO;
import org.tds.sgh.dtos.ReservaDTO;
import org.tds.sgh.infrastructure.Infrastructure;
import org.tds.sgh.business.EstadoReserva;

public class Hotel
{
	// Attributes (private) -----------------------------------------------------------------------
	
	private String nombre;
	
	private String pais;
	
	private Map<String,Habitacion> habitaciones;
	
	private Map<Long,Reserva> reservas;
	
	private Reserva reserva;
	
	
	// Constructors (public) ----------------------------------------------------------------------
	
	public Hotel(String nombre, String pais)
	{
		this.nombre = nombre;
		
		this.pais = pais;
		
		this.habitaciones = new HashMap<String,Habitacion>();
		
		this.reservas	  = new HashMap<Long,Reserva>();
	}
	
	
	// Properties (public) ------------------------------------------------------------------------
	
	public String getNombre()
	{
		return nombre;
	}
	
	public String getPais()
	{
		return pais;
	}
	
	
	// Operations (public) ------------------------------------------------------------------------
	
	public Habitacion agregarHabitacion(TipoHabitacion tipoHabitacion, String nombre) throws Exception
	{
		if (habitaciones.containsKey(nombre)) throw new Exception("El hotel ya tiene una habitación con el nombre indicado.");
		
		Habitacion habitacion = new Habitacion(tipoHabitacion, nombre);
		
		habitaciones.put(habitacion.getNombre(), habitacion);
		
		return habitacion;
	}
	
	public Stream<Habitacion> listarHabitaciones()
	{
		return habitaciones.values().stream();
	}
	
	
	
	public boolean confirmarDisponibilidadxPais(String pais, TipoHabitacion th, GregorianCalendar fi, GregorianCalendar ff) throws Exception{		
		int contHabitaciones = 0;
		int contReservas = 0;
		
		for(Habitacion habitacion : this.habitaciones.values())
		{
			if(habitacion.esDeTipo(th)) 
			{
				contHabitaciones++;
			}
		}
			
		
		for(Reserva reserva : this.reservas.values())
		{
			if(reserva.coincideReserva(th.TipoHabitacionDto(),fi,ff) && 
			  (pais == reserva.getHotel().getPais())) 	
			{
					contReservas++;
			}
		}
		
		return (contHabitaciones > contReservas);
	}
	
	public boolean confirmarDisponibilidad(TipoHabitacion th, GregorianCalendar fi, GregorianCalendar ff){		
		int contHabitaciones = 0;
		int contReservas = 0;
		
		for(Habitacion habitacion : this.habitaciones.values())
		{
			if(habitacion.esDeTipo(th)) 
			{
				contHabitaciones++;
			}
		}
			
		for(Reserva reserva : this.reservas.values())
		{
			if(reserva.coincideReserva(th.TipoHabitacionDto(),fi,ff)) 	
			{
				contReservas++;
			}
		}
		
		return (contHabitaciones > contReservas);
	}	
	
	public Reserva seleccionarReserva(long codigo){
		return reservas.get(codigo);
	}
	
	public  Reserva registrarReserva(Cliente cliente, TipoHabitacion tipoHabitacion, GregorianCalendar fechainicio, GregorianCalendar fechafin, boolean modificable){
		Reserva reserva = new Reserva(EstadoReserva.Pendiente,fechafin,fechainicio, modificable,tipoHabitacion,this, cliente);
		reservas.put(reserva.getCodigo(), reserva);
		return reserva;
	}
			
	
	
	public Reserva obtenerReserva(long codigo){
		return this.reservas.get(new Long(codigo));
	}
	
	public Reserva tomarReserva(Reserva reserva){
		
		ArrayList<Habitacion> habitacionesCandidatas = new ArrayList<Habitacion>();
		for(Habitacion habitacion : this.habitaciones.values()){
			if (reserva.getTipoHabitacion() == habitacion.getTipoHabitacion()){
				habitacionesCandidatas.add(habitacion);
			}
		}
		
		
		for(Reserva r : this.reservas.values()){
			if (reserva.coincideReserva(DTO.getInstance().map(r.getTipoHabitacion()), r.getFechaInicio(), r.getFechaFin())){
				if (r.getHabitacion() != null){
					habitacionesCandidatas.remove(r.getHabitacion());
				}
			}
		}
		reserva.tomar(habitacionesCandidatas.get(0));
		return reserva;
	}
	
	public Reserva cancelarReserva(Reserva reserva){
		reserva.cancelar();
		return reserva;
	}
	
	public List<Reserva> buscarReservasDelCliente(String rutCliente)
	{
		List<Reserva> listaReservaCliente = new ArrayList<Reserva>();
		for(Reserva r: reservas.values()){
			if (r.getCliente().getRut() == rutCliente 
					&& !Infrastructure.getInstance().getCalendario().esPasada(r.getFechaFin()) 
					&& r.getEstado() != EstadoReserva.Tomada){
				listaReservaCliente.add(r);
			}
		}
		return listaReservaCliente;
	}

	public List<Reserva> buscarReservasPendienes()
	{
		List<Reserva> listaReservaCliente = new ArrayList<Reserva>();
		for(Reserva r: reservas.values()){
			if (r.getEstado() == EstadoReserva.Pendiente){
				listaReservaCliente.add(r);
			}
		}
		return listaReservaCliente;
	}	

}
