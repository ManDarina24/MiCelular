package Modelo;

public class Producto {
    private int id;
    private String marca;
    private String modelo;
    private String pantalla;
    private String almacenamiento;
    private String camara;
    private String ram;
    private int stock;
    private double precio;

    public Producto(int id, String marca, String modelo, String pantalla, String almacenamiento, String camara, String ram, int stock, double precio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.pantalla = pantalla;
        this.almacenamiento = almacenamiento;
        this.camara = camara;
        this.ram = ram;
        this.stock = stock;
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

    public int getStock() {
        return stock;
    }

    public double getPrecio() {
        return precio;
    }
    
}
