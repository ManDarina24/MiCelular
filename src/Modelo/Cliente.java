package Modelo;

/**
 *
 * @author darina
 */
public class Cliente {
    protected int id;
    protected String nombre;
    protected String apellidoPaterno;
    protected String apellidoMaterno;
    protected long telefono;
    protected String correo;
    
     public Cliente(String nombre, String apellidoPaterno, String apellidoMaterno, long telefono, String correo) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
    }

  
    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public long getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

 
    
    
}
