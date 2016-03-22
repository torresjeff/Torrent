    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Utils.ManejadorArchivos;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;

/**
 *
 * @author sebastiangracia
 */
public class ServidorContenidoImplementacion extends UnicastRemoteObject implements IServidorContenido{
   public  ServidorContenidoImplementacion() throws RemoteException
   {
       super();
       
   }
   public void CompartirArchivo(String nombre, int parte, int partes) throws RemoteException
   { 
        ManejadorArchivos.DividirArchivo(nombre, partes);    
   } 
}
