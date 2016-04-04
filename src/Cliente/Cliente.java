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
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manuela
 */
public class Cliente
{
    public static void DescargarArchivo(InfoArchivo archivo)
    {
        int partesDescargadas = 0;
        int partes = archivo.servidoresContenido.size();
        ArrayList<Direccion> servidoresContenido = archivo.servidoresContenido;
        
        while (partesDescargadas < partes)
        {
            try {
                IServidorContenido servidorContenido = (IServidorContenido) Naming.lookup("//"+servidoresContenido.get(0).ip+":"+servidoresContenido.get(0).puerto
                                                                                    +"/ServidorContenido");
                servidorContenido.CompartirArchivo(archivo.hash, partesDescargadas, partes);
                
                //TODO: aumentar solo cuando se descarga una parte del archivo
                partesDescargadas++;
            } catch (NotBoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    
    public static void main(String[] args)
    {
       
       
       
       
      try
      {  
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        
        String direccionIp = "192.168.0.7"; // Pc manu
        //String direccionIp = "192.168.0.6"; // Pc lore
        //String direccionIp = "192.168.0.14"; // Pc sebas
        String ipServidor = "192.168.0.7";
        int puerto = 8080;
        LocateRegistry.createRegistry(puerto);
        ServidorContenidoImplementacion servidorContenido = new ServidorContenidoImplementacion("rmi://"+direccionIp+":"+puerto+"/ServidorContenido");
        
        IServidorIntermediario servidor =
                              (IServidorIntermediario) Naming.lookup("//"+ipServidor+":"+puerto+"/ServidorIntermediario");
                              //(IServidorIntermediario) Naming.lookup("//127.0.0.1:8080/ServidorIntermediario");
        
        int opcion = -1;
        

        do{
            System.out.println("OPCION 1: Descargar archivo.");
            System.out.println("OPCION 2: Registrar archivo.");
            System.out.println("OPCION 3: Salir.");
            System.out.print("Ingrese opcion: ");
            opcion = Integer.parseInt(in.readLine());
            
            switch(opcion)
            {
             case 1:
                System.out.print("Ingrese el nombre del archivo: ");
                String nombreArch = in.readLine();
                InfoArchivo infoArch = servidor.BuscarArchivo(nombreArch);
                if (infoArch == null)
                {
                    System.out.println("Archivo no encontrado.");
                }
                else
                {
                    System.out.println(infoArch);
                    DescargarArchivo(infoArch);
                }
                
                break;
             case 2:
                System.out.print("Ingrese el nombre del archivo a registrar: ");
                String nombreArchR = in.readLine();
                System.out.print("Ingrese la direccion: ");
                String ip = in.readLine();
                System.out.print("Ingrese el puerto: ");
                int puertoDescarga = Integer.parseInt(in.readLine());
                InfoArchivo archivo = new InfoArchivo();
                archivo.hash = ManejadorArchivos.GenerarHash("compartidos/"+nombreArchR);
                archivo.nombre = nombreArchR;
                archivo.servidoresContenido.add(new Direccion(ip, puertoDescarga));
               
                boolean respuesta = servidor.RegistrarServidorContenido(archivo);
                System.out.println(respuesta? "Registrado satisfactoriamente": "Error al registrar archivo"); 
                break;
             case 3:
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
   

