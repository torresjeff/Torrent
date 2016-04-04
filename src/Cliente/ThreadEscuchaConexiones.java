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
 *
 * @author sebastiangracia
 */
public class ThreadEscuchaConexiones implements Runnable {

    private int puerto;

    public ThreadEscuchaConexiones(int puerto)
    {
        this.puerto = puerto;
    }
    
    
    
    @Override
    public void run() {
        try {
            //ServerSocket server = new ServerSocket(8080);
            ServerSocket server = new ServerSocket(puerto);
            
            Socket socket = server.accept();
            System.out.println("Conexion realizada con " + socket.getRemoteSocketAddress());
            ManejadorConexiones manejador = new ManejadorConexiones(socket);
            new Thread(manejador).start();

        } catch (IOException ex) {
            Logger.getLogger(ThreadEscuchaConexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
