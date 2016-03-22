/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Utils.InfoArchivo;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author manuela
 */
public class Directorio implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private ArrayList<InfoArchivo> listaArchivos;
    
    public Directorio()
    {
        listaArchivos = new ArrayList<>();
    }
    
    public void agregarArchivo(InfoArchivo info)
    {
        listaArchivos.add(info);
    }

    @Override
    public String toString() {
        return "Directorio{" + "listaArchivos=" + listaArchivos + '}';
    }
    
    
}
