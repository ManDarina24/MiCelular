package Controlador;

import Modelo.Inventario;

/**
 *
 * @author darina
 */

public class Main {
    public static void main(String[] args) {
        
        CtrlAdministracion admin = new CtrlAdministracion();
        admin.iniciar();
        
        
        //Inventario inventario = new Inventario();
        //System.out.println(inventario.buscaProducto(1));
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


