package org.tds.sgh.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.tds.sgh.dtos.*;
import org.tds.sgh.infrastructure.Infrastructure;

public class Reserva{
// Definicion Atributos
	private long	 			codigo;
	private EstadoReserva		estadoReserva;
	private GregorianCalendar	fechaFin;
	private GregorianCalendar	fechaInicio;
	private boolean  			modificablePorHuesped;
	private TipoHabitacion 		tipoHabitacion;
	private Habitacion 		    habitacion;
	private Hotel               hotel;
	private Cliente				cliente;
	private Map<String,Huesped> huespedes;
	
	
	public Reserva(EstadoReserva estadoReserva, GregorianCalendar fechaFin,
			GregorianCalendar fechaInicio, Boolean modificablePorHuesped,TipoHabitacion tipoHabitacion,
			Hotel hotel, Cliente cliente){
		this.estadoReserva	=	estadoReserva;
		this.fechaFin		=	fechaFin;
		this.fechaInicio	=	fechaInicio;
		this.modificablePorHuesped	=	modificablePorHuesped;
		this.tipoHabitacion	=	tipoHabitacion;
		this.hotel			=	hotel;
		this.cliente		= 	cliente;
		this.habitacion     = null;
		this.huespedes		= new HashMap<String,Huesped>();
		this.codigo			= new Random().nextLong();
	}
	
	public boolean coincideReserva(TipoHabitacionDTO tipoHabitacion, GregorianCalendar fechaInicio, GregorianCalendar fechaFin){			
		return this.tipoHabitacion.getNombre().equals(tipoHabitacion.getNombre()) &&
				!(Infrastructure.getInstance().getCalendario().esPosterior(fechaInicio, this.fechaFin) ||
				Infrastructure.getInstance().getCalendario().esAnterior(fechaFin, this.fechaInicio) ||
				Infrastructure.getInstance().getCalendario().esMismoDia(fechaInicio, this.fechaFin));
				
	}
	
	public List<Huesped> getHuespedes(){
		return new ArrayList<Huesped>(this.huespedes.values());
	}
	
	public Hotel getHotel(){
		return  this.hotel;	
	}

	public Hotel setHotel(Hotel hotel){
		return  this.hotel = hotel;	
	}
	
	public void setHabitacion(TipoHabitacion tipoHabitacion, String nombre){
		this.habitacion = new Habitacion(tipoHabitacion, nombre);	
	}
	
	public HuespedDTO registrarHuesped(String nombre, String documento){
		Huesped huesped = new Huesped(nombre, documento);		
		this.huespedes.put(nombre, huesped);
		return huesped.DTO();
	}
	
	public Cliente getCliente(){
		return cliente;
	}

	public EstadoReserva getEstado() {
		return this.estadoReserva;
	}

	public void setEstado(EstadoReserva estado) {
		this.estadoReserva = estado;
	}

	public GregorianCalendar getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(GregorianCalendar fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public void tomar(Habitacion habitacion){
		this.estadoReserva = EstadoReserva.Tomada;
		this.habitacion = habitacion;
	}
	public void cancelar(){
		this.estadoReserva = EstadoReserva.Cancelada;
		this.habitacion = null;
	}	
	
	public void modificarReserva(Hotel hotel, String nombreTipoHabitacion, GregorianCalendar fechaInicio, GregorianCalendar fechaFin){
		this.setHotel(hotel);
		this.setFechaFin(fechaFin); 
		this.setFechaInicio(fechaInicio);
		this.setTipoHabitacion(nombreTipoHabitacion);
		//this.setHabitacion(this.tipoHabitacion, nombreTipoHabitacion);
	}
	

	public GregorianCalendar getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(GregorianCalendar fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public TipoHabitacion getTipoHabitacion() {
		return this.tipoHabitacion;
	}
	
	public Habitacion getHabitacion() {
		return this.habitacion;
	}

	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = new TipoHabitacion(tipoHabitacion);
	}

	public Boolean getModificableporHuesped() {
		return modificablePorHuesped;
	}

	public void setModificablePorHuesped(Boolean modificablePorHuesped) {
		this.modificablePorHuesped = modificablePorHuesped;
	}

	
	public long getCodigo(){
		return this.codigo;
	}
}
			
