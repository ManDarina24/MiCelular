package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author darina
 */
public class CtrlInventario implements ActionListener {
    private final MenuInventario vistaInv = new MenuInventario();
    private final AgregaProducto vistaAgregar = new AgregaProducto();
    private final ModificaProductos vistaModificar = new ModificaProductos();
    private final Inventario modeloInventario = new Inventario();
    
    public CtrlInventario(){
        this.vistaInv.btnAgregar.addActionListener(this);
        this.vistaAgregar.btnAgregaInventario.addActionListener(this);
        this.vistaInv.btnModificar.addActionListener(this);
        this.vistaModificar.btnBuscar.addActionListener(this);
        this.vistaModificar.btnModificaInventario.addActionListener(this);
    }
    
    public void iniciar() {
        vistaInv.setVisible(true);
        vistaInv.setTitle("Menu inventario");
        vistaInv.setLocationRelativeTo(null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //e.getSource() == vistaLogin.btnIngresar
        if (e.getSource() == vistaInv.btnAgregar){
            vistaInv.setVisible(false);
            vistaAgregar.setVisible(true);
            vistaAgregar.setTitle("Agregar producto");
            vistaAgregar.setLocationRelativeTo(null);
        }
        
        if (e.getSource() == vistaInv.btnModificar){
            vistaInv.setVisible(false);
            vistaModificar.setVisible(true);
            vistaModificar.setTitle("Modificar productos");
            vistaModificar.setLocationRelativeTo(null);
        }
        
        if (e.getSource() == vistaModificar.btnBuscar){
            
            int folio = Integer.parseInt(vistaModificar.getFolioTxt().getText());
            if (modeloInventario.buscaProducto(folio) != null){
                //El inventario nos regresa in Producto, entonces eso lo asignamos a las casillas para que el 
                //usuario pueda modificarlo despues 
                Producto producto = modeloInventario.buscaProducto(folio);
                vistaModificar.getMarcaTxt().setText(producto.getMarca());
                vistaModificar.getModeloTxt().setText(producto.getModelo());
                vistaModificar.getPantallaTxt().setText(producto.getPantalla());
                vistaModificar.getAlmTxt().setText(producto.getAlmacenamiento());
                vistaModificar.getCamaraTxt().setText(producto.getCamara());
                vistaModificar.getRamTxt().setText(producto.getRam());
                vistaModificar.getStockTxt().setValue(producto.getStock());
                vistaModificar.getPrecioTxt().setText(String.valueOf(producto.getPrecio()));
            } else{
                JOptionPane.showMessageDialog(null, "Producto no encontrado, intentelo de nuevo");
                limpiarVistaModificar();
            }   
        }
        
        if (e.getSource() == vistaModificar.btnModificaInventario) {
            if (validaDatosModificados()) {
                int id = Integer.parseInt(vistaModificar.getFolioTxt().getText());
                String marca = vistaModificar.getMarcaTxt().getText();
                String modelo = vistaModificar.getModeloTxt().getText();
                String pantalla = vistaModificar.getPantallaTxt().getText();
                String camara = vistaModificar.getCamaraTxt().getText();
                String almacenamiento = vistaModificar.getAlmTxt().getText();
                String ram = vistaModificar.getRamTxt().getText();
                double precio = Double.parseDouble(vistaModificar.getPrecioTxt().getText());
                int stock = Integer.parseInt(vistaModificar.getStockTxt().getValue().toString());
                
                boolean aux1 = modeloInventario.modificaInventario(id, stock, precio);
                boolean aux2 = modeloInventario.modificaProducto(id, marca, modelo, pantalla, camara, almacenamiento, ram);
                
                if (aux1 && aux2){
                    limpiarVistaModificar();
                    JOptionPane.showMessageDialog(null, "El producto ha sido modificado");
                }
                        

            } else {
                JOptionPane.showMessageDialog(null, "Los datos no son validos o hay campos vacios, intentelo de nuevo");
            }

        }
        
        
        if (e.getSource() == vistaAgregar.btnAgregaInventario){
            
            //valida los datos 
            if (validaDatosAgregados()) {
                //Asignar valores recibidos a variables 
                String marca = vistaAgregar.getMarcaTxt().getText();
                String modelo = vistaAgregar.getModeloTxt().getText();
                String pantalla = vistaAgregar.getPantallaTxt().getText();
                String camara = vistaAgregar.getCamaraTxt().getText();
                String almacenamiento = vistaAgregar.getAlmTxt().getText();
                String ram = vistaAgregar.getRamTxt().getText();
                double precio = Double.parseDouble(vistaAgregar.getPrecioTxt().getText());
                int stock = Integer.parseInt(vistaAgregar.getStockTxt().getValue().toString());
                
                //Agregamos los datos a la bd 
                int folio = modeloInventario.agregaDatosProducto(marca, modelo, pantalla, camara, almacenamiento, ram);
                
                if (folio > 0){
                    if (modeloInventario.agregaProductoInventario(folio, precio, stock)){
                        limpiarVistaAgregar();
                        JOptionPane.showMessageDialog(null, "El producto ha sido agregado");
                    }
                }
                
                
            } else {
                JOptionPane.showMessageDialog(null, "Los datos no son validos o hay campos vacios, intentelo de nuevo");
            }
        }
    }
    
    
    private boolean validaDatosAgregados() {
        int contador = 0;
        boolean bandera = false;
        if (!vistaAgregar.getMarcaTxt().getText().isEmpty()){
            //Marca
            contador++;
        }
        if (!vistaAgregar.getModeloTxt().getText().isEmpty()){
            //Modelo
            contador++;
        }
        if (!vistaAgregar.getPantallaTxt().getText().isEmpty()){
            //Pantalla
            contador++;
        }
        if (!vistaAgregar.getCamaraTxt().getText().isEmpty()){
            //Camara
            contador++;
        }
        if (!vistaAgregar.getAlmTxt().getText().isEmpty()){
            //Almacenamiento
            contador++;
        }
        if (!vistaAgregar.getRamTxt().getText().isEmpty()){
            //Almacenamiento
            contador++;
        }
        // Verificar si precio y stock son números
        String precioStr = vistaAgregar.getPrecioTxt().getText();
        String stockStr = vistaAgregar.getStockTxt().getValue().toString();
        if (!precioStr.isEmpty() && !stockStr.isEmpty()) {
            if (precioStr.matches("-?\\d+(\\.\\d+)?") && stockStr.matches("-?\\d+(\\.\\d+)?")) {
                contador++; // Incrementar contador si ambos precios y stock son números válidos
                System.out.println("Numeros validos");
            }
        }
        // Si todos los campos requeridos están llenos, establecer bandera en true
        if (contador == 7) {
            bandera = true;
        }
        return bandera;
    }
    
    private boolean validaDatosModificados() {
        int contador = 0;
        boolean bandera = false;
        if (!vistaModificar.getMarcaTxt().getText().isEmpty()){
            //Marca
            contador++;
        }
        if (!vistaModificar.getModeloTxt().getText().isEmpty()){
            //Modelo
            contador++;
        }
        if (!vistaModificar.getPantallaTxt().getText().isEmpty()){
            //Pantalla
            contador++;
        }
        if (!vistaModificar.getCamaraTxt().getText().isEmpty()){
            //Camara
            contador++;
        }
        if (!vistaModificar.getAlmTxt().getText().isEmpty()){
            //Almacenamiento
            contador++;
        }
        if (!vistaModificar.getRamTxt().getText().isEmpty()){
            //Almacenamiento
            contador++;
        }
        // Verificar si precio y stock son números
        String precioStr = vistaModificar.getPrecioTxt().getText();
        String stockStr = vistaModificar.getStockTxt().getValue().toString();
        if (!precioStr.isEmpty() && !stockStr.isEmpty()) {
            if (precioStr.matches("-?\\d+(\\.\\d+)?") && stockStr.matches("-?\\d+(\\.\\d+)?")) {
                contador++; // Incrementar contador si ambos precios y stock son números válidos
                System.out.println("Numeros validos");
            }
        }
        // Si todos los campos requeridos están llenos, establecer bandera en true
        if (contador == 7) {
            bandera = true;
        }
        return bandera;
    }
    
    private void limpiarVistaAgregar() {
        vistaAgregar.getMarcaTxt().setText("");
        vistaAgregar.getModeloTxt().setText("");
        vistaAgregar.getPantallaTxt().setText("");
        vistaAgregar.getCamaraTxt().setText("");
        vistaAgregar.getAlmTxt().setText("");
        vistaAgregar.getRamTxt().setText("");
        vistaAgregar.getPrecioTxt().setText("");
        vistaAgregar.getStockTxt().setValue(0);
    }
    
    private void limpiarVistaModificar() {
        vistaModificar.getFolioTxt().setText("");
        vistaModificar.getMarcaTxt().setText("");
        vistaModificar.getModeloTxt().setText("");
        vistaModificar.getPantallaTxt().setText("");
        vistaModificar.getCamaraTxt().setText("");
        vistaModificar.getAlmTxt().setText("");
        vistaModificar.getRamTxt().setText("");
        vistaModificar.getPrecioTxt().setText("");
        vistaModificar.getStockTxt().setValue(0);
    }

    
}
