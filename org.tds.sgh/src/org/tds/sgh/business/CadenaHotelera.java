package org.tds.sgh.business;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.tds.sgh.dtos.DTO;
import org.tds.sgh.dtos.HotelDTO;
import org.tds.sgh.dtos.ReservaDTO;
import org.tds.sgh.infrastructure.Infrastructure;


public class CadenaHotelera
{
	// Attributes (private) -----------------------------------------------------------------------
	
	private String nombre;

	private Map<String,Cliente> clientes;
	
	private Map<String,Hotel> hoteles;
	
	private Map<String,TipoHabitacion> tiposHabitacion;
	
	private Hotel hotel;

	// Constructors (public) ----------------------------------------------------------------------

	public CadenaHotelera(String nombre)
	{
		this.nombre = nombre;
		
		this.clientes = new HashMap<String,Cliente>();
		
		this.hoteles = new HashMap<String,Hotel>();
		
		this.tiposHabitacion = new HashMap<String,TipoHabitacion>();
	}
	
	
	// Properties (public) ------------------------------------------------------------------------

	public String getNombre()
	{
		return nombre;
	}

	
	// Operations (public) ------------------------------------------------------------------------
	
	public Cliente agregarCliente(String rut, String nombre, String direccion, String telefono, String mail) throws Exception
	{
		if (clientes.containsKey(rut)) throw new Exception("Ya existe un cliente con el RUT indicado.");
		
		Cliente cliente = new Cliente(rut, nombre, direccion, telefono, mail);
		
		clientes.put(cliente.getRut(), cliente);
		
		return cliente;
	}

	public Cliente buscarCliente(String rut) throws Exception
	{
		Cliente cliente = clientes.get(rut);
		
		return cliente;
	}

	public Stream<Cliente> buscarClientes(String patronNombreCliente) 
	{
		List<Cliente> clientesEncontrados = new ArrayList<Cliente>();
		
		for(Cliente cliente : this.clientes.values()){
			if(cliente.getNombre().matches(patronNombreCliente)){
				clientesEncontrados.add(cliente);
			}
		} 
		
		return clientesEncontrados.stream();
		
	}

	public Stream<Cliente> listarClientes()
	{
		return clientes.values().stream();
	}

	public Hotel agregarHotel(String nombre, String pais) throws Exception
	{
		if (hoteles.containsKey(nombre)) throw new Exception("Ya existe un hotel con el nombre indicado.");
		
		Hotel hotel = new Hotel(nombre, pais);
		
		hoteles.put(hotel.getNombre(), hotel);
		
		return hotel;
	}

	public Hotel buscarHotel(String nombre)
	{
		for(Hotel h: hoteles.values()){
			if (nombre == h.getNombre()){
				return h;
			}
		}
		return null;
	}
	
	public Hotel buscarPais(String pais)
	{
		for(Hotel h: hoteles.values()){
			if (nombre == h.getPais()){
				return h;
			}
		}
		return null;
	}
	
	public Stream<Hotel> listarHoteles()
	{
		return hoteles.values().stream();
	}

	public TipoHabitacion agregarTipoHabitacion(String nombre) throws Exception
	{
		if (tiposHabitacion.containsKey(nombre)) throw new Exception("Ya existe un tipo de habitación con el nombre indicado.");
		
		TipoHabitacion tipoHabitacion = new TipoHabitacion(nombre);
		
		tiposHabitacion.put(tipoHabitacion.getNombre(), tipoHabitacion);
		
		return tipoHabitacion;
	}

	public TipoHabitacion buscarTipoHabitacion(String nombre) throws Exception
	{
		TipoHabitacion tipoHabitacion = tiposHabitacion.get(nombre);
		
		if (tipoHabitacion == null) throw new Exception("No existe un tipo de habitación con el nombre indicado.");
		
		return tipoHabitacion;
	}
	
	public Stream<TipoHabitacion> listarTiposHabitacion()
	{
		return tiposHabitacion.values().stream();
	}

	public boolean confirmarDisponibilidad(String nombreHotel, String nombreTipoHabitacion, GregorianCalendar fechaInicio, GregorianCalendar fechaFin)throws Exception {

		TipoHabitacion tipoHabitacion = this.tiposHabitacion.get(nombreTipoHabitacion);
		Hotel hh = hoteles.get(nombreHotel);
		if (hh == null) throw new Exception("Hotel Inexistente.");
		if (tipoHabitacion == null) throw new Exception("Tipo Habitacion Inexistente.");
		if (Infrastructure.getInstance().getCalendario().esPasada(fechaInicio)) throw new Exception(" No confirma Si Fecha inicio es del Pasado.");
		if (Infrastructure.getInstance().getCalendario().esPosterior(fechaInicio, fechaFin)) throw new Exception(" No confirma Si Fecha inicio es Posterior a Fecha Fin.");
		for(Hotel h: hoteles.values()){
			if (nombreHotel == h.getNombre()){
					if (h.confirmarDisponibilidadxPais(h.getPais(),tipoHabitacion, fechaInicio, fechaFin)){
						return true;
					}
			}
		}
		return false; 
	}
	
