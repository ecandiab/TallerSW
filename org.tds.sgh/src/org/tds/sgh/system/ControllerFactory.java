package org.tds.sgh.system;

import org.tds.sgh.business.CadenaHotelera;
import org.tds.sgh.infrastructure.NotImplementedException;
import org.tds.sgh.system.Controller;

public class ControllerFactory implements IControllerFactory
{
	// Attributes (private) -----------------------------------------------------------------------
	
	private CadenaHotelera cadenaHotelera;
	
	
	// Constructors (public) ----------------------------------------------------------------------
	
	public ControllerFactory(CadenaHotelera cadenaHotelera)
	{
		this.cadenaHotelera = cadenaHotelera;
	}


	// Operations (public) ------------------------------------------------------------------------

	@Override
	public ICadenaController createCadenaController()
	{
		return new CadenaController(cadenaHotelera);
	}
	
	@Override
	public IHacerReservaController createHacerReservaController()
	{
		// TODO
		return new Controller(cadenaHotelera);
	}
	
	@Override
	public IModificarReservaController createModificarReservaController()
	{
		// TODO
		return new Controller(cadenaHotelera);
	}
	@Override
	public ICancelarReservaController createCancelarReservaController()
	{
		// TODO
		return new Controller(cadenaHotelera);
	}

	public ITomarReservaController createTomarReservaController()
	{
		// TODO
		return new Controller(cadenaHotelera);
	}
}
