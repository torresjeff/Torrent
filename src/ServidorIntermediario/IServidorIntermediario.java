/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorIntermediario;

import Utils.InfoArchivo;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author manuela
 */
public interface IServidorIntermediario extends Remote
{
    public InfoArchivo BuscarArchivo(String nombre) throws RemoteException;
    public boolean RegistrarServidorContenido(InfoArchivo infoArchivo) throws RemoteException;
}
