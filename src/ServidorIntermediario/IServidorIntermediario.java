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
    /**
     * El servidor busca el la informacion del archivo (hash y las maquinas que comparten el).
     * @param nombre nombre del archivo que se quiere descargar
     * @return informacion del archivo que queremos descargar.
     * @throws RemoteException 
     */
    public InfoArchivo BuscarArchivo(String nombre) throws RemoteException;
    
    /**
     * Registra la maquina que comparte el archivo.
     * @param infoArchivo informacion del archivo a registrar
     * @return true si fue satisfactorio, false si no.
     * @throws RemoteException 
     */
    public boolean RegistrarServidorContenido(InfoArchivo infoArchivo) throws RemoteException;
}
