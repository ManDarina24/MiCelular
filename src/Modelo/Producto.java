package Modelo;

public class Producto {
    protected int id;
    protected String marca;
    protected String modelo;
    protected String pantalla;
    protected String almacenamiento;
    protected String camara;
    protected String ram;
    protected int cantidad;
    protected double precio;

    public Producto(int id, int cantidad, double precio) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Producto(int id, String marca, String modelo, String pantalla, String almacenamiento, String camara, String ram, int stock, double precio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.pantalla = pantalla;
        this.almacenamiento = almacenamiento;
        this.camara = camara;
        this.ram = ram;
        this.cantidad = stock;
        this.precio = precio;
    }
    
    public int getId(){
        return id;
    }
    
    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPantalla() {
        return pantalla;
    }

    public String getAlmacenamiento() {
        return almacenamiento;
    }

    public String getCamara() {
        return camara;
    }

    public String getRam() {
        return ram;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }
    
    public void setCantidad(int cant){
        this.cantidad = cant;
    }
    
}
