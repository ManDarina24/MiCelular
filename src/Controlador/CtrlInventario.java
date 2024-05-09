package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author darina
 */
public class CtrlInventario implements ActionListener {

    private final MenuInventario vistaInv = new MenuInventario();
    private final AgregaProducto vistaAgregar = new AgregaProducto();
    private final ModificaProductos vistaModificar = new ModificaProductos();
    private final EliminaProducto vistaElimina = new EliminaProducto();
    private final Inventario modeloInventario = new Inventario();
    private final Catalogo vistaCatalogo = new Catalogo();
    private final CtrlAdministracion admin = new CtrlAdministracion();

    public CtrlInventario() {
        this.vistaInv.btnAgregar.addActionListener(this);
        this.vistaAgregar.btnAgregaInventario.addActionListener(this);
        this.vistaInv.btnModificar.addActionListener(this);
        this.vistaModificar.btnBuscar.addActionListener(this);
        this.vistaModificar.btnModificaInventario.addActionListener(this);
        this.vistaInv.btnEliminar.addActionListener(this);
        this.vistaElimina.btnBuscar.addActionListener(this);
        this.vistaElimina.btnEliminaInventario.addActionListener(this);
        this.vistaInv.btnVerProductos.addActionListener(this);
        this.vistaInv.btnRegresar.addActionListener(this);
        this.vistaModificar.btnRegresar.addActionListener(this);
        this.vistaElimina.btnRegresar.addActionListener(this);
        this.vistaAgregar.btnRegresar.addActionListener(this);
        this.vistaCatalogo.btnRegresar.addActionListener(this);
    }

