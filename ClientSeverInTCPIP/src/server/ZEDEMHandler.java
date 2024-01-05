package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 */

/**
 * @author khomo The ZEDEMHandler Class that implements the Runnable interface
 *         for the Servers side of the project It Override the run method .
 */
public class ZEDEMHandler implements Runnable {
	// Variables and Streams declarations
	private Socket server = null;
	private PrintWriter PW = null;
	private BufferedReader BR = null;
	private DataOutputStream DOS = null;

	// The Constructor
	public ZEDEMHandler(Socket server) {
		this.server = server;

		try {
			// Instantiating the Streams
			PW = new PrintWriter(server.getOutputStream(), true);
			BR = new BufferedReader(new InputStreamReader(server.getInputStream()));
			DOS = new DataOutputStream(server.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean Continue = true; 
		boolean Authanticate = false; // To Authenticate the user.
		try {
			while (Continue) // Allow many Commands to be sent.
			{
				String Response = BR.readLine();
				StringTokenizer TOKENS = new StringTokenizer(Response);
				String Command = TOKENS.nextToken(); // The command from the user
				System.out.println("The Command is" + Command);
				if (Command.equals("CONNECT")) {
					String Name = TOKENS.nextToken();
					String Password = TOKENS.nextToken();
					System.out.println("Name from Client is " + Name + " and Password is " + Password);

					Scanner Scan = new Scanner(new File("data/server/users.txt"));
					while (Scan.hasNext()) {
						String Line = Scan.nextLine();
						System.out.println("The line from the file is " + Line);
						StringTokenizer ST = new StringTokenizer(Line);
						String name = ST.nextToken();
						String Pass = ST.nextToken();
						System.out.println("Name from Client is " + name + " and Password is " + Pass);
						if ((Name.equals(name)) && (Password.equals(Pass))) {
							PW.println("Yes <Successfully Logged In>");
							PW.flush();
							Authanticate = true;
						} else {
							PW.println("No <Invalid User>");
							PW.flush();
						}

					}
				} else if (Command.equals("PULL")) {
					if (Authanticate) {
						Scanner Scan = new Scanner(new File("data/server/List.txt"));
						String files = "";
						while (Scan.hasNext()) {
							String Line = Scan.nextLine();
							files += Line + "#";
						}
						System.out.println(files + "\n");
						PW.println(files);
						PW.flush();
					} else {
						PW.println("No <Invalid User>");
						PW.flush();
					}

				} else if (Command.equals("DOWNLOAD")) {
					if (Authanticate) {
						String ID = TOKENS.nextToken();
						System.out.println("The ID is " + ID);

						Scanner Scan = new Scanner(new File("data/server/List.txt"));
						String FILENAME = "";
						while (Scan.hasNext()) {
							String Lines = Scan.nextLine();
							StringTokenizer ST = new StringTokenizer(Lines);
							String IDE = ST.nextToken();
							String FileName = ST.nextToken();
							if (ID.equals(IDE)) {
								FILENAME = FileName;
							}
						}
						System.out.println("The File selected is " + FILENAME);

						File FILE = new File("data/server/" + FILENAME);
						System.out.println("File NAme is " + FILE.getName());
						if (FILE.exists()) {
							long FileSize = FILE.length();
							PW.println(FileSize);
							PW.flush();

							FileInputStream FIS = new FileInputStream(FILE);

							byte[] bytes = new byte[2048];
							int last = 0;
							while ((last = FIS.read(bytes)) > 0) {
								DOS.write(bytes, 0, last);
								DOS.flush();
							}
							FIS.close();
							System.out.println("Downloaded the File to the Client");

						}

					} else {
						PW.println("No <Invalid User>");
						PW.flush();
					}
				} else if (Command.equals("LOGOUT")) {
					if (Authanticate) {
						PW.println("Yes <Succesfully Logged Out>");
						PW.flush();

						PW.close();
						BR.close();
						DOS.close();
						server.close();
						Authanticate = false;
					} else {
						PW.println("No <Invalid User>");
						PW.flush();
					}
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
