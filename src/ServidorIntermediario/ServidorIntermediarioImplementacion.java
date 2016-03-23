/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorIntermediario;

import Utils.Directorio;
import Utils.InfoArchivo;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        System.out.println("BuscarArchivo: \"" + nombre + "\"");
        ArrayList<InfoArchivo> archivos = directorio.getListaArchivos();
        InfoArchivo archivo = null;
        
        for (InfoArchivo ia : archivos)
        {
            if (ia.nombre.equals(nombre))
            {
                archivo = ia;
                System.out.println(ia);
                break;
            }
        }
        
        System.out.println(archivo == null ? "Archivo no encontrado" : "Archivo encontrado");
        return archivo;
    }

    @Override
    public boolean RegistrarServidorContenido(InfoArchivo infoArchivo) {
        directorio.agregarArchivo(infoArchivo);
        boolean success = true;
        
        try
        {
            FileOutputStream fileOut = new FileOutputStream("directorio.bin");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(directorio);
            out.close();
            fileOut.close();
            //System.out.println("Archivo serializado guardado en \"directorio.bin\"");
        }
        catch(IOException i)
        {
            success = false;
            i.printStackTrace();
        }
        
        System.out.println(directorio);
        return success;
    }
    
}
