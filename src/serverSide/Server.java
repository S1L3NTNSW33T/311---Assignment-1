package serverSide;

import java.net.*;
import java.io.*;
import java.util.*;

import utilities.Debug;

/**
 * @author dwatson, kitty, Maryam
 * @version 2.0
 *
 * Represents a Server object to allow for communication between pairs of
 * clients.
 *
 */
public class Server {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // level 0 = normal (OFF)
        // level 1 = debug (ON)
        // this would normally be set outside of the application (through args)
        // and would include a couple more levels
        // e.g. debug.level = verbose, severe, errors, warnings, info, none
        @SuppressWarnings("unused")
        Debug debug = new Debug(1);

        ArrayList<Socket> socketList = new ArrayList<Socket>(2);
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Server up and running!");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                socketList.add(clientSocket);

                if (socketList.size() == 2) {
                    ClientHandler clientHandler = new ClientHandler(
                            socketList.get(0), socketList.get(1));
                    Thread handlerThread = new Thread(clientHandler);
                    handlerThread.start();
                    socketList.clear();
                }
            }
        } catch (SocketException e) {
            // not all exceptions are errors, just handle them gracefully!
            Debug.output("Server: Socket has been closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