	public Cliente registrarCliente(String rut, String nombre, String direccion, String telefono, String mail) throws Exception 
	{
		Cliente c = buscarCliente(rut);
		if (c != null) throw new Exception("Cliente ya existe.");
			
		Cliente cliente = new Cliente(rut, nombre, direccion, telefono, mail);
		clientes.put(cliente.getRut(), cliente);
		return cliente;
	}
	
	public Reserva registrarReserva(Cliente cliente, String nombreHotel, String nombreTipoHabitacion, GregorianCalendar fechaInicial, GregorianCalendar fechaFinal, boolean modificable)throws Exception {
		
		Hotel hotel = this.hoteles.get(nombreHotel);
		TipoHabitacion tipoHabitacion = this.tiposHabitacion.get(nombreTipoHabitacion);
		if (tipoHabitacion == null) throw new Exception("Tipo Habitacion Inexistente.");
		Reserva reserva = hotel.registrarReserva(cliente, tipoHabitacion, fechaInicial, fechaFinal, modificable);
		
		return reserva;
	}
	
	public Stream<Hotel> obtenerAlternativas(String pais, String tipHabitacion, GregorianCalendar fechaInicio, GregorianCalendar fechaFin) throws Exception {
		
		TipoHabitacion tipoHabitacion = this.tiposHabitacion.get(tipHabitacion);
		if (tipoHabitacion == null) throw new Exception("Tipo Habitacion Inexistente.");
		if (Infrastructure.getInstance().getCalendario().esPasada(fechaInicio)) throw new Exception("Fecha de inicio Pasada.");
		if (Infrastructure.getInstance().getCalendario().esPosterior(fechaInicio, fechaFin)) throw new Exception("Fecha de inicio Pasada Fecha Fin.");
		
		List<Hotel> hotelesDisponibles = new ArrayList<Hotel>();
		
		for(Hotel hotel : this.hoteles.values()){
			if(hotel.confirmarDisponibilidad(tipoHabitacion, fechaInicio, fechaFin)){
				if (hotel.getPais().equals(pais)) {
						hotelesDisponibles.add(hotel);
				}
			}
		}
		
		return hotelesDisponibles.stream();
	}
	
	
	public Reserva seleccionarReserva(long codigo) throws Exception  {
		for(Hotel h: hoteles.values()){
			Reserva r = h.seleccionarReserva(codigo);
			if (r != null){
				return r;
			}
		}
		return null;
	}
	
	
	public Reserva seleccionarReservaCliente(long codigo, String cliente) throws Exception  {
		for(Hotel h: hoteles.values()){
			Reserva r = h.seleccionarReserva(codigo);
			if (r != null){
				if (r.getEstado() != EstadoReserva.Pendiente) throw new Exception("No busca Reservas que no esten Pendiente.");
				if (cliente != null){
					if (r.getCliente().getRut() != cliente) throw new Exception("No busca Reservas que no esten Pendiente.");
				}
				return r;
			}
		}
		return null;
	}
	
	public Reserva tomarReserva(Reserva reserva){
		reserva.getHotel().tomarReserva(reserva);
		return reserva;
		
	}
	
	public List<ReservaDTO> buscarReservasDelCliente(String rutCliente){
			List<ReservaDTO> salida = new ArrayList<ReservaDTO>();
			for(Hotel h: hoteles.values()){
				List<Reserva> r = h.buscarReservasDelCliente(rutCliente);
				for(Reserva s: r){
					salida.add(DTO.getInstance().map(s));
				}
			}
			return salida; 
	}
	
	public List<ReservaDTO> buscarReservasPendientes (String nombreHotel){
		List<ReservaDTO> salida = new ArrayList<ReservaDTO>();
		for(Hotel h: hoteles.values()){
			if (h.getNombre() == nombreHotel){
				List<Reserva> r = h.buscarReservasPendienes();
				for(Reserva s: r){
					salida.add(DTO.getInstance().map(s));
				}
			}
		}
		return salida;
	}
	public Reserva cancelarReservaDelCliente(Reserva reserva){
		return reserva.getHotel().cancelarReserva(reserva);
	}
	
	public Reserva modificarReserva(Reserva reserva, String nombreHotel,
			String nombreTipoHabitacion, GregorianCalendar fechaInicio,
			GregorianCalendar fechaFin, boolean modificablePorHuesped)throws Exception{
		
		if (!reserva.getModificableporHuesped()) throw new Exception("Reserva no Modificable por Huesped.");
		if (confirmarDisponibilidad(nombreHotel, nombreTipoHabitacion, fechaInicio, fechaFin) && (reserva.getModificableporHuesped())) {
			Hotel h = buscarHotel(nombreHotel);
			reserva.modificarReserva(h, nombreTipoHabitacion, fechaInicio, fechaFin);
		}
		return reserva;
	} 
}