    public void iniciar() {
        vistaInv.setVisible(true);
        vistaInv.setTitle("Menu inventario");
        vistaInv.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Vista Inventario
        if (e.getSource() == vistaInv.btnRegresar) {
            vistaInv.setVisible(false);
            admin.mostrarMenuPrincipal();
        }
        if (e.getSource() == vistaElimina.btnRegresar) {
            vistaElimina.setVisible(false);
            iniciar();
        }
        if (e.getSource() == vistaModificar.btnRegresar) {
            vistaModificar.setVisible(false);
            iniciar();
        }
        if (e.getSource() == vistaAgregar.btnRegresar) {
            vistaAgregar.setVisible(false);
            iniciar();
        }
        if (e.getSource() == vistaCatalogo.btnRegresar) {
            vistaCatalogo.setVisible(false);
            iniciar();

        }

        if (e.getSource() == vistaInv.btnAgregar) {
            vistaInv.setVisible(false);
            vistaAgregar.setVisible(true);
            vistaAgregar.setTitle("Agregar producto");
            vistaAgregar.setLocationRelativeTo(null);
            
            SpinnerNumberModel modeloSpinner = (SpinnerNumberModel) vistaAgregar.stockTxt.getModel();
            modeloSpinner.setMinimum(0);
            modeloSpinner.setValue(1);
            vistaAgregar.stockTxt.setModel(modeloSpinner);
        }

        if (e.getSource() == vistaInv.btnModificar) {
            vistaInv.setVisible(false);
            vistaModificar.setVisible(true);
            vistaModificar.setTitle("Modificar productos");
            vistaModificar.setLocationRelativeTo(null);
            
            SpinnerNumberModel modeloSpinner = (SpinnerNumberModel) vistaModificar.stockTxt.getModel();
            modeloSpinner.setMinimum(0);
            modeloSpinner.setValue(0);
            vistaModificar.stockTxt.setModel(modeloSpinner);
        }
        //
        if (e.getSource() == vistaInv.btnEliminar) {
            vistaInv.setVisible(false);
            vistaElimina.setVisible(true);
            vistaElimina.setTitle("Elimina productos");
            vistaElimina.setLocationRelativeTo(null);
        }

        if (e.getSource() == vistaInv.btnVerProductos) {
            vistaInv.setVisible(false);
            vistaCatalogo.setVisible(true);
            vistaCatalogo.setTitle("Catalogo");
            vistaCatalogo.setLocationRelativeTo(null);
            vistaCatalogo.tabla.setModel(listar(vistaCatalogo.tabla));
        }

        if (e.getSource() == vistaElimina.btnBuscar) {
            String strFolio = vistaElimina.getFolioTxt().getText();
            if (!strFolio.isEmpty() && strFolio.matches("-?\\d+(\\.\\d+)?")) {
                int folio = Integer.parseInt(vistaElimina.getFolioTxt().getText());

                if (modeloInventario.buscaProducto(folio) != null) {
                    //El inventario nos regresa un Producto, entonces eso lo asignamos a las casillas para que el 
                    //usuario pueda observar el producto
                    Producto producto = modeloInventario.buscaProducto(folio);
                    vistaElimina.getIdLbl().setText(String.valueOf(producto.getId()));
                    vistaElimina.getMarcaLbl().setText("Marca: " + producto.getMarca());
                    vistaElimina.getModeloLbl().setText("Modelo: " + producto.getModelo());
                    vistaElimina.getPantallaLbl().setText("Pantalla: " + producto.getPantalla());
                    vistaElimina.getAlmLbl().setText("Almacenamiento: " + producto.getAlmacenamiento());
                    vistaElimina.getCamaraLbl().setText("Camara: " + producto.getCamara());
                    vistaElimina.getRamLbl().setText("Ram: " + producto.getRam());
                    vistaElimina.getStockLbl().setText("Cantidad disponible: " + String.valueOf(producto.getCantidad()));
                    vistaElimina.getPrecioLbl().setText("Precio: " + String.valueOf(producto.getPrecio()));

                } else {
                    String[] opciones = {"Aceptar"};
                    JOptionPane.showOptionDialog(null, "Producto no encontrado, intentelo de nuevo", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

                    limpiarVistaEliminar();
                }
            }
        }

        if (e.getSource() == vistaElimina.btnEliminaInventario) {
            String StrFolio = vistaElimina.getIdLbl().getText();
            if (!StrFolio.isEmpty() && StrFolio.matches("-?\\d+(\\.\\d+)?")) {

                //Confirmacion
                String[] res = {"Aceptar", "Cancelar"};
                int respuesta = JOptionPane.showOptionDialog(null, "¿Deseas eliminar este producto del inventario?", "Inventario", 0, JOptionPane.INFORMATION_MESSAGE, null, res, "Aceptar");

                if (respuesta == 0) {
                    int folio = Integer.parseInt(StrFolio);
                    if (modeloInventario.eliminaProducto(folio)) {
                        String[] opciones = {"Aceptar"};
                        JOptionPane.showOptionDialog(null, "El producto ha sido eliminado del inventario", "Inventario", 0, JOptionPane.INFORMATION_MESSAGE, null, opciones, "Aceptar");

                        limpiarVistaEliminar();
                    }
                }

            } else {
                String[] opciones = {"Aceptar"};
                JOptionPane.showOptionDialog(null, "Para eliminar un producto, primero busca uno", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

            }

        }

        if (e.getSource() == vistaModificar.btnBuscar) {

            String strFolio = vistaModificar.getFolioTxt().getText();
            if (!strFolio.isEmpty() && strFolio.matches("-?\\d+(\\.\\d+)?")) {
                int folio = Integer.parseInt(vistaModificar.getFolioTxt().getText());

                if (modeloInventario.buscaProducto(folio) != null) {
                    //El inventario nos regresa in Producto, entonces eso lo asignamos a las casillas para que el 
                    //usuario pueda modificarlo despues 
                    vistaModificar.getIdLbl().setText(strFolio);
                    Producto producto = modeloInventario.buscaProducto(folio);
                    vistaModificar.getMarcaTxt().setText(producto.getMarca());
                    vistaModificar.getModeloTxt().setText(producto.getModelo());
                    vistaModificar.getPantallaTxt().setText(producto.getPantalla());
                    vistaModificar.getAlmTxt().setText(producto.getAlmacenamiento());
                    vistaModificar.getCamaraTxt().setText(producto.getCamara());
                    vistaModificar.getRamTxt().setText(producto.getRam());
                    vistaModificar.getStockTxt().setValue(producto.getCantidad());
                    vistaModificar.getPrecioTxt().setText(String.valueOf(producto.getPrecio()));
                } else {
                    String[] opciones = {"Aceptar"};
                    JOptionPane.showOptionDialog(null, "Producto no encontrado, intentelo de nuevo", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

                    limpiarVistaModificar();
                }
            }
        }

        if (e.getSource() == vistaModificar.btnModificaInventario) {
            String marca = vistaModificar.getMarcaTxt().getText();
            String modelo = vistaModificar.getModeloTxt().getText();
            String pantalla = vistaModificar.getPantallaTxt().getText();
            String camara = vistaModificar.getCamaraTxt().getText();
            String almacenamiento = vistaModificar.getAlmTxt().getText();
            String ram = vistaModificar.getRamTxt().getText();
            String precioStr = vistaModificar.getPrecioTxt().getText();
            String stockStr = vistaModificar.getStockTxt().getValue().toString();
            
            //Confirmar
            String[] res = {"Aceptar", "Cancelar"};
            int respuesta = JOptionPane.showOptionDialog(null, "¿Deseas modificar este producto del inventario?", "Inventario", 0, JOptionPane.INFORMATION_MESSAGE, null, res, "Aceptar");

            if (respuesta == 0) {
                if (validaDatos(marca, modelo, pantalla, camara, almacenamiento, ram, precioStr, stockStr)) {
                    int id = Integer.parseInt(vistaModificar.getIdLbl().getText());
                    int stock = Integer.parseInt(stockStr);
                    double precio = Double.parseDouble(precioStr);

                    boolean aux1 = modeloInventario.modificaProducto(id, marca, modelo, pantalla, camara, almacenamiento, ram, stock, precio);

                    if (aux1) {
                        String[] opciones = {"Aceptar"};
                        JOptionPane.showOptionDialog(null, "El producto ha sido modificado", "Inventario", 0, JOptionPane.INFORMATION_MESSAGE, null, opciones, "Aceptar");

                        limpiarVistaModificar();
                    }

                } else {
                    String[] opciones = {"Aceptar"};
                    JOptionPane.showOptionDialog(null, "Los datos no son validos o hay campos vacios, intentelo de nuevo", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

                }

            }
            

        }

        if (e.getSource() == vistaAgregar.btnAgregaInventario) {

            String marca = vistaAgregar.getMarcaTxt().getText();
            String modelo = vistaAgregar.getModeloTxt().getText();
            String pantalla = vistaAgregar.getPantallaTxt().getText();
            String camara = vistaAgregar.getCamaraTxt().getText();
            String almacenamiento = vistaAgregar.getAlmTxt().getText();
            String ram = vistaAgregar.getRamTxt().getText();
            String precioStr = vistaAgregar.getPrecioTxt().getText();
            String stockStr = vistaAgregar.getStockTxt().getValue().toString();
            
            
            //Confirmacion
            String[] res = {"Aceptar", "Cancelar"};
            int respuesta = JOptionPane.showOptionDialog(null, "¿Deseas modificar este producto del inventario?", "Inventario", 0, JOptionPane.INFORMATION_MESSAGE, null, res, "Aceptar");

            if (respuesta == 0) {
                //valida los datos 
                if (validaDatos(marca, modelo, pantalla, camara, almacenamiento, ram, precioStr, stockStr)) {
                    //Asignar valores recibidos a variables 

                    double precio = Double.parseDouble(precioStr);
                    int stock = Integer.parseInt(stockStr);

                    //Agregamos los datos a la bd 
                    if (modeloInventario.agregaProducto(marca, modelo, pantalla, camara, almacenamiento, ram, precio, stock)) {

                        String[] opciones = {"Aceptar"};
                        JOptionPane.showOptionDialog(null, "El producto ha sido agregado", "Inventario", 0, JOptionPane.INFORMATION_MESSAGE, null, opciones, "Aceptar");

                        limpiarVistaAgregar();
                    }

                } else {
                    String[] opciones = {"Aceptar"};
                    JOptionPane.showOptionDialog(null, "Los datos no son validos o hay campos vacios, intentelo de nuevo", "Inventario", 0, JOptionPane.ERROR_MESSAGE, null, opciones, "Aceptar");

                }
            }
            
        }
    }

    private boolean validaDatos(String marca, String modelo, String pantalla, String camara, String almacenamiento, String ram, String precio, String stock) {
        int contador = 0;
        boolean bandera = false;
        if (!marca.isEmpty()) {
            //Marca
            contador++;
        }
        if (!modelo.isEmpty()) {
            //Modelo
            contador++;
        }
        if (!pantalla.isEmpty()) {
            //Pantalla
            contador++;
        }
        if (!camara.isEmpty()) {
            //Camara
            contador++;
        }
        if (!almacenamiento.isEmpty()) {
            //Almacenamiento
            contador++;
        }
        if (!ram.isEmpty()) {
            //Almacenamiento
            contador++;
        }

        if (!precio.isEmpty() && !stock.isEmpty()) {
            if (precio.matches("-?\\d+(\\.\\d+)?") && stock.matches("-?\\d+(\\.\\d+)?")) {
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
        vistaModificar.getIdLbl().setText("-");
    }

    private void limpiarVistaEliminar() {
        vistaElimina.getIdLbl().setText("-");
        vistaElimina.getFolioTxt().setText("");
        vistaElimina.getMarcaLbl().setText("Marca:");
        vistaElimina.getModeloLbl().setText("Modelo:");
        vistaElimina.getPantallaLbl().setText("Pantalla:");
        vistaElimina.getAlmLbl().setText("Almacenamiento:");
        vistaElimina.getCamaraLbl().setText("Camara:");
        vistaElimina.getRamLbl().setText("Ram:");
        vistaElimina.getStockLbl().setText("Cantidad disponible:");
        vistaElimina.getPrecioLbl().setText("Precio:");
    }

    private DefaultTableModel listar(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        List<Producto> lista = modeloInventario.listar();
        Object[] object = new Object[9];

        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getMarca();
            object[2] = lista.get(i).getModelo();
            object[3] = lista.get(i).getPantalla();
            object[4] = lista.get(i).getCamara();
            object[5] = lista.get(i).getAlmacenamiento();
            object[6] = lista.get(i).getRam();
            object[7] = lista.get(i).getPrecio();
            object[8] = lista.get(i).getCantidad();
            modelo.addRow(object);
        }
        return modelo;

    }
}
