package Controlador;

import Modelo.Reporte;
import Vista.ReporteV;
import com.itextpdf.text.DocumentException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author darina
 */
public class CtrlReportes implements ActionListener {

    private ReporteV vistaReporte = new ReporteV();
    private Reporte modeloReporte = new Reporte();

    public CtrlReportes() {
        this.vistaReporte.btnRegresar.addActionListener(this);
        this.vistaReporte.btnCrear.addActionListener(this);
        this.vistaReporte.periodoCombo.addActionListener(this);
    }

    public void iniciar() {
        vistaReporte.setVisible(true);
        vistaReporte.setTitle("Reportes");
        vistaReporte.setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaReporte.btnRegresar) {
            CtrlAdministracion admin = new CtrlAdministracion();
            admin.mostrarMenuPrincipal();
            vistaReporte.setVisible(false);
        }

        if (e.getSource() == vistaReporte.periodoCombo) {
            String selectedOption = (String) vistaReporte.periodoCombo.getSelectedItem();
            // Verificamos la opción seleccionada y desactivamos el botón si es necesario
            if (selectedOption.equals("Diario")) {
                vistaReporte.mesCombo.setEnabled(false);
            } else {
                vistaReporte.mesCombo.setEnabled(true);
            }
        }

        if (e.getSource() == vistaReporte.btnCrear) {
            //Reporte mensual 
            String tipoReporte = (String) vistaReporte.tipoCombo.getSelectedItem();
            String periodo = (String) vistaReporte.periodoCombo.getSelectedItem();
         
            if (tipoReporte.equals("Informe de ventas general")) {
                if (periodo == "Diario") {
                    System.out.println("Creando reporte diario del informe general");
                    
                    // Obtener la fecha actual
                    Date fechaActual = new Date();
                    // Formatear la fecha como una cadena de texto
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); // El formato que desees
                    String fecha = formato.format(fechaActual);
                    
                    try {
                        //Creamos el reporte
                        modeloReporte.generarReporteDiario(fecha);
                    } catch (FileNotFoundException | DocumentException ex) {
                        Logger.getLogger(CtrlReportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else {
                    //Mensual
                    System.out.println("Creando reporte mensual del informe general");

                    int mesSeleccionado = vistaReporte.mesCombo.getSelectedIndex() + 1;

                    try {
                        modeloReporte.generarReporteMensual(mesSeleccionado);
                    } catch (FileNotFoundException | DocumentException ex) {
                        Logger.getLogger(CtrlReportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                //Informe de productos mas vendidos
                if (periodo == "Diario") {
                    System.out.println("Creando reporte diario del informe general de productos mas vendidos");
                    
                     // Obtener la fecha actual
                    Date fechaActual = new Date();
                    // Formatear la fecha como una cadena de texto
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); // El formato que desees
                    String fecha = formato.format(fechaActual);
                    
                    try {
                        //Creamos el reporte
                        modeloReporte.generarReporteMasVendidosPorDia(fecha);
                    } catch (FileNotFoundException | DocumentException ex) {
                        Logger.getLogger(CtrlReportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //Mensual
                    System.out.println("Creando reporte mensual del informe general de productos mas vendidos");

                    int mesSeleccionado = vistaReporte.mesCombo.getSelectedIndex() + 1;

                    try {
                        modeloReporte.generarReporteMasVendidos(mesSeleccionado);
                    } catch (FileNotFoundException | DocumentException ex) {
                        Logger.getLogger(CtrlReportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            
            //Notificacion 
            
            String[] opciones = {"Aceptar"};
            JOptionPane.showOptionDialog(null, "El reporte se ha creado correctamente", "Reporte", 0, JOptionPane.INFORMATION_MESSAGE, null, opciones, "Aceptar");
            
        }

    }

}
