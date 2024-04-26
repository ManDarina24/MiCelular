package Modelo;

/**
 *
 * @author jocel
 */
public class PagoEfectivo extends Pago {
    public double calcularCambio(double total, double montoRecibido){
        double cambio;
        cambio = montoRecibido - total;
        return cambio;
    }
}
