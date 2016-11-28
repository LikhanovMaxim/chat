package auxiliary;

import java.io.File;
import java.util.Scanner;

public class Enter {
    public static String catalog() {
        Scanner scan = new Scanner(System.in);
        String catalog = scan.nextLine();
        while (!(new File(catalog)).isDirectory()) {
            System.out.println("Такого каталога не существет. Пожалуйста, повторите ввод:");
            catalog = scan.nextLine();
        }
        return catalog;
    }

    public static int port(Scanner scan) {
        System.out.println("Введите номер порта:");
        String port = scan.nextLine();
        while (!Validator.checkPort(port)) {
            System.out.println("Неверный формат. Повторите попытку");
            port = scan.nextLine();
        }
        return Integer.parseInt(port);

    }
}
