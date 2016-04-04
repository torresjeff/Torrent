    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import ServidorIntermediario.ServidorIntermediarioImplementacion;
import Utils.ManejadorArchivos;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastiangracia
 */
public class ServidorContenidoImplementacion extends UnicastRemoteObject implements IServidorContenido{
   
    public  ServidorContenidoImplementacion(String nombre) throws RemoteException
   {
       super();
       try {
            Naming.rebind(nombre, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorIntermediarioImplementacion.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public void CompartirArchivo(String hash, int parte, int partes) throws RemoteException
   { 
        String nombreArchivo = null;
        File folder = new File(".");
        for (File fileEntry : folder.listFiles())
        {
            if (fileEntry.isDirectory())
            {
                //Si es un directorio
            }
            else //Si es un archivo
            {
                String fileName = fileEntry.getName();
                String hashLocal = ManejadorArchivos.GenerarHash(fileName);
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
            ManejadorArchivos.DividirArchivo(nombreArchivo, partes);
            //ThreadEscuchaConexiones conexiones = new ThreadEscuchaConexiones(8081);
            //new Thread(conexiones).start();
            return;
        }
        
   }
}
