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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
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
    static final String IP_SERVIDOR = "192.168.0.7";
    static final int PUERTO_SERVIDOR = 8080;
    
    public static void DescargarArchivo(InfoArchivo archivo, IServidorIntermediario servidorIntermediario)
    {
        int partesDescargadas = 0;
        int partes = archivo.servidoresContenido.size();
        ArrayList<Direccion> servidoresContenido = archivo.servidoresContenido;
        String carpetaDescarga = "compartidos/";
        
        while (partesDescargadas < partes)
        {
            try {
                String ipServidor = servidoresContenido.get(partesDescargadas).ip;
                int puertoServidor = servidoresContenido.get(partesDescargadas).puerto;
                IServidorContenido servidorContenido = (IServidorContenido) Naming.lookup("//"+ipServidor+":"+8080
                                                                                    +"/ServidorContenido");
                int tamanoParte = servidorContenido.CompartirArchivo(archivo.hash, partesDescargadas, partes, puertoServidor);
                
                System.out.println("Conectandose a " + ipServidor + " en el puerto " + puertoServidor);
                Socket client = new Socket(ipServidor, puertoServidor);
                System.out.println("Conexion realizada");
                
                System.out.println("Descargando " + archivo.nombre + ": parte " + (partesDescargadas+1) + " de " + partesDescargadas);
                byte[] mybytearray = new byte[tamanoParte];
                InputStream is = client.getInputStream();
                FileOutputStream fos = new FileOutputStream(carpetaDescarga+archivo.nombre+".part"+partesDescargadas);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int bytesRead, current = 0;
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;
                
                do
                {
                    
                    bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                    if (bytesRead >= 0)
                        current += bytesRead;
                } while (bytesRead > 0);
                
                bos.write(mybytearray, 0, current);
                bos.close();
                client.close();
                //TODO: aumentar solo cuando se descarga una parte del archivo
                partesDescargadas++;
            } catch (NotBoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        ManejadorArchivos.UnirArchivo(carpetaDescarga+archivo.nombre, partes);
        
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        
        System.out.println("Archivo descargado.");
        
        try {
            System.out.println("Para registrarse como servidor de contenido debe:");
            System.out.print("Ingresar direccion ip: ");
            String ip = in.readLine();
            System.out.print("Ingresar puerto para descarga de este archivo: ");
            int puerto = Integer.parseInt(in.readLine());
            archivo.servidoresContenido.add(new Direccion(ip, puerto));
            servidorIntermediario.RegistrarServidorContenido(archivo);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    public static void main(String[] args)
    {
      try
      {  
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        
        String direccionIp = "192.168.0.7"; // Pc manu
        //String direccionIp = "192.168.0.4"; // Pc lore
        //String direccionIp = "192.168.0.14"; // Pc sebas
        //String IP_SERVIDOR = "192.168.0.7";
        //int PUERTO_SERVIDOR = 8080;
        LocateRegistry.createRegistry(PUERTO_SERVIDOR);
        ServidorContenidoImplementacion servidorContenido = new ServidorContenidoImplementacion("rmi://"+direccionIp+":"+PUERTO_SERVIDOR+"/ServidorContenido");
        
        IServidorIntermediario servidor =
                              (IServidorIntermediario) Naming.lookup("//"+IP_SERVIDOR+":"+PUERTO_SERVIDOR+"/ServidorIntermediario");
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
                    DescargarArchivo(infoArch, servidor);
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
   

