    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import ServidorIntermediario.ServidorIntermediarioImplementacion;
import Utils.ManejadorArchivos;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
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
   public void CompartirArchivo(String nombre, int parte, int partes) throws RemoteException
   { 
        ManejadorArchivos.DividirArchivo(nombre, partes);    
   } 
}
