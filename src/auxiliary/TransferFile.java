package auxiliary;

import java.io.*;

public class TransferFile {

    public static void send(File file, ObjectOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeLong(file.length());
            dataOutputStream.writeUTF(file.getName());
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[64 * 1024];
            int count;
            while ((count = inputFile.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, count);
            }
            dataOutputStream.flush();
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File receive(String catalog, ObjectInputStream dataInputStream) {
        try {
            long fileSize = dataInputStream.readLong();
            String fileName = catalog + dataInputStream.readUTF();
            byte[] buffer = new byte[64 * 1024];
            FileOutputStream outputFile = new FileOutputStream(fileName);
            int count, total = 0;
            while ((count = dataInputStream.read(buffer)) != -1) {
                total += count;
                outputFile.write(buffer, 0, count);
                if (total == fileSize) {
                    break;
                }
            }
            outputFile.flush();
            outputFile.close();
            return new File(fileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}