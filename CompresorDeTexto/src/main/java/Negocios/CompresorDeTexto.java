/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author ASUS
 */
public class CompresorDeTexto {

    private String texto; // Texto normal
    public vNbits textoCompreso; // Texto comprimido a nivel de bits

    // Constructor de oficio
    public CompresorDeTexto() {
        this.texto = "";
        this.textoCompreso = null;
    }

    // CONSTRUCTOR PARAMETRIZADO
    // Recibe el texto, lo comprime y guarda en la ruta predeterminada.
    public CompresorDeTexto(String textoAComprimir) throws IOException {
        this.comprimirTexto(textoAComprimir);
        this.guardarArchivoCompreso();
    }
    
    // CONSTRUCTOR PARAMETRIZADO
    // Recibe el texto, lo comprime y guarda en la ruta (segundo parametro).
    public CompresorDeTexto(String textoAComprimir, String ruta) throws IOException {
        this.comprimirTexto(textoAComprimir);
        this.guardarArchivoCompreso(ruta);
    }

    // Método que devuelve true si el byte que recibe como parámetro pertenece a 
    // una letra [A, Z], [a, z], espacio o salto de línea.
    private boolean esUnCaracterAceptado(byte numeroDeByte) {
        return (numeroDeByte >= 65 && numeroDeByte <= 90
                || numeroDeByte >= 97 && numeroDeByte <= 122
                || numeroDeByte == 32 || numeroDeByte == 10);
    }

    // Método que transforma el codigo ascii de un carácter a un código compreso.
    // Retorna -1 si el no es un carácter aceptado.
    private int codigoAsciiToCodigoCompreso(byte codigoAscii) {
        if (esUnCaracterAceptado(codigoAscii)) {
            if (codigoAscii >= 65 && codigoAscii <= 90) {
                return ((int) codigoAscii - 65);
            }
            if (codigoAscii >= 97 && codigoAscii <= 122) {
                return ((int) codigoAscii - 71);
            }
            if (codigoAscii == 32) {
                return ((int) codigoAscii + 20);
            }
            if (codigoAscii == 10) {
                return ((int) codigoAscii + 43);
            }
        }
        return -1;
    }

    // Método que recibe un codigo compreso y devuelve el codigo Ascii correspondiente.
    private byte codigoCompresoToCodigoAscii(byte codigoCompreso) {
        if (codigoCompreso >= 0 && codigoCompreso <= 25) {
            return ((byte) (codigoCompreso + 65));
        }
        if (codigoCompreso >= 26 && codigoCompreso <= 51) {
            return ((byte) (codigoCompreso + 71));
        }
        if (codigoCompreso == 52) {
            return ((byte) 32);
        }
        return ((byte) 10);

    }

    // COMPRIME EL TEXTO Y LO GUARDA EN EL VECTOR DE N BITS
    public void comprimirTexto(String textoAComprimir) {
        // Se asigna el texto y luego se inicializa el vNBits que contendrá el texto compreso.
        this.texto = textoAComprimir.replaceAll("[^a-zA-Z - \n-\n]", ""); //Se elimina cualquier carácter no válido
        this.textoCompreso = new vNbits(this.texto.length(), 6);
        // Se define un vector de bytes donde se guardarán los valores (Ascii) de la cadena de Texto
        byte textoEnBytes[] = textoAComprimir.getBytes();
        // Ciclo For para convertir e insertar cada uno de los bytes en el vector de N bits
        for (int i = 0; i < textoAComprimir.length(); i++) {
            // Definimos un entero que almacenará el código convertido usando el método "codigoAsciiToCodigoCompreso"
            int codigoCompreso = codigoAsciiToCodigoCompreso(textoEnBytes[i]);
            // Si codigoCompreso = -1 significa que el carácter no era válido
            if (codigoCompreso > -1) {
                // Se inserta en el vector de N bits (llamado textoCompreso) el codigoCompreso
                textoCompreso.insertar(codigoCompreso, i + 1);
            }
        }
    }

    // GUARDA EL ARCHIVO COMPRESO EN LA RUTA DADA
    public void guardarArchivoCompreso(String ruta) throws FileNotFoundException, IOException {
        byte byteArray[] = textoCompreso.toByteArray();
        FileOutputStream salida = new FileOutputStream(ruta);
        salida.write(byteArray);
    }
    
    public void guardarArchivoCompreso() throws FileNotFoundException, IOException {
        byte byteArray[] = textoCompreso.toByteArray();
        FileOutputStream salida = new FileOutputStream("archivoCompreso.txt");
        salida.write(byteArray);
    }

    public void comprimirYGuardar(String textoAComprimir, String ruta) throws IOException {
        this.comprimirTexto(textoAComprimir);
        this.guardarArchivoCompreso(ruta);
    }
    
    public void comprimirYGuardar(String textoAComprimir) throws IOException {
        this.comprimirTexto(textoAComprimir);
        this.guardarArchivoCompreso();
    }

    public String descomprimirTexto() {
        byte byteArray[] = new byte[textoCompreso.getCant()];
        for (int i = 0; i < textoCompreso.getCant(); i++) {
            byteArray[i] = codigoCompresoToCodigoAscii((byte) textoCompreso.obtener(i + 1));
        }
        this.texto = new String(byteArray);
        return this.texto;
    }

    public void abrirArchivoCompreso() throws FileNotFoundException, IOException {
        FileInputStream entrada = new FileInputStream("archivoCompreso.txt"); // Abre el archivo de texto
        byte byteArray[] = entrada.readAllBytes(); // Cadena para almacenar el texto del archivo
        int cantidad = ((byteArray[0] & 0xFF) << 24)
                | ((byteArray[1] & 0xFF) << 16)
                | ((byteArray[2] & 0xFF) << 8)
                | ((byteArray[3] & 0xFF));
        this.textoCompreso = new vNbits(cantidad, 6);
        this.textoCompreso.fromByteArray(byteArray);
    }
    
    
    public void abrirArchivoCompreso(String ruta) throws FileNotFoundException, IOException {
        FileInputStream entrada = new FileInputStream(ruta); // Abre el archivo de texto
        byte byteArray[] = entrada.readAllBytes(); // Cadena para almacenar el texto del archivo
        int cantidad = ((byteArray[0] & 0xFF) << 24)
                | ((byteArray[1] & 0xFF) << 16)
                | ((byteArray[2] & 0xFF) << 8)
                | ((byteArray[3] & 0xFF));
        this.textoCompreso = new vNbits(cantidad, 6);
        this.textoCompreso.fromByteArray(byteArray);
    }
}
