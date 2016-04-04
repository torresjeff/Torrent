package Cliente;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class ManejadorConexiones implements Runnable {

        private String nombreArchivo;
        private int parte;
        private int partes;
	private Socket socket;

    public ManejadorConexiones(String nombreArchivo, int parte, int partes, Socket socket) {
        this.nombreArchivo = nombreArchivo;
        this.parte = parte;
        this.partes = partes;
        this.socket = socket;
    }
	


    public ManejadorConexiones(Socket socket)
    {
            this.socket = socket;
    }


    @Override
    public void run()
    {

        BufferedReader inFromClient;
            try {
                    //inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                    
                    File file = new File(nombreArchivo+".part"+parte);
                    byte[] mybytearray = new byte[(int) file.length()];
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                    bis.read(mybytearray, 0, mybytearray.length);
                    OutputStream os = socket.getOutputStream();
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    socket.close();
                    
                    /*String clientMessage = inFromClient.readLine();
                    System.out.println("Client message: " + clientMessage);*/
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }



    }

}
