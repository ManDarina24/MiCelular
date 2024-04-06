package Modelo;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author darina
 */
public class Inventario {

    public boolean agregaProducto(String marca, String modelo, String pantalla, String camara, String almacenamiento, String ram, double precio, int stock) {
        int folio = 0;
        String insertProductoSQL = "INSERT INTO producto (marca, modelo, pantalla, camara, almacenamiento, ram) VALUES (?, ?, ?, ?, ?, ?)";
        String insertInventarioSQL = "INSERT INTO inventario (id_Producto, stock, precio) VALUES (?, ?, ?)";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmtProducto = conn.prepareStatement(insertProductoSQL, Statement.RETURN_GENERATED_KEYS);  PreparedStatement pstmtInventario = conn.prepareStatement(insertInventarioSQL)) {

            // Insertar datos del producto
            pstmtProducto.setString(1, marca);
            pstmtProducto.setString(2, modelo);
            pstmtProducto.setString(3, pantalla);
            pstmtProducto.setString(4, camara);
            pstmtProducto.setString(5, almacenamiento);
            pstmtProducto.setString(6, ram);

            if (pstmtProducto.executeUpdate() == 1) {
                // Obtener el folio del producto insertado
                try ( ResultSet rs = pstmtProducto.getGeneratedKeys()) {
                    if (rs.next()) {
                        folio = rs.getInt(1); // Retorna el valor de la primera columna generada automáticamente
                    }
                }

                // Insertar datos en el inventario
                pstmtInventario.setInt(1, folio);
                pstmtInventario.setInt(2, stock);
                pstmtInventario.setDouble(3, precio);

                if (pstmtInventario.executeUpdate() == 1) {
                    // Si se insertó correctamente en el inventario, marcar la bandera como verdadera
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Producto buscaProducto(int folio) {

        Producto producto = null;
        String sql = "SELECT p.id, p.marca, p.modelo, p.pantalla, p.camara, p.almacenamiento, p.ram, i.stock, i.precio "
                + "FROM Producto p JOIN Inventario i ON p.id = i.id_Producto WHERE p.id = ?;";
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, folio);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                producto = new Producto(rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("pantalla"),
                        rs.getString("almacenamiento"),
                        rs.getString("camara"),
                        rs.getString("ram"),
                        rs.getInt("stock"),
                        rs.getDouble("precio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return producto;
    }

    public boolean eliminaProducto(int folio) {
        boolean bandera = false;
        String sqlInventario = "DELETE FROM inventario WHERE id_Producto = ?";
        String sqlProducto = "DELETE FROM producto WHERE id = ?";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmtInv = conn.prepareStatement(sqlInventario);  PreparedStatement pstmtProd = conn.prepareStatement(sqlProducto)) {

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

    public boolean modificaProducto(int folio, String nuevaMarca, String nuevoModelo, String nuevaPantalla, String nuevaCamara, String nuevoAlmacenamiento, String nuevoRam, int nuevoStock, double nuevoPrecio) {
        boolean bandera = false;
        String updateProductoSQL = "UPDATE Producto SET marca = ?, modelo = ?, pantalla = ?, camara = ?, almacenamiento = ?, ram = ? WHERE id = ?";
        String updateInventarioSQL = "UPDATE Inventario SET stock = ?, precio = ? WHERE id_Producto = ?";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmtProducto = conn.prepareStatement(updateProductoSQL);  PreparedStatement pstmtInventario = conn.prepareStatement(updateInventarioSQL)) {

            // Actualizar datos del producto
            pstmtProducto.setString(1, nuevaMarca);
            pstmtProducto.setString(2, nuevoModelo);
            pstmtProducto.setString(3, nuevaPantalla);
            pstmtProducto.setString(4, nuevaCamara);
            pstmtProducto.setString(5, nuevoAlmacenamiento);
            pstmtProducto.setString(6, nuevoRam);
            pstmtProducto.setInt(7, folio);

            // Actualizar datos del inventario
            pstmtInventario.setInt(1, nuevoStock);
            pstmtInventario.setDouble(2, nuevoPrecio);
            pstmtInventario.setInt(3, folio);

            boolean productoActualizado = pstmtProducto.executeUpdate() == 1;
            boolean inventarioActualizado = pstmtInventario.executeUpdate() == 1;

            bandera = productoActualizado && inventarioActualizado;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bandera;
    }

    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.id, p.marca, p.modelo, p.pantalla, p.camara, p.almacenamiento, p.ram, i.stock, i.precio "
                + "FROM Producto p JOIN Inventario i ON p.id = i.id_Producto";
        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto(rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("pantalla"),
                        rs.getString("almacenamiento"),
                        rs.getString("camara"),
                        rs.getString("ram"),
                        rs.getInt("stock"),
                        rs.getDouble("precio"));

                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return productos;
    }

}
