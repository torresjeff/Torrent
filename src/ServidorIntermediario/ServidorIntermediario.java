/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorIntermediario;

import Utils.Directorio;
import Utils.ManejadorArchivos;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manuela
 */
public class ServidorIntermediario
{
    public static void CrearDirectorio(Directorio directorio)
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("directorio.bin");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            directorio = (Directorio) in.readObject();
            in.close();
            fileIn.close();
            
            System.out.println(directorio);
        } catch(IOException i)
        {
            i.printStackTrace();
            return;
        } catch(ClassNotFoundException c)
        {
            System.out.println("Directorio class not found");
            c.printStackTrace();
            return;
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            String direccionIp = "192.168.0.7"; // Pc manu
            //String direccionIp = "192.168.0.4"; // Pc lore
            //String direccionIp = "192.168.0.14"; // Pc sebas
            int puerto = 8080;
            
            Directorio directorio = new Directorio();
            
            //CrearDirectorio(directorio);
            
            FileInputStream fileIn = new FileInputStream("directorio.bin");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            directorio = (Directorio) in.readObject();
            in.close();
            fileIn.close();
            
            System.out.println(directorio);
            
            LocateRegistry.createRegistry(puerto);
            
            ManejadorArchivos.GenerarHash("directorio.bin");
            
            ServidorIntermediarioImplementacion servidor = new
                    ServidorIntermediarioImplementacion("rmi://"+direccionIp+":"+puerto+"/ServidorIntermediario", directorio);
                    //ServidorIntermediarioImplementacion("rmi://127.0.0.1:8080/ServidorIntermediario", directorio);
            
            /*try {
            //Serializar
            /*InfoArchivo infoArchivo = new InfoArchivo();
            infoArchivo.nombre = "limon.mp3";
            infoArchivo.hash = "fdas938fd";
            infoArchivo.servidoresContenido.add(new Direccion("192.168.0.1", 8080));
            Directorio directorio = new Directorio();
            directorio.agregarArchivo(infoArchivo);
            try
            {
            FileOutputStream fileOut = new FileOutputStream("directorio.bin");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(directorio);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in directorio.bin");
            }catch(IOException i)
            {
            i.printStackTrace();
            }*/
            
            //Deserializar
            /*try
            {
            Directorio directorio = new Directorio();
            FileInputStream fileIn = new FileInputStream("directorio.bin");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            directorio = (Directorio) in.readObject();
            in.close();
            fileIn.close();
            
            System.out.println(directorio);
            }catch(IOException i)
            {
            i.printStackTrace();
            return;
            }catch(ClassNotFoundException c)
            {
            System.out.println("Directorio class not found");
            c.printStackTrace();
            return;
            }*/
            /*ServerSocket server = new ServerSocket(8080);
            while (true)
            {
            Socket socket = server.accept();
            
            ManejadorConexiones manejador = new ManejadorConexiones(socket);
            new Thread(manejador).start();
            }*/
            
            
            /*} catch (IOException ex) {
            Logger.getLogger(ServidorIntermediario.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorIntermediario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServidorIntermediario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorIntermediario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorIntermediario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
