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
    //Llevamos una lista de los puertos que estamos usando para enviar los archivos.
    private static ArrayList<Integer> puertosEnUso;
    
    /**
     * 
     * @param nombre Direccion y puerto donde escucharemos las invocaciones remotas de metodos.
     * @throws RemoteException 
     */
    public  ServidorContenidoImplementacion(String nombre) throws RemoteException
   {
       super();
       puertosEnUso = new ArrayList<>();
       try {
            Naming.rebind(nombre, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorIntermediarioImplementacion.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    
    /**
     * Abre un socket para enviar el archivo. Ademas divide el archivo en el numero de partes.
     * @param hash hash del archivo a ser descargado.
     * @param parte parte actual del archivo a ser descargado.
     * @param partes numero total de partes en la que se divide el archivo. Este numero corresponde al numero de maquinas que tienen el archivo.
     * @param puerto puerto por el que vamos a enviar el archivo.
     * @return tamano de la parte actual que se va a descargar.
     * @throws RemoteException 
     */
   public int CompartirArchivo(String hash, int parte, int partes, int puerto) throws RemoteException
   {
        String nombreArchivo = null; //Nombre del archivo que corresponde con el hash del parametro.
        String carpetaCompartidos = "compartidos/"; //Carpeta donde buscamos los archivos.
        File folder = new File(carpetaCompartidos); //Creamos un objeto de la carpeta.
        for (File fileEntry : folder.listFiles()) //Recorremos todos los archivos de la carpeta "compartidos"
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
                
                //Si el hash corresponde al que nos entro como parametro, entonces encontramos el archivo
                if (hash.equals(hashLocal))
                {
                    nombreArchivo = fileName; //Guadamos el nombre del archivo
                    System.out.println("Se encontro el archivo: \"" + nombreArchivo + "\"");
                    
                    break;
                }
            }
        }
        
        //Si encontramos el archivo que se quiere descargar
        if (nombreArchivo != null)
        {
            //Dividimoso el archivo en el numero de partes que entro como parametro
            ManejadorArchivos.DividirArchivo(carpetaCompartidos+nombreArchivo, partes);
            
            //si no estamos utilizando creamos un nuevo hilo para escuchar por ese puerto y enviar el archivo.
            if (!puertosEnUso.contains(puerto))
            {
                puertosEnUso.add(puerto);
                ThreadEscuchaConexiones conexiones = new ThreadEscuchaConexiones(carpetaCompartidos+nombreArchivo, parte,
                    partes, puerto);
                new Thread(conexiones).start(); // Creamos un nuevo thread para escuchar conexiones
            }
            
            
            File file = new File(carpetaCompartidos+nombreArchivo);
            return (int)file.length(); //Retornamos el tamano total del archivo
        }
        
        return -1; //Retornamos -1 si no encontramos el archivo
        
   }
}
