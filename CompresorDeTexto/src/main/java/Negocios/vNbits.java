/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author user
 */
public class vNbits {

    private int v[];
    private int nb;
    private int cant;

    public vNbits(int ne, int nb) {
        this.cant = ne;
        this.nb = nb;
        int NE = ((ne * nb) % 32 == 0) ? (ne * nb) / 32 : ((ne * nb) / 32) + 1;
        v = new int[NE];
    }

    public void insertar(int elem, int pos) {
        if (pos > 0 && pos <= cant && elem <= (Math.pow(2, nb)) - 1) {
            int ent = CalEnt(pos);
            int bits = CalBits(pos);
            int elem1 = elem;
            int mask = (int) ((Math.pow(2, nb)) - 1);
            mask = mask << bits;
            mask = ~mask;
            v[ent] = v[ent] & mask;
            elem = elem << bits;
            v[ent] = v[ent] | elem;
            if ((bits + nb) > 32) {
                mask = (int) (Math.pow(2, nb) - 1);
                mask = mask >>> 32 - bits;
                mask = ~mask;
                v[ent + 1] = v[ent + 1] & mask;
                elem1 = elem1 >>> 32 - bits;
                v[ent + 1] = v[ent + 1] | elem1;
            }
        }

    }

    public int obtener(int pos) {
        int Nent = CalEnt(pos);
        int Nbits = CalBits(pos);
        int mask = (int) (Math.pow(2, nb) - 1);
        mask = mask << Nbits;
        mask = v[Nent] & mask;
        mask = mask >>> Nbits;
        if ((Nbits + nb) > 32) {
            int mask1 = (int) (Math.pow(2, nb) - 1);
            mask1 = mask1 >>> 32 - Nbits;
            mask1 = mask1 & v[Nent + 1];
            mask1 = mask1 << 32 - Nbits;
            mask = mask1 | mask;
        }
        return mask;
    }

    private int CalEnt(int pos) {
        return ((pos - 1) * nb / 32);
    }

    private int CalBits(int pos) {
        return ((pos - 1) * nb % 32);
    }

    public int getCant() {
        return this.cant;
    }

    public int[] toIntArray() {
        return v;
    }

    public byte[] toByteArray() {
        int cantidad = ((cant * nb) % 32 == 0) ? (cant * nb) / 32 : ((cant * nb) / 32) + 1;
        byte byteArray[] = new byte[cantidad*4 + 4];
        int aux1 = cant;
        byteArray[0] = (byte) (aux1 >> 24);
        byteArray[1] = (byte) (aux1 >> 16);
        byteArray[2] = (byte) (aux1 >> 8);
        byteArray[3] = (byte) aux1;
        for (int i = 0; i < cantidad; i++) {
            int aux = v[i];
            byteArray[i*4 + 4] = (byte) (aux >> 24);
            byteArray[i*4 + 5] = (byte) (aux >> 16);
            byteArray[i*4 + 6] = (byte) (aux >> 8);
            byteArray[i*4 + 7] = (byte) aux;
        }
        return byteArray;
    }
    
    public void fromByteArray(byte [] byteArray) {
        int cantidad = byteArray.length / 4;
        for (int i = 0; i < cantidad - 1; i++) {
            v[i] = (((byteArray[i * 4 + 4] & 0xFF) << 24)
                | ((byteArray[i * 4 + 5] & 0xFF) << 16)
                | ((byteArray[i * 4 + 6] & 0xFF) << 8)
                | ((byteArray[i * 4 + 7] & 0xFF)));
        }
    }

    private byte[] intToByteArray(int value) {
        return new byte[]{
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value};
    }

    private int byteArrayToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24)
                | ((bytes[1] & 0xFF) << 16)
                | ((bytes[2] & 0xFF) << 8)
                | ((bytes[3] & 0xFF));
    }

    @Override
    public String toString() {
        String s = "V={";
        for (int i = 1; i <= cant; i++) {
            s = s + " " + obtener(i) + ",";
        }
        s = s.replaceFirst(".$", "") + " }";
        return s;
    }

}
