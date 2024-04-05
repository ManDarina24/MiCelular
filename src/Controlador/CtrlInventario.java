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
    private final Inventario modeloInventario = new Inventario();
    
    public CtrlInventario(){
        this.vistaInv.btnAgregar.addActionListener(this);
        this.vistaAgregar.btnAgregaInventario.addActionListener(this);
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

    
    
}
