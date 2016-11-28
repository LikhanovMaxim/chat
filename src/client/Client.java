package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import auxiliary.Enter;
import auxiliary.Validator;
import auxiliary.TransferFile;

public class Client {
    private Socket socket;
    private String nickname, room;
    private Scanner scan;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream outFile;
    private ObjectInputStream inFile;

    public Client(int port) {
        try {
            scan = new Scanner(System.in);
            System.out.println("Введите IP для подключения к серверу.");
            System.out.println("Формат: xxx.xxx.xxx.xxx");
            String ip = enterIP();
            Initialization(port, ip);

            System.out.println("Введите свой ник:");
            nickname = EnterName();

            System.out.println("Комнаты, в которые можно войти:");
            printRooms();
            System.out.println("------------------------------------");
            System.out.println("Введите комнату, в которую хотите зайти\n" +
                    "(если её не существует, то сервер создаст новую комнату):");
            out.println(room = scan.nextLine());
            System.out.println("Подскази:\n1) Чтобы отправить файл сначала введите 'file', после его имя\n" +
                    "2) Если вы хотите выйти, то введите exit\n"+
                    "------------------------------------");

            ReceiveFromServer resend = new ReceiveFromServer(nickname);
            resend.start();
            while (inTheChat()) {
            }
            resend.setStop();
            deleteInformationFromServer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void Initialization(int port, String ip) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        outFile = new ObjectOutputStream(socket.getOutputStream());
        inFile = new ObjectInputStream(socket.getInputStream());
    }

    private String enterIP() {
        String ip = scan.nextLine();
        while (!Validator.checkIP(ip)) {
            System.out.println("Неверный формат. Повторите попытку");
            ip = scan.nextLine();
        }
        return ip;
    }

    private String EnterName() throws IOException {
        String name;
        out.println(name = scan.nextLine());
        while ((in.readLine()).equalsIgnoreCase("Name is not free!")) {
            System.out.println("Этот ник занят, введите другой ник:");
            out.println(name = scan.nextLine());
        }
        return name;
    }

    private void printRooms() throws IOException {
        for (String element = in.readLine(); !element.equalsIgnoreCase("end rooms."); element = in.readLine()) {
            System.out.println(element);
        }
    }

    private boolean inTheChat() {
        String message = scan.nextLine();
        if (message.equalsIgnoreCase("file")) {
            out.println("file");
            File file = enterFile();
            TransferFile.send(file, outFile);
        } else {
            out.println(message);
        }
        return !message.equalsIgnoreCase("exit");
    }

    private File enterFile() {
        File file;
        System.out.println("Введите имя файла:");
        for (file = new File(scan.nextLine()); !file.isFile(); ) {
            System.out.println("Неверное имя файла, пожалуйста, повторите:");
            file = new File(scan.nextLine());
        }
        return file;
    }

    private void deleteInformationFromServer() {
        out.println(nickname);      //"delete nickname "
        out.println(room);          //"delete room "
    }

    private void close() {
        try {
            in.close();
            out.close();
            outFile.close();
            inFile.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    private class ReceiveFromServer extends Thread {
        private boolean stopped;
        private String catalog;

        private ReceiveFromServer(String nickname) {
            System.out.println("Введите каталог, в котором хотите сохранить файлы из чата:");
            catalog = Enter.catalog()+ "\\";
        }

        private void setStop() {
            stopped = true;
        }

        @Override
        public void run() {
            String message;
            try {
                while (!stopped) {
                    message = in.readLine();
                    if (message.equalsIgnoreCase("file")) {
                        System.out.println(in.readLine());
                        TransferFile.receive(catalog, inFile);
                    } else {
                        System.out.println(message);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            }
        }
    }

}