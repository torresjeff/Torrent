/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import ServidorIntermediario.IServidorIntermediario;
import Utils.Direccion;
import Utils.InfoArchivo;
import Utils.ManejadorArchivos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
      try
      {  
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);

        IServidorIntermediario servidor =
                              (IServidorIntermediario) Naming.lookup("//192.168.0.7:8080/ServidorIntermediario");
        
        int opcion = -1;
        System.out.println("OPCION 1: Descargar archivo.");
        System.out.println("OPCION 2: Registrar archivo.");
        System.out.println("OPCION 3: Salir.");
        System.out.print("Ingrese opcion: ");
        opcion = Integer.parseInt(in.readLine());

        do{
            switch(opcion)
            {
             case 1:
                System.out.println("Ingrese el nombre del archivo");
                String nombreArch = in.readLine();
                InfoArchivo infoArch = servidor.BuscarArchivo(nombreArch);
                System.out.println(infoArch);
                break;
             case 2:
                System.out.print("Ingrese el nombre del archivo a registrar: ");
                String nombreArchR = in.readLine();
                System.out.print("Ingrese la direccion: ");
                String ip = in.readLine();
                System.out.print("Ingrese el puerto: ");
                int puerto = Integer.parseInt(in.readLine());
                InfoArchivo archivo = new InfoArchivo();
                archivo.hash = ManejadorArchivos.GenerarHash(nombreArchR);
                archivo.nombre = nombreArchR;
                archivo.servidoresContenido.add(new Direccion(ip, puerto));
               
                boolean respuesta = servidor.RegistrarServidorContenido(archivo);
                System.out.println(respuesta? "Registrado satisfactoriamente": "Error al registrar archivo"); 
                break;
             case 3:
                 System.out.println("OPCION 3: SALIR");
                 opcion = -1;
                 break;
             }
        }while ( opcion != -1);
    }
                  catch (NotBoundException ex) {
                      Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (MalformedURLException ex) {
                      Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (RemoteException ex) {
                      Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 }
   

