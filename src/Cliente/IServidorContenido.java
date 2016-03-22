/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author sebastiangracia
 */
public interface IServidorContenido extends Remote {
    public void CompartirArchivo(String nombre, int parte, int partes) throws RemoteException;
}
