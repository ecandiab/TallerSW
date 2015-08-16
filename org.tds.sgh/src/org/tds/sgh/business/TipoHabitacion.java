package org.tds.sgh.business;
import org.tds.sgh.dtos.TipoHabitacionDTO;

public class TipoHabitacion
{
	// Attributes (private) -----------------------------------------------------------------------
	
	private String nombre;
	
	
	// Constructors (public) ----------------------------------------------------------------------
	
	public TipoHabitacion(String nombre)
	{
		this.nombre = nombre;
	}
	
	
	// Properties (public) ------------------------------------------------------------------------
	
	public String getNombre()
	{
		return nombre;
	}
	
	public void setNombre(String Nombre)
	{
		this.nombre = Nombre;
	}
	
	public TipoHabitacionDTO TipoHabitacionDto()
	{
		return new TipoHabitacionDTO(nombre);
	}
}