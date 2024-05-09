package Modelo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reporte {

    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        Reporte reporte = new Reporte();
        //reporte.generarReporteMensual(5); // Cambia "5" al número de mes que desees generar el reporte
        //reporte.generarReporteDiario("2024-05-05");
        //reporte.generarReporteMasVendidos(4);
        reporte.generarReporteMasVendidosPorDia("2024-05-05");
    }

    public void generarReporteMensual(int mes) throws FileNotFoundException, DocumentException {
        // Consulta SQL para obtener datos de ventas filtrados por mes
        String sql = "SELECT RegistroVenta.folio, CONCAT(Cliente.nombre, ' ', Cliente.apellidoPaterno, ' ', Cliente.apellidoMaterno) AS nombreCompleto, RegistroVenta.fechaVenta, RegistroVenta.total, RegistroVenta.metodoPago, RegistroVenta.usuarioResp FROM RegistroVenta INNER JOIN Cliente ON RegistroVenta.id_Cliente = Cliente.id WHERE MONTH(RegistroVenta.fechaVenta) = ?";
        String sqlTotal = "SELECT SUM(total) AS totalMes FROM RegistroVenta WHERE MONTH(fechaVenta) = ?";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql);  PreparedStatement pstmtTotal = conn.prepareStatement(sqlTotal)) {
            pstmt.setInt(1, mes); // Asignar el parámetro del mes
            pstmtTotal.setInt(1, mes); // Asignar el parámetro del mes

            try ( ResultSet rs = pstmt.executeQuery();  ResultSet rsTotal = pstmtTotal.executeQuery()) {
                // Crear el documento PDF
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream("reporte_ventas_mes_" + mes + ".pdf"));

                // Abrir el documento
                document.open();

                Paragraph titulo = new Paragraph("Chispas, mi celular \n",
                        FontFactory.getFont("arial",
                                14,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                //Centramos el texto
                titulo.setAlignment(Paragraph.ALIGN_CENTER);

                // Añadimos el titulo al documento
                document.add(titulo);

                Paragraph info = new Paragraph("INFORME GENERAL DE VENTAS",
                        FontFactory.getFont("arial",
                                12,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                info.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(info);

                String strmes = "";
                switch (mes) {
                    case 1 ->
                        strmes = "ENERO";
                    case 2 ->
                        strmes = "FEBRERO";
                    case 3 ->
                        strmes = "MARZO";
                    case 4 ->
                        strmes = "ABRIL";
                    case 5 ->
                        strmes = "MAYO";
                    case 6 ->
                        strmes = "JUNIO";
                    case 7 ->
                        strmes = "JULIO";
                    case 8 ->
                        strmes = "AGOSTO";
                    case 9 ->
                        strmes = "SEPTIEMBRE";
                    case 10 ->
                        strmes = "OCTUBRE";
                    case 11 ->
                        strmes = "NOVIEMBRE";
                    case 12 ->
                        strmes = "DICIEMBRE";
                    default -> {
                    }
                }

                Paragraph mesPr = new Paragraph(strmes,
                        FontFactory.getFont("arial",
                                12,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                mesPr.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(mesPr);

                // Obtener la fecha y hora actual en la zona horaria de México
                ZonedDateTime horaMexico = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));

                // Verificar si la hora actual está en horario de verano
                ZoneOffset offset = horaMexico.getOffset();
                if (offset.getTotalSeconds() != ZoneOffset.UTC.getTotalSeconds()) {
                    // Si la hora actual está en horario de verano, ajustarla manualmente
                    horaMexico = horaMexico.withZoneSameInstant(ZoneOffset.ofTotalSeconds(offset.getTotalSeconds() - 3600)); // Ajustar una hora hacia atrás
                }

                // Formatear la fecha y hora según tu preferencia
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String horaFormateada = horaMexico.format(formato);

                Paragraph fechaYhora = new Paragraph("Fecha: " + horaFormateada + "\n\n",
                        FontFactory.getFont("arial",
                                12,
                                BaseColor.BLACK
                        )
                );
                fechaYhora.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(fechaYhora);

                // Crear tabla para datos de ventas
                PdfPTable table = new PdfPTable(5);
                table.setWidths(new float[]{1, 3, 2, 2, 2}); // Ajusta los anchos de las columnas
                table.setHeaderRows(1); // Marca la primera fila como encabezado

                // Encabezados de la tabla
                PdfPCell cell = new PdfPCell(new Paragraph("Folio"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Cliente"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Fecha Vendida"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Total Vendido"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Método de Pago"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                // Iterar sobre los resultados de la consulta y agregarlos a la tabla
                while (rs.next()) {
                    table.addCell(rs.getString("folio"));
                    table.addCell(rs.getString("nombreCompleto"));
                    table.addCell(rs.getString("fechaVenta"));
                    table.addCell(rs.getString("total"));
                    table.addCell(rs.getString("metodoPago"));

                }

                // Agregar la tabla al documento
                document.add(table);

                // Calcular el total final de todas las ventas del mes
                double totalMes = 0.0;
                if (rsTotal.next()) {
                    totalMes = rsTotal.getDouble("totalMes");
                }

                // Agregar el total final al documento
                Paragraph totalVentas = new Paragraph("\nTotal de ventas del mes: " + totalMes,
                        FontFactory.getFont("arial",
                                12,
                                BaseColor.BLACK
                        )
                );
                totalVentas.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(totalVentas);

                // Cerrar el documento
                document.close();

                System.out.println("El reporte se ha generado correctamente.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarReporteDiario(String fecha) throws FileNotFoundException, DocumentException {
        // Consulta SQL para obtener datos de ventas filtrados por fecha
        String sql = "SELECT RegistroVenta.folio, CONCAT(Cliente.nombre, ' ', Cliente.apellidoPaterno, ' ', Cliente.apellidoMaterno) AS nombreCompleto, RegistroVenta.fechaVenta, RegistroVenta.total, RegistroVenta.metodoPago FROM RegistroVenta INNER JOIN Cliente ON RegistroVenta.id_Cliente = Cliente.id WHERE DATE(RegistroVenta.fechaVenta) = ?";

        // Consulta SQL para calcular el total de ventas para el día
        String sqlTotal = "SELECT SUM(total) AS totalDia FROM RegistroVenta WHERE DATE(fechaVenta) = ?";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql);  PreparedStatement pstmtTotal = conn.prepareStatement(sqlTotal)) {

            pstmt.setString(1, fecha); // Asignar el parámetro de la fecha
            pstmtTotal.setString(1, fecha); // Asignar el parámetro de la fecha

            try ( ResultSet rs = pstmt.executeQuery();  ResultSet rsTotal = pstmtTotal.executeQuery()) {
                // Crear el documento PDF
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream("reporte_ventas_" + fecha + ".pdf"));

                // Abrir el documento
                document.open();

                Paragraph titulo = new Paragraph("Chispas, mi celular \n",
                        FontFactory.getFont("arial",
                                14,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                //Centramos el texto
                titulo.setAlignment(Paragraph.ALIGN_CENTER);

                // Añadimos el titulo al documento
                document.add(titulo);

                Paragraph info = new Paragraph("INFORME GENERAL DE VENTAS\n" + fecha + "\n\n",
                        FontFactory.getFont("arial",
                                12,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                info.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(info);

                // Crear tabla para datos de ventas
                PdfPTable table = new PdfPTable(5);
                table.setWidths(new float[]{1, 3, 2, 2, 2}); // Ajusta los anchos de las columnas
                table.setHeaderRows(1); // Marca la primera fila como encabezado

                // Encabezados de la tabla
                PdfPCell cell = new PdfPCell(new Paragraph("Folio de Venta"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Cliente"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Fecha Vendida"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Total Vendido"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Método de Pago"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                // Iterar sobre los resultados de la consulta y agregarlos a la tabla
                while (rs.next()) {
                    table.addCell(rs.getString("folio"));
                    table.addCell(rs.getString("nombreCompleto"));
                    table.addCell(rs.getString("fechaVenta"));
                    table.addCell(rs.getString("total"));
                    table.addCell(rs.getString("metodoPago"));
                }

                // Agregar la tabla al documento
                document.add(table);

                // Calcular el total de ventas para el día
                double totalDia = 0.0;
                if (rsTotal.next()) {
                    totalDia = rsTotal.getDouble("totalDia");
                }

                // Agregar el total de ventas al documento
                Paragraph totalVentas = new Paragraph("\nTotal de ventas del mes: " + totalDia,
                        FontFactory.getFont("arial",
                                12,
                                BaseColor.BLACK
                        )
                );
                totalVentas.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(totalVentas);

                // Cerrar el documento
                document.close();

                System.out.println("El reporte se ha generado correctamente.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarReporteMasVendidos(int mes) throws FileNotFoundException, DocumentException {
        // Consulta SQL para obtener los productos más vendidos para el mes especificado
        String sql = "SELECT Producto.id, Producto.marca, Producto.modelo, Producto.pantalla, Producto.camara, Producto.almacenamiento, Producto.ram, SUM(DetalleVenta.cantidadVendida) AS cantidadTotalVendida "
                + "FROM DetalleVenta "
                + "INNER JOIN RegistroVenta ON DetalleVenta.folio_Venta = RegistroVenta.folio "
                + "INNER JOIN Producto ON DetalleVenta.id_producto = Producto.id "
                + "WHERE MONTH(RegistroVenta.fechaVenta) = ? "
                + "GROUP BY Producto.id, Producto.marca, Producto.modelo, Producto.pantalla, Producto.camara, Producto.almacenamiento, Producto.ram "
                + "ORDER BY cantidadTotalVendida DESC";

        try ( Connection conn = ConexionDB.conectar();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, mes); // Asignar el parámetro del mes

            try ( ResultSet rs = pstmt.executeQuery()) {
                // Crear el documento PDF
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream("reporte_productos_mas_vendidos_mes_" + mes + ".pdf"));

                // Abrir el documento
                document.open();
                Paragraph titulo = new Paragraph("Chispas, mi celular \n",
                        FontFactory.getFont("arial",
                                14,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                //Centramos el texto
                titulo.setAlignment(Paragraph.ALIGN_CENTER);

                // Añadimos el titulo al documento
                document.add(titulo);

                Paragraph info = new Paragraph("INFORME GENERAL DE LOS PRODUCTOS MAS VENDIDOS",
                        FontFactory.getFont("arial",
                                12,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                info.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(info);

                String strmes = "";
                switch (mes) {
                    case 1 ->
                        strmes = "ENERO";
                    case 2 ->
                        strmes = "FEBRERO";
                    case 3 ->
                        strmes = "MARZO";
                    case 4 ->
                        strmes = "ABRIL";
                    case 5 ->
                        strmes = "MAYO";
                    case 6 ->
                        strmes = "JUNIO";
                    case 7 ->
                        strmes = "JULIO";
                    case 8 ->
                        strmes = "AGOSTO";
                    case 9 ->
                        strmes = "SEPTIEMBRE";
                    case 10 ->
                        strmes = "OCTUBRE";
                    case 11 ->
                        strmes = "NOVIEMBRE";
                    case 12 ->
                        strmes = "DICIEMBRE";
                    default -> {
                    }
                }

                Paragraph mesPr = new Paragraph(strmes,
                        FontFactory.getFont("arial",
                                12,
                                Font.BOLD,
                                BaseColor.BLACK
                        )
                );
                mesPr.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(mesPr);
                
                // Obtener la fecha y hora actual en la zona horaria de México
                ZonedDateTime horaMexico = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));

                // Verificar si la hora actual está en horario de verano
                ZoneOffset offset = horaMexico.getOffset();
                if (offset.getTotalSeconds() != ZoneOffset.UTC.getTotalSeconds()) {
                    // Si la hora actual está en horario de verano, ajustarla manualmente
                    horaMexico = horaMexico.withZoneSameInstant(ZoneOffset.ofTotalSeconds(offset.getTotalSeconds() - 3600)); // Ajustar una hora hacia atrás
                }

                // Formatear la fecha y hora según tu preferencia
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String horaFormateada = horaMexico.format(formato);

                Paragraph fechaYhora = new Paragraph("Fecha: " + horaFormateada + "\n\n",
                        FontFactory.getFont("arial",
                                12,
                                BaseColor.BLACK
                        )
                );
                fechaYhora.setAlignment(Paragraph.ALIGN_RIGHT);
                document.add(fechaYhora);


                // Crear tabla para datos de productos más vendidos
                PdfPTable table = new PdfPTable(8);
                table.setWidths(new float[]{1, 2, 2, 2, 2, 2, 2, 2}); // Ajusta los anchos de las columnas
                table.setHeaderRows(1); // Marca la primera fila como encabezado

                // Encabezados de la tabla
                PdfPCell cell = new PdfPCell(new Paragraph("ID"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Marca"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Modelo"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Pantalla"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Cámara"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Almacenamiento"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("RAM"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Cantidad vendida"));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);

                // Iterar sobre los resultados de la consulta y agregarlos a la tabla
                while (rs.next()) {
                    table.addCell(rs.getString("id"));
                    table.addCell(rs.getString("marca"));
                    table.addCell(rs.getString("modelo"));
                    table.addCell(rs.getString("pantalla"));
                    table.addCell(rs.getString("camara"));
                    table.addCell(rs.getString("almacenamiento"));
                    table.addCell(rs.getString("ram"));
                    table.addCell(rs.getString("cantidadTotalVendida"));
                }

                // Agregar la tabla al documento
                document.add(table);

                // Cerrar el documento
                document.close();

                System.out.println("El reporte de productos más vendidos se ha generado correctamente.");
            }
        } catch (SQLException ex) {

        }
    }
    
    public void generarReporteMasVendidosPorDia(String fecha) throws FileNotFoundException, DocumentException {
    // Consulta SQL para obtener los productos más vendidos para la fecha especificada
    String sql = "SELECT Producto.id, Producto.marca, Producto.modelo, Producto.pantalla, Producto.camara, Producto.almacenamiento, Producto.ram, SUM(DetalleVenta.cantidadVendida) AS cantidadTotalVendida "
            + "FROM DetalleVenta "
            + "INNER JOIN RegistroVenta ON DetalleVenta.folio_Venta = RegistroVenta.folio "
            + "INNER JOIN Producto ON DetalleVenta.id_producto = Producto.id "
            + "WHERE DATE(RegistroVenta.fechaVenta) = ? "
            + "GROUP BY Producto.id, Producto.marca, Producto.modelo, Producto.pantalla, Producto.camara, Producto.almacenamiento, Producto.ram "
            + "ORDER BY cantidadTotalVendida DESC";

    try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, fecha); // Asignar el parámetro de la fecha

        try (ResultSet rs = pstmt.executeQuery()) {
            // Crear el documento PDF
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("reporte_productos_mas_vendidos_fecha_" + fecha + ".pdf"));

            // Abrir el documento
            document.open();
            Paragraph titulo = new Paragraph("Chispas, mi celular \n",
                    FontFactory.getFont("arial",
                            14,
                            Font.BOLD,
                            BaseColor.BLACK
                    )
            );
            //Centramos el texto
            titulo.setAlignment(Paragraph.ALIGN_CENTER);

            // Añadimos el titulo al documento
            document.add(titulo);

            Paragraph info = new Paragraph("INFORME GENERAL DE LOS PRODUCTOS MAS VENDIDOS",
                    FontFactory.getFont("arial",
                            12,
                            Font.BOLD,
                            BaseColor.BLACK
                    )
            );
            info.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(info);

            // Formatear la fecha
            Paragraph fechaPr = new Paragraph("Fecha: " + fecha + "\n\n",
                    FontFactory.getFont("arial",
                            12,
                            BaseColor.BLACK
                    )
            );
            fechaPr.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(fechaPr);

            // Crear tabla para datos de productos más vendidos
            PdfPTable table = new PdfPTable(8);
            table.setWidths(new float[]{1, 2, 2, 2, 2, 2, 2, 2}); // Ajusta los anchos de las columnas
            table.setHeaderRows(1); // Marca la primera fila como encabezado

            // Encabezados de la tabla
            PdfPCell cell = new PdfPCell(new Paragraph("ID"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Marca"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Modelo"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Pantalla"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Cámara"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Almacenamiento"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("RAM"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Cantidad vendida"));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            // Iterar sobre los resultados de la consulta y agregarlos a la tabla
            while (rs.next()) {
                table.addCell(rs.getString("id"));
                table.addCell(rs.getString("marca"));
                table.addCell(rs.getString("modelo"));
                table.addCell(rs.getString("pantalla"));
                table.addCell(rs.getString("camara"));
                table.addCell(rs.getString("almacenamiento"));
                table.addCell(rs.getString("ram"));
                table.addCell(rs.getString("cantidadTotalVendida"));
            }

            // Agregar la tabla al documento
            document.add(table);

            // Cerrar el documento
            document.close();

            System.out.println("El reporte de productos más vendidos se ha generado correctamente.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

}
