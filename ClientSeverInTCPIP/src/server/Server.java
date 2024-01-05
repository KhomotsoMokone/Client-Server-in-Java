package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 */

/**
 * @author khomo The Server Class for Accepting the connection and listerning to
 *         ports . It allows multiple Clients to Connect to it . Makes use of
 *         Multi-threading
 */
public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket SS = null;
		while (true) {
			try {

				SS = new ServerSocket(2021);
				Socket server = SS.accept();
				ZEDEMHandler ZH = new ZEDEMHandler(server);
				Thread T = new Thread(ZH);
				T.start();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (SS != null) {
					try {
						SS.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
