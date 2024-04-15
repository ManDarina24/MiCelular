package Modelo;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author jocel
 */
public class Venta {
   
    public Venta(){}
      
    public int registraCliente(Cliente cliente) {
    int id = 0;
    String insertClienteSQL = "INSERT INTO Cliente (nombre, apellidoPaterno, apellidoMaterno, telefono, correo) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = ConexionDB.conectar();
         PreparedStatement pstmtCliente = conn.prepareStatement(insertClienteSQL, Statement.RETURN_GENERATED_KEYS)) {

        // Insertar datos del cliente
        pstmtCliente.setString(1, cliente.nombre);
        pstmtCliente.setString(2, cliente.apellidoPaterno);
        pstmtCliente.setString(3, cliente.apellidoMaterno);
        pstmtCliente.setLong(4, cliente.telefono);
        pstmtCliente.setString(5, cliente.correo);

        if (pstmtCliente.executeUpdate() == 1) {
            // Obtener el ID del cliente insertado
            try (ResultSet rs = pstmtCliente.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1); // Retorna el valor de la primera columna generada automáticamente
                }
            }
            
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return id;
}

    public boolean registraVenta(int idCliente, List<Producto> productos, double total, MetodoPago metodoPago, String usuarioResp) {
        String insertVentaSQL = "INSERT INTO RegistroVenta (id_Cliente, fechaVenta, total, metodoPago, usuarioResp) VALUES (?,  CURRENT_TIMESTAMP, ?, ?, ?)";
        String insertDetalleVentaSQL = "INSERT INTO DetalleVenta (folio_Venta, id_Producto, cantidadVendida, subTotal) VALUES (?, ?, ?, ?)";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmtVenta = conn.prepareStatement(insertVentaSQL, Statement.RETURN_GENERATED_KEYS);  PreparedStatement pstmtDetalleVenta = conn.prepareStatement(insertDetalleVentaSQL)) {

            // Insertar datos de la venta
            pstmtVenta.setInt(1, idCliente);
            pstmtVenta.setDouble(2, total);
            pstmtVenta.setString(3, metodoPago.name()); // Convertir el enum a String
            pstmtVenta.setString(4, usuarioResp);

            // Ejecutar la inserción de la venta
            if (pstmtVenta.executeUpdate() == 1) {
                // Obtener el folio de la venta insertada
                int folioVenta;
                try ( ResultSet rs = pstmtVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        folioVenta = rs.getInt(1); // Obtener el folio de la venta
                    } else {
                        throw new SQLException("No se pudo obtener el folio de la venta.");
                    }
                }

                // Insertar los detalles de la venta
                for (Producto producto : productos) {
                    // Insertar detalle de venta por cada producto
                    pstmtDetalleVenta.setInt(1, folioVenta);
                    pstmtDetalleVenta.setInt(2, producto.id);
                    pstmtDetalleVenta.setInt(3, producto.cantidad);
                    pstmtDetalleVenta.setDouble(4, producto.precio); // Precio unitario 
                    pstmtDetalleVenta.addBatch();
                }

                // Ejecutar el lote de inserciones de detalles de venta
                int[] resultados = pstmtDetalleVenta.executeBatch();
                // Verificar si todas las inserciones de detalles de venta fueron exitosas
                for (int resultado : resultados) {
                    if (resultado != 1) {
                        throw new SQLException("Error al insertar detalles de la venta.");
                    }
                }

                return true; // La venta y sus detalles se registraron correctamente
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Ocurrió un error al registrar la venta y/o sus detalles
    }

    public enum MetodoPago {
        tarjeta,
        efectivo
    }
 
}
