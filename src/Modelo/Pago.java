package Modelo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Pago {

    public static double calcularTotal(List<Producto> productosCanasta) {
        double total = 0;

        if (!productosCanasta.isEmpty()) {
            for (int i = 0; i < productosCanasta.size(); i++) {
                total = total + (productosCanasta.get(i).getPrecio() * productosCanasta.get(i).getCantidad());
            }
        }

        return total;
    }

    public static void generarRecibo(int folioVenta, List<Producto> productosCanasta, Cliente cliente, double monto, String metodoPago, String numTarjeta) throws FileNotFoundException, DocumentException {

        com.itextpdf.text.Rectangle pageSize = new com.itextpdf.text.Rectangle(200, 400);

        Document documento = new Document(pageSize);
        documento.setMargins(12, 12, 10, 0);
        FileOutputStream ficheroPDF = new FileOutputStream("Recibo.pdf");
        PdfWriter.getInstance(documento, ficheroPDF);

        // Se abre el documento
        documento.open();

        Paragraph titulo = new Paragraph("Chispas, mi celular \n",
                FontFactory.getFont("arial",
                        10,
                        Font.BOLD,
                        BaseColor.BLACK
                )
        );
        //Centramos el texto
        titulo.setAlignment(Paragraph.ALIGN_CENTER);

        // Añadimos el titulo al documento
        documento.add(titulo);

        Paragraph direccion = new Paragraph("Puebla\n RFC:CHI050110B2",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        direccion.setAlignment(Paragraph.ALIGN_CENTER);

        documento.add(direccion);

        Paragraph empleado = new Paragraph("\nAtendio: administrador",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        empleado.setAlignment(Paragraph.AUTHOR);

        documento.add(empleado);

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

        Paragraph fechaYhora = new Paragraph("Fecha: " + horaFormateada,
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        fechaYhora.setAlignment(Paragraph.ALIGN_RIGHT);
        documento.add(fechaYhora);

        //--------------------------------------------------------------------------
        //Cliente
        LineSeparator linea = new LineSeparator();
        documento.add(new Chunk(linea));

        Paragraph datosCliente = new Paragraph("Datos del cliente",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        datosCliente.setAlignment(Paragraph.ALIGN_CENTER);

        documento.add(datosCliente);
        datosCliente = new Paragraph("Folio: " + cliente.getId() + "\nNombre: " + cliente.getNombre() + " " + cliente.getApellidoPaterno()
                + " " + cliente.apellidoMaterno + "\nTelefono: " + cliente.getTelefono(),
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        documento.add(datosCliente);
        //Es pago con tarjeta
        if (Venta.MetodoPago.tarjeta.name().equals(metodoPago)) {
            Paragraph tarjeta = new Paragraph("Tarjeta: *************" + numTarjeta.substring(numTarjeta.length() - 3),
                    FontFactory.getFont("arial",
                            8,
                            BaseColor.BLACK
                    )
            );
            documento.add(tarjeta);
        }

        //-------------------------------------------------------------------------
        documento.add(new Chunk(linea));

        Paragraph venta = new Paragraph("Factura simplificada", FontFactory.getFont("arial", 8, BaseColor.BLACK));
        venta.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(venta);

        Paragraph folio = new Paragraph("Folio: " + folioVenta + "\n\n",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        documento.add(folio);

        //Tabla
        PdfPTable tabla = new PdfPTable(4);
        // Establecer el ancho de las columnas
        float[] columnWidths = {1f, 3f, 1.2f, 1.8f}; // Ajustar según tus necesidades
        tabla.setWidths(columnWidths);
        tabla.setHeaderRows(1);
        com.itextpdf.text.Font font = FontFactory.getFont("arial",
                8,
                BaseColor.BLACK);

        tabla.addCell(new PdfPCell(new Phrase("Id", font)));
        tabla.addCell(new PdfPCell(new Phrase("Producto", font)));
        tabla.addCell(new PdfPCell(new Phrase("Cant.", font)));
        tabla.addCell(new PdfPCell(new Phrase("Precio unit.", font)));


        for (int i = 0; i < productosCanasta.size(); i++) {
            tabla.addCell(new PdfPCell(new Phrase(String.valueOf(productosCanasta.get(i).getId()), font)));
            tabla.addCell(new PdfPCell(new Phrase(productosCanasta.get(i).getMarca() + " " + productosCanasta.get(i).getModelo(), font)));
            tabla.addCell(new PdfPCell(new Phrase(String.valueOf(productosCanasta.get(i).getCantidad()), font)));
            tabla.addCell(new PdfPCell(new Phrase(String.valueOf(productosCanasta.get(i).getPrecio()), font)));
        }

        tabla.getDefaultCell().setPadding(5); // Ajustar el espacio entre el contenido y los bordes de las celdas
        tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // Alinear el contenido al centro de las celdas
        tabla.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        documento.add(tabla);
        
        
        //Metodo de pago 
        
        double totalVenta = calcularTotal(productosCanasta);
        Paragraph total = new Paragraph("\nTotal: " + totalVenta,
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
        );
        total.setAlignment(Paragraph.ALIGN_RIGHT);
        documento.add(total);
        
        if (Venta.MetodoPago.efectivo.name().equals(metodoPago)){
            Paragraph cambio = new Paragraph("Monto recibido: " + monto + "\nCambio: " + (monto - totalVenta),
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
            );
            cambio.setAlignment(Paragraph.ALIGN_RIGHT);
            documento.add(cambio);
            
            Paragraph efectivo = new Paragraph("Pago en efectivo ",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
            );
            efectivo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(efectivo);
            
        } else {
            Paragraph tarjeta = new Paragraph("Pago con tarjeta\n Pago validado ",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK
                )
            );
            tarjeta.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(tarjeta);
        }
        
        

        //-------------------------------------------------------------------------
        documento.add(new Chunk(linea));

        Paragraph pie = new Paragraph("¡Gracias por su compra!",
                FontFactory.getFont("arial",
                        8,
                        BaseColor.BLACK)
        );
        pie.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(pie);

        // Se cierra el documento
        documento.close();

    }
}
