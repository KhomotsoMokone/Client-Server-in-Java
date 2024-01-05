package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * 
 */

/**
 * @author khomo The ZEDEMClientPane Class for the GUI of the Project which
 *         contains... MANAGEGUI method for the GUI layout
 */
public class ZEDEMClientPane extends GridPane {

	// Variables and Nodes Declarations
	private Socket socket = null;
	private PrintWriter PW = null;
	private BufferedReader BR = null;
	private DataInputStream DIS = null;
	private Label NAME = null;
	private Label PASSWEORD = null;
	private TextField Name = null;
	private TextField Password = null;
	private TextField CONNECTIONSTATUS = null;
	private Button LOGIN = null;
	private Button PULL = null;
	private TextArea The_List = null;
	private TextField ID = null;
	private Button DOWNLOAD = null;
	private Button LOGOUT = null;
	private String[] array;

	// The Constructor
	public ZEDEMClientPane() {
		MANAGEGUI();
		try {
			socket = new Socket("localhost", 2021);
			PW = new PrintWriter(socket.getOutputStream(), true);
			BR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DIS = new DataInputStream(socket.getInputStream());

			// Setting The LogIn On Action
			LOGIN.setOnAction(e -> {

				PW.println("CONNECT" + " " + Name.getText() + " " + Password.getText());
				PW.flush();
				String Response = "";
				try {
					Response = BR.readLine();
					System.out.println("The response is" + Response);
					CONNECTIONSTATUS.appendText(Response+ "  ");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			// Setting the Pull Button on Action
			PULL.setOnAction(e -> {
				PW.println("PULL");
				PW.flush();

				try {
					String Response = BR.readLine().replace("#", "\n");
					array = Response.split("\n");
					for (String line : array) {
						System.out.println("The Response is : " + line);
						The_List.appendText(line + "\n");
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			// Setting the Download Button on Action
			DOWNLOAD.setOnAction(e -> {
				PW.println("DOWNLOAD" + " " + ID.getText());
				PW.flush();

				try {
					String Response = BR.readLine();
					System.out.println("The FILE SIZE IS " + Response);
					Integer FILESIZE = Integer.parseInt(Response);
					String FILEName = "";
					for (String line : array) {
						StringTokenizer ST = new StringTokenizer(line);
						String ID_NUM = ST.nextToken();
						String FileName = ST.nextToken();
						if (ID_NUM.equals(ID.getText())) {
							FILEName = FileName;
						}

					}

					File files = new File("data/client/" + FILEName);
					System.out.println("FILE TO SAVE IS " + files.getName());
					FileOutputStream FOS = new FileOutputStream(files);
					byte[] bytes = new byte[2048];
					int last = 0;
					int TotalBytes = 0;
					while (TotalBytes != FILESIZE) {
						last = DIS.read(bytes, 0, bytes.length);
						FOS.write(bytes);
						FOS.flush();
						TotalBytes += last;
					}
					FOS.close();
					CONNECTIONSTATUS.appendText("DOWNLOADED THE FILE  ");
					System.out.println("DOWNLOADED THE FILE");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			// Setting the Logout Button on Action
			LOGOUT.setOnAction(e -> {
				PW.println("LOGOUT");
				PW.flush();
				try {
					String Response = BR.readLine();
					CONNECTIONSTATUS.appendText(Response + "  ");
					System.out.println("The response is : " + Response);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					// Closing all the Streams
					try {
						PW.close();
						BR.close();
						DIS.close();
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// The method to manage the GUI and the Layouts
	public void MANAGEGUI() {

		// Instantiating the Nodes
		NAME = new Label("NAME ");
		PASSWEORD = new Label("PASSWORD");
		Name = new TextField();
		Password = new TextField();
		LOGIN = new Button("LOGIN");
		PULL = new Button("PULL");
		ID = new TextField();
		DOWNLOAD = new Button("DOWNLOAD");
		LOGOUT = new Button("LOGOUT");
		The_List = new TextArea();
		CONNECTIONSTATUS = new TextField();

		// Adding the nodes to the Pane
		add(NAME, 0, 0);
		add(PASSWEORD, 0, 1);
		add(Name, 1, 0);
		add(Password, 1, 1);
		add(LOGIN, 0, 2);
		add(CONNECTIONSTATUS, 0, 3);
		add(PULL, 0, 4);
		add(The_List, 0, 5);
		add(ID, 0, 6);
		add(DOWNLOAD, 0, 7);
		add(LOGOUT, 0, 8);

	}
}
