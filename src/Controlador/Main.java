package Controlador;

import Modelo.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author darina
 */

public class Main {
    public static void main(String[] args) {
        
        CtrlAdministracion admin = new CtrlAdministracion();
        admin.iniciar();
        
        //Venta venta = new Venta();
        
        /*Inventario inventario = new Inventario();
        inventario.modificaStockProducto(2, 1);*/
        /*Cliente cliente = new Cliente("Mariel", "Hernandez", "Cervantes", 2465897100L, "mari@gmail.com");
        cliente.setId(venta.registraCliente(cliente));
        System.out.println("Cliente: "+cliente.getId());
        Producto producto1 = new Producto(2, 1, 14499);
        Producto producto2 = new Producto(12, 1, 5000);
        List<Producto> productos = new ArrayList<>();
        productos.add(producto2);
        productos.add(producto1);
        
        System.out.println(venta.registraVenta(cliente.getId(), productos, 19999, Venta.MetodoPago.efectivo, "Administrador"));*/
        //System.out.println(venta.registraCliente("Aram", "Espinosa", "Tlatelpa", 2462085266L, "aram@gmail.com"));
        //System.out.println(venta.registraCliente());
        //Inventario inventario = new Inventario();
        //System.out.println(inventario.buscaProducto(15));
        //System.out.println(inventario.eliminaProducto(0));
        
        //int folio = inventario.agregaDatosProducto("Samsung", "Galaxy S23 Ultra ", "6.8", "200 MP","512 GB", "12GB");
        //inventario.agregaProductoInventario(folio, 18799.00, 5);
        
        /*boolean productoModificado = inventario.modificaProducto(1, "Samsung", "Galaxy S23 Ultra ", "6.8", "200MP", "512 GB", "8 RAM");
        if (productoModificado) {
            System.out.println("Producto modificado correctamente.");
        } else {
            System.out.println("No se pudo modificar el producto.");
        }

        // Ejemplo de modificación de inventario
        boolean inventarioModificado = inventario.modificaInventario(1, 6, 18799.00);
        if (inventarioModificado) {
            System.out.println("Inventario modificado correctamente.");
        } else {
            System.out.println("No se pudo modificar el inventario.");
        }*/
    }
}


