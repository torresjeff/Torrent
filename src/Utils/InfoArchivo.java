/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author manuela
 */
public class InfoArchivo implements Serializable
{
    public InfoArchivo()
    {
        servidoresContenido = new ArrayList<>();
    }
    public String hash;
    public String nombre;
    public ArrayList<Direccion> servidoresContenido;

    @Override
    public String toString() {
        return "InfoArchivo{" + "hash=" + hash + ", nombre=" + nombre + ", servidoresContenido=" + servidoresContenido + '}';
    }
    
    
}
