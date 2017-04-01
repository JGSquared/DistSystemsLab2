import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private int port;

	public Server(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		// all server code here
		// you should have a "left" server connection
		// and a "right" server connection
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(port);
			while (true) {
				Socket socket = listener.accept();
				System.out.println("Accepted connection from: " + socket.getInetAddress());
				ServerConnection connection = new ServerConnection(socket);
				Thread t = new Thread(connection);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class ServerConnection implements Runnable {
	private Socket s;

	public ServerConnection(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		InputStream in;
		OutputStream out;
		try {
			in = s.getInputStream();
			out = s.getOutputStream();
			while (in.read() != -1) {
				if (!Philosopher.haveLeftChopstick && !Philosopher.haveRightChopstick) {
					out.write(0);
				} else {
					out.write(1);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}