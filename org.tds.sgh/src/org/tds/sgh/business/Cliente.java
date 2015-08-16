package org.tds.sgh.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tds.sgh.dtos.DTO;
import org.tds.sgh.dtos.ReservaDTO;

public class Cliente
{
	// Attributes (private) -----------------------------------------------------------------------
	
	private String rut;
	
	private String nombre;
	
	private String direccion;
	
	private String telefono;
	
	private String mail;
	
	private Map<Long,Reserva> reservas;
	
	
	// Constructors (public) ----------------------------------------------------------------------
	
	public Cliente(){
		this.rut = null;
		this.nombre = null;
		this.direccion = null;
		this.telefono = null;
		this.mail = null;		
	}
	
	public Cliente(String rut, String nombre, String direccion, String telefono, String mail)
	{
		this.rut = rut;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.mail = mail;
	}
	
	
	// Properties (public) ------------------------------------------------------------------------
	
	public String getRut()
	{
		return rut;
	}

	public String getNombre()
	{
		return nombre;
	}
	
	public String getDireccion()
	{
		return direccion;
	}
	
	public String getTelefono()
	{
		return telefono;
	}
	
	public String getMail()
	{
		return mail;
	}
}
