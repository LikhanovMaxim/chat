package testForSingleComputer;

public class Const {
    public static final int Port = 8283;
    public static final String IpForSingleComputer = "127.0.0.1";
    public static final String catalogForStore = "D:\\Учеба\\4 курс\\Компьютерные сети\\chat\\store\\file storage for server\\";
    public static final String catalogForClient = "D:\\Учеба\\4 курс\\Компьютерные сети\\chat\\store\\";
}

// D:\Учеба\4 курс\Компьютерные сети\резерв.txt
// D:\Редко используемое\Manual для ноута\040C.pdf
// D:\Учеба\курсовая 3 курс\Определения.docx
///**************************
// manual for a quick test on single computer
//********************************************
// In App.java:
// 1) new Server(Const.Port);
//  instead
//new Server(port(scan));
//********************************************
////  1.2)
// new client(Const.Port)
// instead
//new client(port);
//********************************************
// 2) In Server.java
//  catalogForStore = Const.catalogForStore;
// instead
//System.out.println("Введите каталог, в котором будут храниться файлы");
//        catalogForStore = Enter.catalog() + "\\";
//********************************************
//3.1) In client.java
//  String ip = Const.IpForSingleComputer;
//  instead
//            System.out.println("Введите IP для подключения к серверу.");
//            System.out.println("Формат: xxx.xxx.xxx.xxx");
//            String ip = enterIP();
//********************************************
//3.2)
//private String catalog = Const.catalogForClient;
//private AcceptFromServer(String nickname) {
//      catalog += nickname;
//         new File(catalog).mkdir();
//         catalog += "\\";
//}
// instead
//private ReceiveFromServer(String nickname) {
//    System.out.println("Введите каталог, в котором хотите сохранить файлы из чата:");
//    catalog = Enter.catalog()+ "\\";
//}
//********************************************