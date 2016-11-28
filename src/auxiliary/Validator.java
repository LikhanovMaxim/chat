package auxiliary;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    public static boolean checkIP(String ip) {
        String correctIp = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        String point = "\\.";
        Pattern p = Pattern.compile("^" + correctIp + point + correctIp + point + correctIp + point + correctIp + "$");
        return (p.matcher(ip)).matches();
    }

    public static boolean checkPort(String port) {
        String correctPort = "^[0-9][0-9][0-9][0-9]$";
        Pattern p = Pattern.compile(correctPort);
        return (p.matcher(port)).matches();
    }

}
