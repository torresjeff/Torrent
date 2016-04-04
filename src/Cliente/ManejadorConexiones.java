package Cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ManejadorConexiones implements Runnable {

	private Socket socket;
	
	public ManejadorConexiones(Socket socket)
	{
		this.socket = socket;
	}
	
	
	@Override
	public void run()
	{
            
            BufferedReader inFromClient;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
			
			String clientMessage = inFromClient.readLine();
			System.out.println("Client message: " + clientMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
            
	}

}
