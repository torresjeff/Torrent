/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import ServidorIntermediario.IServidorIntermediario;
import Utils.InfoArchivo;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manuela
 */
public class Cliente
{
    public static void main(String[] args)
    {
        try {
            IServidorIntermediario servidor =
                    (IServidorIntermediario) Naming.lookup("rmi://192.168.0.7:8080/ServidorIntermediario");
            Scanner sc = new Scanner(System.in);  //crear un objeto Scanner
            String nombreArch = sc.nextLine();
            InfoArchivo infoArch = servidor.BuscarArchivo(nombreArch);
            System.out.println(infoArch);
        } catch (NotBoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
