/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread para escuchar constantemente las conexiones en nuestros puertos y compartir archivos.
 * @author sebastiangracia
 */
public class ThreadEscuchaConexiones implements Runnable {

    private int puerto;
    private String nombreArchivo;
    private int parte, partes;

    public ThreadEscuchaConexiones(String nombreArchivo, int parte, int partes, int puerto) {
        this.puerto = puerto;
        this.nombreArchivo = nombreArchivo;
        this.parte = parte;
        this.partes = partes;
    }

    
    @Override
    public void run() {
        try {
            //ServerSocket server = new ServerSocket(8080);
            ServerSocket server = new ServerSocket(puerto);
            
            while (true)
            {
                Socket socket = server.accept();
                System.out.println("Conexion realizada con " + socket.getRemoteSocketAddress());
                ManejadorConexiones manejador = new ManejadorConexiones(nombreArchivo, parte, partes, socket);
                new Thread(manejador).start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ThreadEscuchaConexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
