package main;

import java.util.Scanner;

import auxiliary.Enter;
import server.Server;
import client.Client;

public class App {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Запустить программу в режиме сервера или клиента? (S(erver) / C(lient)). Или выйти. (E)xit");
        while (true) {
            char answer = Character.toLowerCase(scan.nextLine().charAt(0));
            switch (answer) {
                case 's': {
                    new Server(Enter.port(scan));
                    break;
                }
                case 'c': {
                    new Client(Enter.port(scan));
                    break;
                }
                case 'e': {
                    return;
                }
                default: {
                    System.out.println("Некорректный ввод. Повторите.");
                }
            }
            System.out.println("Запустить программу в режиме сервера или клиента? (S(erver) / C(lient))Или выйти. (E)xit");
        }
    }


}