package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author darina
 */

public class ConexionDB {
    private static final String url = "jdbc:mysql://localhost:3306/proyectocelulares";
    private static final String usuario = "root";
    private static final String contrasenia = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasenia);
    }
}

