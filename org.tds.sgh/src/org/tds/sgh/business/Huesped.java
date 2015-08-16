package org.tds.sgh.business;
import org.tds.sgh.dtos.*;

public class Huesped {

	private String documento;
	private String nombre;
	
	public Huesped(String nombre, String documento)
		{
			this.nombre = nombre;
			this.documento = documento;
		}
	
		public String getDocumento() {
			return documento;
		}
		
		public void setDocumento(String documento) {
			this.documento = documento;
		}
		
		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public HuespedDTO DTO()
		{
			return new HuespedDTO(getNombre(), getDocumento());
		}
}