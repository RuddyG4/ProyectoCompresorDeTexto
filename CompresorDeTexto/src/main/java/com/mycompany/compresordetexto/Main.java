/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.compresordetexto;

import Negocios.CompresorDeTexto;
import java.io.IOException;

/**
 *
 * @author ASUS
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        String str = "Hello, World_123!! bye\nHello again4";
        str = str.replaceAll("[^a-zA-Z - \n-\n]", "");
        System.out.println(str);    
    }
}
