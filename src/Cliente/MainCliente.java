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
public class MainCliente
{
    static final String IP_SERVIDOR = "192.168.0.4"; //Direccion ip del servidor intermediario
    static final int PUERTO_SERVIDOR = 8080; //Puerto del servidor intermediario
    
    /**
     * Descarga todas las partes de un archivo de las diferentes maquinas que lo comparten.
     * @param archivo informacion del archivo.
     * @param servidorIntermediario servidor intermediario.
     */
    public static void DescargarArchivo(InfoArchivo archivo, IServidorIntermediario servidorIntermediario)
    {
        int partesDescargadas = 0;
        int partes = archivo.servidoresContenido.size(); //Partes totales en las que se divide el archivo. Numero de maquians que comparten el archivo
        ArrayList<Direccion> servidoresContenido = archivo.servidoresContenido;
        String carpetaDescarga = "compartidos/"; //El archivo se descarga en la carpeta "compartidos/"
        
        //Mientras no hayamos terminado de descargar todas las partes del archivo
        while (partesDescargadas < partes)
        {
            try {
                String ipServidor = servidoresContenido.get(partesDescargadas).ip;
                int puertoServidor = servidoresContenido.get(partesDescargadas).puerto;
                
                //Buscamos a una de las maquinas que tenga el archivo con direccion y puertos anteriores
                IServidorContenido servidorContenido = (IServidorContenido) Naming.lookup("//"+ipServidor+":"+8080
                                                                                    +"/ServidorContenido");
                
                //Guardamos el tamano de la parte que vamos a descargar.
                int tamanoParte = servidorContenido.CompartirArchivo(archivo.hash, partesDescargadas, partes, puertoServidor);
                
                
                System.out.println("Conectandose a " + ipServidor + " en el puerto " + puertoServidor);
                Socket client = new Socket(ipServidor, puertoServidor); // Nos conectamos a la maquina que esta compartiendo el archivo.
                System.out.println("Conexion realizada");
                
                System.out.println("Descargando " + archivo.nombre + ": parte " + (partesDescargadas+1) + " de " + partes);
                byte[] mybytearray = new byte[tamanoParte]; //Creamos un arreglo lo suficientemente grande para guardar la parte del archivo.
                //Creamos flujos de entrada por donde vamos a recibir los datos.
                InputStream is = client.getInputStream();
                //Creamos el archivo donde lo vamos a guardar.
                FileOutputStream fos = new FileOutputStream(carpetaDescarga+archivo.nombre+".part"+partesDescargadas);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int bytesRead, current = 0;
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;
                
                do
                {
                    //Seguimos leyendo del flujo de entrada mientras no se haya completado la descarga.
                    bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                    if (bytesRead >= 0)
                        current += bytesRead;
                } while (bytesRead > 0);
                
                bos.write(mybytearray, 0, current);
                bos.close();
                client.close();
                
                partesDescargadas++; // Aumentamos el numero de partes descargadas
            } catch (NotBoundException ex) {
                Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        //Luego de descargar todas las partes unimos el archivo.
        ManejadorArchivos.UnirArchivo(carpetaDescarga+archivo.nombre, partes);
        
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        
        System.out.println("Archivo descargado.");
        
        try {
            //Nos registramos como servidores de contenido
            System.out.println("Para registrarse como servidor de contenido debe:");
            System.out.print("Ingresar direccion ip: ");
            String ip = in.readLine();
            System.out.print("Ingresar puerto para descarga de este archivo: ");
            int puerto = Integer.parseInt(in.readLine());
            archivo.servidoresContenido.add(new Direccion(ip, puerto));
            
            servidorIntermediario.RegistrarServidorContenido(archivo); //Le decimos al servidor intermediario que nos registre
            
        } catch (IOException ex) {
            Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    public static void main(String[] args)
    {
      try
      {  
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        
        String direccionIp = "192.168.0.7"; // Direccion PC de este computador
        //String direccionIp = "192.168.0.4"; // Pc lore
        //String direccionIp = "192.168.0.14"; // Pc sebas
        //String IP_SERVIDOR = "192.168.0.7";
        //int PUERTO_SERVIDOR = 8080;
        int puertoContenido = 8080; // Puerto donde escuchamos las invocaciones de metodos
        
        LocateRegistry.createRegistry(puertoContenido);
        //Creamos el objeto donde recibimos las llamadas remotas.
        ServidorContenidoImplementacion servidorContenido = new ServidorContenidoImplementacion("rmi://"+direccionIp+":"+puertoContenido+"/ServidorContenido");
        
        //Buscamos el servidor intermediario para utilizarlo
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
             case 1: //Descargar archivo
                System.out.print("Ingrese el nombre del archivo: ");
                String nombreArch = in.readLine();
                InfoArchivo infoArch = servidor.BuscarArchivo(nombreArch); //El servidor intermediario busca el archivo
                if (infoArch == null)
                {
                    System.out.println("Archivo no encontrado.");
                }
                else //Si encontramos el archivo lo descargamos
                {
                    System.out.println(infoArch);
                    DescargarArchivo(infoArch, servidor);
                }
                
                break;
             case 2: //Registrar archivo
                 //Ingresar datos del archivo
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
               
                boolean respuesta = servidor.RegistrarServidorContenido(archivo); //Le pedimos al servidor intermediario que nos registre para compartir el archivo.
                System.out.println(respuesta? "Registrado satisfactoriamente": "Error al registrar archivo"); 
                break;
             case 3: //Salir
                 opcion = -1;
                 break;
             }
        }while ( opcion != -1);
    }
                  catch (NotBoundException ex) {
                      Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (MalformedURLException ex) {
                      Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (RemoteException ex) {
                      Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (IOException ex) {
            Logger.getLogger(MainCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 }
   

