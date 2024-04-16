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
    private CtrlAdministracion admin = new CtrlAdministracion();
    private VCliente vistaCliente = new VCliente();
    private Cliente modeloCliente;
    private InfoVenta vistaDetalles = new InfoVenta();
    private Venta modeloVenta = new Venta();
    
    public CtrlVenta(){
        
        this.vistaVenta.btnBuscar.addActionListener(this);
        this.vistaVenta.tabla.getSelectionModel().addListSelectionListener(this);
        this.vistaVenta.btnAgregar.addActionListener(this);
        this.vistaAgregaProducto.btnAceptar.addActionListener(this);
        this.vistaAgregaProducto.btnCancelar.addActionListener(this);
        this.vistaVenta.btnCanasta.addActionListener(this);
        this.canasta.btnAceptar.addActionListener(this);
        this.canasta.tabla.getSelectionModel().addListSelectionListener(this);
        this.canasta.btnBasura.addActionListener(this);
        this.vistaVenta.btnRegresar.addActionListener(this);
        this.vistaVenta.btnSiguiente.addActionListener(this);
        this.vistaCliente.btnSiguiente.addActionListener(this);
        this.vistaCliente.btnRegresar.addActionListener(this);
        this.vistaDetalles.btnRegresar1.addActionListener(this);
        this.vistaDetalles.btnCancelar.addActionListener(this);
        this.vistaDetalles.btnPagar.addActionListener(this);
    }
    
    public void iniciar(){
        vistaVenta.setVisible(true);
        vistaVenta.setTitle("Venta");
        vistaVenta.setLocationRelativeTo(null);
        vistaVenta.tabla.setModel(listar(vistaVenta.tabla, 0));
        vistaVenta.btnAgregar.setEnabled(false);
        canasta.btnBasura.setEnabled(false);
        vistaVenta.btnSiguiente.setEnabled(false);
    }
    
    
     @Override
    public void valueChanged(ListSelectionEvent e) {

        if (!e.getValueIsAdjusting() && vistaVenta.tabla.getSelectedRow() != -1) {
            vistaVenta.btnAgregar.setEnabled(true); // Habilitar btnAgregar si se ha seleccionado una fila
        } else {
            vistaVenta.btnAgregar.setEnabled(false); // Deshabilitar btnAgregar si no se ha seleccionado ninguna fila
        }
        
        if (!e.getValueIsAdjusting() && canasta.tabla.getSelectedRow() != -1) {
            canasta.btnBasura.setEnabled(true);
        } else {
            canasta.btnBasura.setEnabled(false);
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
            
            boolean bandera = false;
            if (!productosCanasta.isEmpty()) {
                for (int i = 0; i < productosCanasta.size(); i++) {
                    if (producto.getId() == productosCanasta.get(i).getId() ){
                        bandera = true;
                    }

                }
            } 
            
            if (productosCanasta.isEmpty() || bandera == false){
                productosCanasta.add(producto);
                vistaVenta.btnSiguiente.setEnabled(true);
            } else {
                String[] opciones = {"Aceptar"};
                JOptionPane.showOptionDialog(null, "El producto ya se encuentra en la canasta", "Producto", 0, JOptionPane.INFORMATION_MESSAGE, null, opciones, "Aceptar");
      
            }
            

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
           
            double total = 0;
            if (!productosCanasta.isEmpty()){
                
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
        
        if (e.getSource() == canasta.btnBasura) {
            String[] opciones = {"Si", "No"};
            int resultado = JOptionPane.showOptionDialog(null, "¿Deseas eliminar este elemento?", "Eliminar producto de la cesta", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Si");

            if (resultado == 0) {
                int filaSeleccionada = canasta.tabla.getSelectedRow();
                Object[] datosFila = new Object[canasta.tabla.getColumnCount()];
                for (int i = 0; i < canasta.tabla.getColumnCount(); i++) {
                    datosFila[i] = canasta.tabla.getValueAt(filaSeleccionada, i);
                }
                
                for (int i = 0; i < productosCanasta.size(); i++){
                    if (productosCanasta.get(i).getId() == Integer.parseInt(datosFila[0].toString())){
                        productosCanasta.remove(i);
                        break;
                    }
                }
            }
            
            canasta.tabla.setModel(listarCanasta(canasta.tabla));
            
            double total = 0;
            if (!productosCanasta.isEmpty()){
                
                for (int i = 0; i < productosCanasta.size(); i++){
                    total = total + (productosCanasta.get(i).getPrecio() * productosCanasta.get(i).getCantidad());
                }
                canasta.lblTotal.setText("Total: $" + total);
            } else {
                canasta.lblTotal.setText("Total: $" + 0);
                vistaVenta.btnSiguiente.setEnabled(false);
            }
        }
        
        if (e.getSource() == vistaVenta.btnRegresar){
            
            String[] opciones = {"Aceptar", "Cancelar"};
            int resultado = JOptionPane.showOptionDialog(null, "Si sales de esta seccion los productos\n agregados a la canasta se perderan  \n¿Salir de todas formas?", "Salir", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Cancelar");
            
            if (resultado == 0){
                vistaVenta.setVisible(false);
                admin.mostrarMenuPrincipal();
            }
            
        }
        
        if (e.getSource() == vistaVenta.btnSiguiente){
            vistaVenta.setVisible(false);
            vistaCliente.setVisible(true);
            vistaCliente.setTitle("Registro cliente");
            vistaCliente.setLocationRelativeTo(null);
        }
        
        if (e.getSource() == vistaCliente.btnSiguiente) {
            String nombre = vistaCliente.getNombreTxt().getText();
            String paterno = vistaCliente.getPaternoTxt().getText();
            String materno = vistaCliente.getMaternoTxt().getText();
            String tel = vistaCliente.getTelTxt().getText();
            String correo = vistaCliente.getCorreoTxt().getText();

            boolean validar = validarRegistro(nombre, paterno, materno, tel, correo);
            if (validar) {
                long telefono = Long.parseLong(tel);
                modeloCliente = new Cliente(nombre, paterno, materno, telefono, correo);
                vistaDetalles.setVisible(true);
                vistaDetalles.setLocationRelativeTo(null);
                vistaDetalles.setTitle("Detalles de la venta");
                vistaCliente.setVisible(false);
                vistaDetalles.tabla.setModel(listarCanasta(vistaDetalles.tabla));
                vistaDetalles.tablaClientes.setModel(listarCliente(vistaDetalles.tablaClientes));

                //here
                double total = 0;

                for (int i = 0; i < productosCanasta.size(); i++) {
                    total = total + (productosCanasta.get(i).getPrecio() * productosCanasta.get(i).getCantidad());
                }
                vistaDetalles.totalFinal.setText(String.valueOf(total));
            } else {
                String[] opciones = {"Aceptar"};
                JOptionPane.showOptionDialog(null, "Los datos no son validos, intentelo de nuevo", "Datos invalidos", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");

            }

        }

        if (e.getSource() == vistaCliente.btnRegresar){
            vistaVenta.setVisible(true);
            vistaCliente.setVisible(false);
            vistaVenta.setLocationRelativeTo(null);
        }
        
        if (e.getSource() == vistaDetalles.btnRegresar1){
            vistaCliente.setVisible(true);
            vistaCliente.setLocationRelativeTo(null);
            vistaDetalles.setVisible(false);
        }
        
        if (e.getSource() == vistaDetalles.btnPagar) {
            String[] opciones = {"Aceptar", "Cancelar"};
            int opc = JOptionPane.showOptionDialog(null, "¿Deseas continuar con la venta?", "Pago", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
            
            if (opc == 0){
                
                double total = 0;

                for (int i = 0; i < productosCanasta.size(); i++) {
                    total = total + (productosCanasta.get(i).getPrecio() * productosCanasta.get(i).getCantidad());
                }

                //Guarda en la bd
                int id = modeloVenta.registraCliente(modeloCliente);
                modeloVenta.registraVenta(id, productosCanasta, total, Venta.MetodoPago.efectivo, "Administrador");
                
                for(int i = 0; i < productosCanasta.size(); i++){
                    Producto producto = modeloInventario.buscaProducto(productosCanasta.get(i).getId());
                    int cantidadNueva = producto.getCantidad() - productosCanasta.get(i).getCantidad();
                    modeloInventario.modificaStockProducto(producto.getId(), cantidadNueva);
                }
                
                String[] conf = {"Aceptar"};
                JOptionPane.showOptionDialog(null, "El pago se ha realizado de manera exitosa", "Pago", 0, JOptionPane.INFORMATION_MESSAGE, null, conf, "Aceptar");
      
                //Redirige
                vistaDetalles.setVisible(false);
                admin.mostrarMenuPrincipal();
            }
        }
        
        if (e.getSource() == vistaDetalles.btnCancelar) {
            String[] opciones = {"Aceptar", "Cancelar"};
            int opc = JOptionPane.showOptionDialog(null, "¿Deseas cancelar la venta?", "Cancelar", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
            
            if (opc == 0){
                vistaDetalles.setVisible(false);
                admin.mostrarMenuPrincipal();
            }
        }

    }

    private DefaultTableModel listar(JTable tabla, int folio) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }

        Object[] object = new Object[5];
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
    
     private DefaultTableModel listarCliente(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        
        Object[] object = new Object[5];
        Cliente cliente = modeloCliente;

        if (cliente != null) {
            object[0] = cliente.getNombre();
            object[1] = cliente.getApellidoPaterno();
            object[2] = cliente.getApellidoMaterno();
            object[3] = cliente.getTelefono();
            object[4] = cliente.getCorreo();
            modelo.addRow(object);
        }
        return modelo;

    }

    public boolean validarRegistro(String nombre, String apellidoPaterno, String apellidoMaterno, String tel, String correo) {

        // Verificar que los nombres y apellidos contengan solo letras
        if (!nombre.matches("[a-zA-Z]+") || !apellidoPaterno.matches("[a-zA-Z]+") || !apellidoMaterno.matches("[a-zA-Z]+")) {
            System.out.println("1");
            return false;
        }

        // Verificar que el teléfono tenga 10 dígitos numéricos
        if (!tel.matches("\\d{10}")) {
            System.out.println("2");
            return false;
        }

        // Verificar que el correo electrónico tenga un formato válido
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("3");
            return false;
        }

        // Si pasa todas las validaciones, los datos son válidos
        return true;
    }
    
}
