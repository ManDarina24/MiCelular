package Controlador;

import Modelo.*;
import Vista.*;
import com.itextpdf.text.DocumentException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;

/**
 *
 * @author darina
 */
public class CtrlVenta implements ActionListener, ListSelectionListener, DocumentListener {
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
    private PagoEnEfectivo vistaEfectivo = new PagoEnEfectivo(); 
    private PagoConTarjeta vistaTarjeta = new PagoConTarjeta();
    
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
        this.vistaDetalles.btnPagarEfectivo.addActionListener(this);
        this.vistaEfectivo.montoTxt.getDocument().addDocumentListener(this);
        this.vistaEfectivo.btnPagar.addActionListener(this);
        this.vistaEfectivo.btnRegresar.addActionListener(this);
        this.vistaDetalles.btnPagarTarjeta.addActionListener(this);
        this.vistaTarjeta.btnPagar.addActionListener(this);
        this.vistaTarjeta.btnRegresar.addActionListener(this);
    }
    
    public void iniciar(){
        vistaVenta.setVisible(true);
        vistaVenta.setTitle("Venta");
        vistaVenta.setLocationRelativeTo(null);
        vistaVenta.tabla.setModel(listar(vistaVenta.tabla, 0));
        vistaVenta.btnAgregar.setEnabled(false);
        canasta.btnBasura.setEnabled(false);
        vistaVenta.btnSiguiente.setEnabled(false);
        vistaEfectivo.btnPagar.setEnabled(false);
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
                    String[] opciones = {"Aceptar"};
                    JOptionPane.showOptionDialog(null, "Producto no encontrado", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

                    vistaVenta.getFolioTxt().setText("");
                    folio = 0;
                }
            } else {
                String[] opciones = {"Aceptar"};
                JOptionPane.showOptionDialog(null, "Ingresa datos validos", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

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
           
            double total = Pago.calcularTotal(productosCanasta);
            canasta.lblTotal.setText("Total: $" + total);
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
            
            double total = Pago.calcularTotal(productosCanasta);
            canasta.lblTotal.setText("Total: $" + total);
            
            if (total == 0){
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
            
            vistaCliente.telTxt.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    JTextField textField = (JTextField) vistaCliente.telTxt;
                    String text = textField.getText();
                    if (!Character.isDigit(c) || text.length() >= 10) {
                        e.consume();
                    }
                }
            });
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

                double total = Pago.calcularTotal(productosCanasta);
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
        
        if (e.getSource() == vistaDetalles.btnPagarEfectivo){
            vistaEfectivo.setVisible(true);
            vistaEfectivo.setLocationRelativeTo(null);
            vistaEfectivo.setTitle("Efectivo");
            vistaDetalles.setEnabled(false);
            double total = Pago.calcularTotal(productosCanasta);
            vistaEfectivo.lblTotal.setText(String.valueOf(total));
        }
        
        if (e.getSource() == vistaDetalles.btnPagarTarjeta){
            vistaTarjeta.setVisible(true);
            vistaTarjeta.setLocationRelativeTo(null);
            vistaTarjeta.setTitle("Tarjeta");
            vistaDetalles.setEnabled(false);
            double total = Pago.calcularTotal(productosCanasta);
            vistaTarjeta.lblTotal.setText(String.valueOf(total));
            
            vistaTarjeta.numeroTarjetatxt.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    JTextField textField = (JTextField) e.getSource();
                    String text = textField.getText();
                    if (!Character.isDigit(c) || text.length() >= 16) {
                        e.consume();
                    }
                }
            });
            
            vistaTarjeta.cvvTxt.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    JTextField textField = (JTextField) e.getSource();
                    String text = textField.getText();
                    if (!Character.isDigit(c) || text.length() >= 3) {
                        e.consume();
                    }
                }
            });
            
         
        }
        
        if (e.getSource() == vistaTarjeta.btnPagar) {
            String[] opciones = {"Aceptar", "Cancelar"};
            int opc = JOptionPane.showOptionDialog(null, "¿Deseas continuar con el pago?", "Pago", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");

            if (opc == 0) {
                if (validarDatosTarjeta()) {
                    System.out.println("Datos validos");
                    vistaTarjeta.proceso.setText("Validando datos con el banco...");
                    // Tercera tarea: Cambiar el texto del proceso a "Validando datos con el banco"
                    Timer timer3 = new Timer(5000, (event3) -> {

                        PagoTarjeta tarjeta = new PagoTarjeta();

                        if (tarjeta.validarPago()) {

                            // Mostrar mensaje de éxito
                            String[] conf = {"Aceptar"};
                            JOptionPane.showOptionDialog(null, "El pago se ha realizado de manera exitosa", "Pago", 0, JOptionPane.INFORMATION_MESSAGE, null, conf, "Aceptar");

                            double totalVenta = Pago.calcularTotal(productosCanasta);
                            //Registra la venta
                            int id = modeloVenta.registraCliente(modeloCliente);

                            modeloCliente.setId(id);
                            int folioVenta = modeloVenta.registraVenta(id, productosCanasta, totalVenta, Venta.MetodoPago.tarjeta, "Administrador");

                            //Disminuir Stock
                            for (int i = 0; i < productosCanasta.size(); i++) {
                                Producto producto = modeloInventario.buscaProducto(productosCanasta.get(i).getId());
                                int cantidadNueva = producto.getCantidad() - productosCanasta.get(i).getCantidad();
                                modeloInventario.modificaStockProducto(producto.getId(), cantidadNueva);
                            }
                            String numeroTarjeta = vistaTarjeta.numeroTarjetatxt.getText();
                            try {
                                //Por que marca erro?
                                Pago.generarRecibo(folioVenta, productosCanasta, modeloCliente, 0, "tarjeta", numeroTarjeta);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(CtrlVenta.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (DocumentException ex) {
                                Logger.getLogger(CtrlVenta.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //Redirige
                            vistaDetalles.setVisible(false);
                            vistaTarjeta.setVisible(false);
                            admin.mostrarMenuPrincipal();

                        } else {
                            String[] conf = {"Aceptar"};
                            JOptionPane.showOptionDialog(null, "Tarjeta no valida, intentelo de nuevo", "Pago", 0, JOptionPane.ERROR_MESSAGE, null, conf, "Aceptar");

                        }
                    });
                    timer3.setRepeats(false); // Esta tarea se ejecuta una sola vez
                    timer3.start(); // Comienza la tercera tarea

                }

            }
        }
        
        if (e.getSource() == vistaEfectivo.btnPagar) {
            String[] opciones = {"Aceptar", "Cancelar"};
            int opc = JOptionPane.showOptionDialog(null, "¿Deseas continuar con el pago?", "Pago", 0, JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");

            if (opc == 0) {

                try {
                    double totalVenta = Pago.calcularTotal(productosCanasta);
                    //Registra la venta
                    int id = modeloVenta.registraCliente(modeloCliente);
                    
                    modeloCliente.setId(id);
                    int folioVenta = modeloVenta.registraVenta(id, productosCanasta, totalVenta, Venta.MetodoPago.efectivo, "Administrador");
                    
                    //Disminuir Stock
                    for (int i = 0; i < productosCanasta.size(); i++) {
                        Producto producto = modeloInventario.buscaProducto(productosCanasta.get(i).getId());
                        int cantidadNueva = producto.getCantidad() - productosCanasta.get(i).getCantidad();
                        modeloInventario.modificaStockProducto(producto.getId(), cantidadNueva);
                    }
                    //Generar recibo
                    Pago.generarRecibo(folioVenta, productosCanasta, modeloCliente, Double.parseDouble(vistaEfectivo.montoTxt.getText()), "efectivo", "");
                    
                    String[] conf = {"Aceptar"};
                    JOptionPane.showOptionDialog(null, "El pago se ha realizado de manera exitosa", "Pago", 0, JOptionPane.INFORMATION_MESSAGE, null, conf, "Aceptar");
                    
                    //Redirige
                    vistaDetalles.setVisible(false);
                    vistaEfectivo.setVisible(false);
                    admin.mostrarMenuPrincipal();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CtrlVenta.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(CtrlVenta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        if (e.getSource() == vistaTarjeta.btnRegresar){
            vistaTarjeta.setVisible(false);
            vistaDetalles.setEnabled(true);
            vistaDetalles.setVisible(true);
            
        }
        if (e.getSource() == vistaEfectivo.btnRegresar){
            vistaEfectivo.setVisible(false);
            vistaDetalles.setEnabled(true);
            vistaDetalles.setVisible(true);
            vistaEfectivo.cambioLbl.setText("0.0");
            vistaEfectivo.montoTxt.setText("");
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

        if (producto != null && producto.getCantidad() > 0) {
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
                if (lista.get(i).getCantidad() > 0){
                    modelo.addRow(object);
                }
                
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
        if (!nombre.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ]+") || !apellidoPaterno.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ]+") || !apellidoMaterno.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ]+")) {
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
    @Override
    public void insertUpdate(DocumentEvent e) {
        actualizarCambio();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        actualizarCambio();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        actualizarCambio();
    }
    
    private void actualizarCambio() {
        PagoEfectivo efectivo = new PagoEfectivo();
        double total = Pago.calcularTotal(productosCanasta);
        if (vistaEfectivo.montoTxt.getText().matches("\\d*\\.?\\d+")) {
            double cambio = efectivo.calcularCambio(total, Double.parseDouble(vistaEfectivo.montoTxt.getText()));
            vistaEfectivo.cambioLbl.setText(String.valueOf(cambio) + " pesos");

            if (cambio >= 0) {
                vistaEfectivo.btnPagar.setEnabled(true);
            } else {
                vistaEfectivo.btnPagar.setEnabled(false); // Bloquear el botón si el cambio es negativo
            }
        } else {
            String[] opciones = {"Aceptar"};
            JOptionPane.showOptionDialog(null, "Valor invalido", "Pago", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");
            vistaEfectivo.btnPagar.setEnabled(false);
        }
    }
    
    private boolean validarDatosTarjeta() {
        String numeroTarjeta = vistaTarjeta.numeroTarjetatxt.getText();
        String cvv = vistaTarjeta.cvvTxt.getText();
        if (numeroTarjeta.length() != 16 || cvv.length() != 3) {
            String[] opci = {"Aceptar"};
            JOptionPane.showOptionDialog(null, "Ingresa todos los campos", "Pago", 0, JOptionPane.QUESTION_MESSAGE, null, opci, "Aceptar");
            return false;
        }
        
        String mesExpiracion = (String) vistaTarjeta.mesTxt.getSelectedItem();
        String añoExpiracion = (String) vistaTarjeta.anioTxt.getSelectedItem();
        int mesActual = LocalDate.now().getMonthValue();
        int añoActual = LocalDate.now().getYear();
        int mesSeleccionado = vistaTarjeta.mesTxt.getSelectedIndex();
        int añoSeleccionado = Integer.parseInt(añoExpiracion);
        
        if (mesExpiracion == "Mes" || añoExpiracion == "Año") {
            String[] opci = {"Aceptar"};
            JOptionPane.showOptionDialog(null, "Selecciona un mes y un año", "Pago", 0, JOptionPane.ERROR_MESSAGE, null, opci, "Aceptar");
            return false;
        }
        
        if (añoSeleccionado == añoActual && mesSeleccionado <= mesActual ) {
                System.out.println(añoSeleccionado);
                System.out.println(mesSeleccionado);
                System.out.println(añoActual);
                System.out.println(mesActual);
                String[] opci = {"Aceptar"};
                JOptionPane.showOptionDialog(null, "La fecha de caducidad es invalida", "Pago", 0, JOptionPane.ERROR_MESSAGE, null, opci, "Aceptar");
                return false;
            }
        return true;

    }
    
}
