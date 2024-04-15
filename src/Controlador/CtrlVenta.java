package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author darina
 */
public class CtrlVenta implements ActionListener, ListSelectionListener{
    private List<Producto> productosCanasta = new ArrayList<>();
    private VVenta vistaVenta = new VVenta();
    private Inventario modeloInventario = new Inventario();
    private AgregarConfirmacion vistaAgregaProducto = new AgregarConfirmacion();
    private Canasta canasta = new Canasta(); 
    
    public CtrlVenta(){
        
        this.vistaVenta.btnBuscar.addActionListener(this);
        this.vistaVenta.tabla.getSelectionModel().addListSelectionListener(this);
        this.vistaVenta.btnAgregar.addActionListener(this);
        this.vistaAgregaProducto.btnAceptar.addActionListener(this);
        this.vistaAgregaProducto.btnCancelar.addActionListener(this);
        this.vistaVenta.btnCanasta.addActionListener(this);
        this.canasta.btnAceptar.addActionListener(this);
        
    }
    
    public void iniciar(){
        vistaVenta.setVisible(true);
        vistaVenta.setTitle("Venta");
        vistaVenta.setLocationRelativeTo(null);
        vistaVenta.tabla.setModel(listar(vistaVenta.tabla, 0));
        vistaVenta.btnAgregar.setEnabled(false);
        
    }
    
    
     @Override
    public void valueChanged(ListSelectionEvent e) {
        
        if (!e.getValueIsAdjusting() && vistaVenta.tabla.getSelectedRow() != -1) {
                vistaVenta.btnAgregar.setEnabled(true); // Habilitar btnAgregar si se ha seleccionado una fila
            } else {
                vistaVenta.btnAgregar.setEnabled(false); // Deshabilitar btnAgregar si no se ha seleccionado ninguna fila
            }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaVenta.btnBuscar) {
            int folio = 0;
            String strfolio = vistaVenta.getFolioTxt().getText();
            if (strfolio.matches("\\d+") && !strfolio.isEmpty()) {
                //Es entero positivo
                folio = Integer.parseInt(strfolio);
                if (modeloInventario.buscaProducto(folio) == null) {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    vistaVenta.getFolioTxt().setText("");
                    folio = 0;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingresa datos validos");
                vistaVenta.getFolioTxt().setText("");
            }
            vistaVenta.tabla.setModel(listar(vistaVenta.tabla, folio));
        }

        if (e.getSource() == vistaVenta.btnAgregar) {
            vistaVenta.setEnabled(false);
            vistaAgregaProducto.setVisible(true);
            vistaAgregaProducto.setTitle("Agregar producto seleccionado");
            vistaAgregaProducto.setLocationRelativeTo(null);

            //Actualiza la info de la venta 
            int filaSeleccionada = vistaVenta.tabla.getSelectedRow();
            Object[] datosFila = new Object[vistaVenta.tabla.getColumnCount()];
            for (int i = 0; i < vistaVenta.tabla.getColumnCount(); i++) {
                datosFila[i] = vistaVenta.tabla.getValueAt(filaSeleccionada, i);
            }

            Producto aux = modeloInventario.buscaProducto(Integer.parseInt(datosFila[0].toString()));
            vistaAgregaProducto.getIdLbl().setText(String.valueOf(aux.getId()));
            vistaAgregaProducto.getMarcaLbl().setText("Marca: " + aux.getMarca());
            vistaAgregaProducto.getModeloLbl().setText("Modelo: " + aux.getModelo());
            vistaAgregaProducto.getPantallaLbl().setText("Pantalla: " + aux.getPantalla());
            vistaAgregaProducto.getCamaraLbl().setText("Camara: " + aux.getCamara());
            vistaAgregaProducto.getRamLbl().setText("Ram: " + aux.getRam());
            vistaAgregaProducto.getAlmLbl().setText("Almacenamiento: " + aux.getAlmacenamiento());
            vistaAgregaProducto.getPrecioLbl().setText("Precio: " + aux.getPrecio());

            SpinnerNumberModel modeloSpinner = (SpinnerNumberModel) vistaAgregaProducto.CantidadSpinner.getModel();
            modeloSpinner.setMaximum(aux.getCantidad()); // Establece el valor máximo
            modeloSpinner.setMinimum(1);
            modeloSpinner.setValue(1);
            vistaAgregaProducto.CantidadSpinner.setModel(modeloSpinner);
        }

        if (e.getSource() == vistaAgregaProducto.btnAceptar) {
            vistaVenta.setVisible(true);
            vistaVenta.setEnabled(true);
            vistaAgregaProducto.setVisible(false);
            
            Producto producto = modeloInventario.buscaProducto(Integer.parseInt(vistaAgregaProducto.getIdLbl().getText()));
            producto.setCantidad(Integer.parseInt(vistaAgregaProducto.CantidadSpinner.getValue().toString()));
            productosCanasta.add(producto);

        }

        if (e.getSource() == vistaAgregaProducto.btnCancelar) {
            vistaVenta.setVisible(true);
            vistaVenta.setEnabled(true);
            vistaAgregaProducto.setVisible(false);

        }

        if (e.getSource() == vistaVenta.btnCanasta) {
            canasta.setVisible(true);
            canasta.setTitle("Detalles venta");
            canasta.setLocationRelativeTo(null);
            vistaVenta.setEnabled(false);
            canasta.tabla.setModel(listarCanasta(canasta.tabla));
            
            if (!productosCanasta.isEmpty()){
                double total = 0;
                for (int i = 0; i < productosCanasta.size(); i++){
                    total = total + (productosCanasta.get(i).getPrecio() * productosCanasta.get(i).getCantidad());
                }
                canasta.lblTotal.setText("Total: $" + total);
            }
            
            
            

        }
        
        if (e.getSource() == canasta.btnAceptar){
            vistaVenta.setVisible(true);
            vistaVenta.setEnabled(true);
            canasta.setVisible(false);
        }

    }

    private DefaultTableModel listar(JTable tabla, int folio) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }

        Object[] object = new Object[9];
        Producto producto = modeloInventario.buscaProducto(folio);

        if (producto != null) {
            object[0] = producto.getId();
            object[1] = producto.getMarca();
            object[2] = producto.getModelo();
            object[3] = producto.getPrecio();
            object[4] = producto.getCantidad();
            modelo.addRow(object);
        } else {
            List<Producto> lista = modeloInventario.listar();

            for (int i = 0; i < lista.size(); i++) {
                object[0] = lista.get(i).getId();
                object[1] = lista.get(i).getMarca();
                object[2] = lista.get(i).getModelo();
                object[3] = lista.get(i).getPrecio();
                object[4] = lista.get(i).getCantidad();
                modelo.addRow(object);
            }
        }

        return modelo;

    }
    
    private DefaultTableModel listarCanasta(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        List<Producto> lista = productosCanasta;
        Object[] object = new Object[9];

        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getMarca();
            object[2] = lista.get(i).getModelo();
            object[3] = lista.get(i).getPrecio();
            object[4] = lista.get(i).getCantidad();
            modelo.addRow(object);
        }
        return modelo;

    }

    

   
    
    
    
        
}
