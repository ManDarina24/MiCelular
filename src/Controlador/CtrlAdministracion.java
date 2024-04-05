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

public class CtrlAdministracion implements ActionListener{
    
    private final Administracion modeloAdmin = new Administracion();
    private final Login vistaLogin = new Login();
    private final MenuPrincipal vistaMenu = new MenuPrincipal();
    private final MenuInventario vistaInv = new MenuInventario();
    
    CtrlAdministracion() {
        this.vistaLogin.btnIngresar.addActionListener(this);
        this.vistaMenu.btnInventario.addActionListener(this);
    }
    
    public void iniciar(){
        vistaLogin.setTitle("Login");
        vistaLogin.setVisible(true);
        vistaLogin.setLocationRelativeTo(null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaLogin.btnIngresar) {
            String usuario = vistaLogin.getUsuarioTXT().getText();
            String contrasenia = vistaLogin.getContraseniaTXT().getText();

            if (!usuario.isEmpty() && !contrasenia.isEmpty()) {
                boolean autenticado = modeloAdmin.verificaDatos(usuario, contrasenia);
                if (autenticado) {
                    System.out.println("¡Usuario autenticado correctamente!");
                    vistaLogin.setVisible(false);
                    vistaMenu.setVisible(true);
                    vistaMenu.setTitle("Menu principal");
                    vistaMenu.setLocationRelativeTo(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Datos faltantes, llena todos los campos");
            }
            
        }
        
        if (e.getSource() == vistaMenu.btnInventario){
            vistaMenu.setVisible(false);
            CtrlInventario ctrlInventario = new CtrlInventario();
            ctrlInventario.iniciar();
        }

    }
    
    
    
    
}