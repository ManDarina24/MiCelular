package Modelo;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author darina
 */
public class Inventario {
    
    
    public int agregaDatosProducto(String marca, String modelo, String pantalla, String camara, String almacenamiento, String ram) {
        int folio = 0;
        String sql = "INSERT INTO producto (marca, modelo, pantalla, camara, almacenamiento, ram) VALUES (?, ?, ?, ?, ?, ?)";
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, marca);
            pstmt.setString(2, modelo);
            pstmt.setString(3, pantalla);
            pstmt.setString(4, camara);
            pstmt.setString(5, almacenamiento);
            pstmt.setString(6, ram);

            if (pstmt.executeUpdate() == 1) {
                //Obtenemos el folio
                try ( ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        folio = rs.getInt(1); // Retorna el valor de la primera columna generada automáticamente
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return folio;
    }
    
    public boolean agregaProductoInventario(int folio, double precio, int stock){
        boolean bandera = false;
        String sql = "INSERT INTO inventario (id_Producto, stock, precio) VALUES (?, ?, ?)";
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, folio);
            pstmt.setInt(2, stock);
            pstmt.setDouble(3, precio);
            

            if (pstmt.executeUpdate() == 1) {
                //Obtenemos el folio
                bandera = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return bandera;
    }
    
    public boolean buscaProducto(int folio){
        boolean bandera = false;
        String sql = "SELECT COUNT(*) AS cantidad FROM producto WHERE id = ?;";
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, folio);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("cantidad") > 0){
                    bandera = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return bandera;
    }
    
    public boolean eliminaProducto(int folio){
        boolean bandera = false;
        String sqlInventario = "DELETE FROM inventario WHERE id_Producto = ?";
        String sqlProducto = "DELETE FROM producto WHERE id = ?";
        
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmtInv = conn.prepareStatement(sqlInventario);
                PreparedStatement pstmtProd = conn.prepareStatement(sqlProducto)) {

            pstmtInv.setInt(1, folio);
            pstmtProd.setInt(1, folio);

            if (pstmtInv.executeUpdate() == 1 && pstmtProd.executeUpdate() == 1) {
                bandera = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return bandera;
    }
    
    public boolean modificaProducto(int folio, String nuevaMarca, String nuevoModelo, String nuevaPantalla, String nuevaCamara, String nuevoAlmacenamiento, String nuevoRam){
        boolean bandera = false;
        String sql = "UPDATE Producto SET marca = ?, modelo = ?, pantalla = ?, camara = ?, almacenamiento = ?, ram = ? WHERE id = ?";
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaMarca);
            pstmt.setString(2, nuevoModelo);
            pstmt.setString(3, nuevaPantalla);
            pstmt.setString(4, nuevaCamara);
            pstmt.setString(5, nuevoAlmacenamiento);
            pstmt.setString(6, nuevoRam);
            pstmt.setInt(7, folio);

            if (pstmt.executeUpdate() == 1) {
                //Obtenemos el folio
                bandera = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bandera;
    }
    
    public boolean modificaInventario(int id_producto, int stock, double precio){
        boolean bandera = false;
        String sql = "UPDATE Inventario SET stock = ?, precio = ? WHERE id_Producto = ?";
        
        try (Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, stock);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, id_producto);
            
            if (pstmt.executeUpdate() == 1) {
                //Obtenemos el folio
                bandera = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bandera;
    }
}
