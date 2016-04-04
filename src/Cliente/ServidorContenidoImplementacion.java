    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import ServidorIntermediario.ServidorIntermediarioImplementacion;
import Utils.ManejadorArchivos;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastiangracia
 */
public class ServidorContenidoImplementacion extends UnicastRemoteObject implements IServidorContenido{
   
    private static ArrayList<Integer> puertosEnUso;
    
    public  ServidorContenidoImplementacion(String nombre) throws RemoteException
   {
       super();
       try {
            Naming.rebind(nombre, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorIntermediarioImplementacion.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public int CompartirArchivo(String hash, int parte, int partes, int puerto) throws RemoteException
   {
        String nombreArchivo = null;
        String carpetaCompartidos = "compartidos/";
        File folder = new File(carpetaCompartidos);
        for (File fileEntry : folder.listFiles())
        {
            if (fileEntry.isDirectory())
            {
                //Si es un directorio
            }
            else //Si es un archivo
            {
                String fileName = fileEntry.getName();
                String hashLocal = ManejadorArchivos.GenerarHash(carpetaCompartidos+fileName);
                //System.out.println(fileEntry.getName() + " hash: " + hashLocal);
                
                if (hash.equals(hashLocal))
                {
                    nombreArchivo = fileName;
                    System.out.println("Se encontro el archivo: \"" + nombreArchivo + "\"");
                    
                    break;
                }
            }
        }
        
        if (nombreArchivo != null)
        {
            ManejadorArchivos.DividirArchivo(carpetaCompartidos+nombreArchivo, partes);
            if (!puertosEnUso.contains(puerto))
            {
                puertosEnUso.add(puerto);
                ThreadEscuchaConexiones conexiones = new ThreadEscuchaConexiones(carpetaCompartidos+nombreArchivo, parte,
                    partes, puerto);
                new Thread(conexiones).start();
            }
            
            File file = new File(carpetaCompartidos+nombreArchivo);
            return (int)file.length();
        }
        
        return -1;
        
   }
}
