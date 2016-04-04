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
    
    /**
     * El servidor busca el la informacion del archivo (hash y las maquinas que comparten el).
     * @param nombre nombre del archivo que se quiere descargar
     * @return informacion del archivo que queremos descargar.
     * @throws RemoteException 
     */
    @Override
    public InfoArchivo BuscarArchivo(String nombre) {
        System.out.println("BuscarArchivo: \"" + nombre + "\"");
        ArrayList<InfoArchivo> archivos = directorio.getListaArchivos();
        InfoArchivo archivo = null;
        
        //Buscamos si el archivo existe en el directorio.
        for (InfoArchivo ia : archivos)
        {
            if (ia.nombre.equalsIgnoreCase(nombre))
            {
                archivo = ia;
                System.out.println(ia);
                break;
            }
        }
        
        System.out.println(archivo == null ? "Archivo no encontrado" : "Archivo encontrado");
        return archivo; // Retornamos los datos del archivo si lo encontramos
    }

    
    /**
     * Registra la maquina que comparte el archivo.
     * @param infoArchivo informacion del archivo a registrar
     * @return true si fue satisfactorio, false si no.
     * @throws RemoteException 
     */
    @Override
    public boolean RegistrarServidorContenido(InfoArchivo infoArchivo) {
        directorio.agregarArchivo(infoArchivo); //Registramos el archivo
        boolean success = true;
        
        try
        {
            //Actualizamos y serializamos el directorio
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
        
        System.out.println("Archivo registrado");
        System.out.println(directorio);
        return success;
    }
    
}
