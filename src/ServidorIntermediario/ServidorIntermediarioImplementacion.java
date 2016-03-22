/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorIntermediario;

import Utils.Directorio;
import Utils.InfoArchivo;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manuela
 */
public class ServidorIntermediarioImplementacion extends UnicastRemoteObject implements IServidorIntermediario
{
    private Directorio directorio;
    private String nombre;
    ServidorIntermediarioImplementacion(String nombre, Directorio directorio) throws RemoteException
    {
        super();
        
        this.nombre = nombre;
        this.directorio = directorio;
        
        try {
            Naming.rebind(nombre, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorIntermediarioImplementacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public InfoArchivo BuscarArchivo(String nombre) {
        System.out.println("BuscarArchivo: " + nombre);
        ArrayList<InfoArchivo> archivos = directorio.getListaArchivos();
        InfoArchivo archivo = null;
        
        for (InfoArchivo ia : archivos)
        {
            if (ia.nombre.equals(nombre))
            {
                archivo = ia;
                break;
            }
        }
        
        return archivo;
    }

    @Override
    public boolean RegistrarServidorContenido(InfoArchivo infoArchivo) {
        System.out.println("RegistrarServidorContenido: " + infoArchivo);
        return true;
    }
    
}
