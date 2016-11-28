package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import auxiliary.Enter;
import auxiliary.TransferFile;

public class Server {
    private ServerSocket server;
    private String catalogForStore;
    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private Set<String> nickNames = new HashSet<>();
    private Set<String> rooms = new HashSet<>();

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Введите каталог, в котором будут храниться файлы");
            catalogForStore = Enter.catalog() + "\\";
            System.out.println("Server is working");
            while (true) {
                Socket socket = server.accept();
                Connection con = new Connection(socket);
                connections.add(con);
                con.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            server.close();
            synchronized (connections) {
                for (Connection connection : connections) {
                    (connection).close();
                }
            }
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    private class Connection extends Thread {
        private Socket socket;
        private String name;
        private String room;
        private BufferedReader in;
        private PrintWriter out;
        private ObjectInputStream inFile;
        private ObjectOutputStream outFile;

        Connection(Socket socket) {
            try {
                Initialization(socket);

                name = enterName();
                nickNames.add(name);
                out.println("Name is free!");
                System.out.println(name + " joined to this server");

                sendAvailableRooms();

                room = in.readLine();
                rooms.add(room);
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

        private void Initialization(Socket socket) throws IOException {
            this.socket = socket;
            inFile = new ObjectInputStream(socket.getInputStream());
            outFile = new ObjectOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }

        private String enterName() throws IOException {
            String name = in.readLine();
            while (!nameIsFree(name)) {
                out.println("Name is not free!");
                name = in.readLine();
            }
            return name;
        }

        private boolean nameIsFree(String name) {
            for (String cur : nickNames) {
                if (cur.equalsIgnoreCase(name)) {
                    return false;
                }
            }
            return true;
        }

        private void sendAvailableRooms() {
            for (String element : rooms) {
                out.println(element);
            }
            out.println("end rooms.");
        }


        private void welcomeAll() {
            Connection cur;
            synchronized (connections) {
                for (Connection connection : connections) {
                    cur = connection;
                    if (room.equalsIgnoreCase(cur.room)) {
                        (cur).out.println(name + " comes now");
                    }
                }
            }
        }

        private void sendMessage(String message) {
            Connection cur;
            synchronized (connections) {
                for (Connection connection : connections) {
                    cur = connection;
                    if (room.equalsIgnoreCase(cur.room)) {
                        (cur).out.println(name + ": " + message);
                    }
                }
            }
        }

        private void sendFileToUsersInRoom(File file) {
            Connection cur;
            synchronized (connections) {
                for (Connection connection : connections) {
                    cur = connection;
                    if (room.equalsIgnoreCase(cur.room)) {
                        (cur).out.println("file");
                        (cur).out.println(name + ": sent a file '" + file.getName() + "'");
                        TransferFile.send(file, cur.outFile);
                    }
                }
            }
        }

        private int farewellAll() {
            Connection cur;
            int countUserInRoom = 0;
            synchronized (connections) {
                for (Connection connection : connections) {
                    cur = connection;
                    if (room.equalsIgnoreCase(cur.room)) {
                        (cur).out.println(name + " has left");
                        countUserInRoom++;
                    }
                }
            }
            return countUserInRoom;
        }


        @Override
        public void run() {
            try {
                welcomeAll();
                for (String message = in.readLine(); !message.equals("exit"); message = in.readLine()) {
                    if (message.equalsIgnoreCase("file")) {
                        File file = TransferFile.receive(catalogForStore, inFile);
                        sendFileToUsersInRoom(file);
                    } else {
                        sendMessage(message);
                    }
                }
                System.out.println(name + " was disconnected from this server");
                int countUserInRoom = farewellAll();
                nickNames.remove(in.readLine());
                if (countUserInRoom <= 1) {
                    rooms.remove(in.readLine());
                }
            } catch (IOException e) {
                System.out.println("Error");
                e.printStackTrace();
            } finally {
                close();
            }
        }


        void close() {
            try {
                in.close();
                out.close();
                inFile.close();
                outFile.close();
                socket.close();
                name = null;
                connections.remove(this);
                if (connections.size() == 0) {
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
                e.printStackTrace();
            }
        }
    }
}