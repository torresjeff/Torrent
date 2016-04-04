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
    /**
     * Abre un socket para enviar el archivo. Ademas divide el archivo en el numero de partes.
     * @param hash hash del archivo a ser descargado.
     * @param parte parte actual del archivo a ser descargado.
     * @param partes numero total de partes en la que se divide el archivo. Este numero corresponde al numero de maquinas que tienen el archivo.
     * @param puerto puerto por el que vamos a enviar el archivo.
     * @return tamano de la parte actual que se va a descargar.
     * @throws RemoteException 
     */
    public int CompartirArchivo(String hash, int parte, int partes, int puerto) throws RemoteException;
}
