package Modelo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administracion {
    public boolean verificaDatos(String usuario, String contraseñaIngresada) {
        boolean autenticado = false;
        String contraseñaEncriptadaIngresada = encriptarMD5(contraseñaIngresada);

        String sql = "SELECT contrasenia FROM administrador WHERE BINARY usuario = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String contraseñaEncriptadaBD = rs.getString("contrasenia");
                    if (contraseñaEncriptadaBD.equals(contraseñaEncriptadaIngresada)) {
                        autenticado = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autenticado;
    }

    private String encriptarMD5(String contrasenia) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(contrasenia.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
