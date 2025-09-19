package Zad1;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DomainAnalyzer {
    public static void main(String[] args) {
        try (
            InputStreamReader input = new InputStreamReader(new FileInputStream(new File("/home/marko/Documents/rm/jan12025/ispit/src/Zad1/domains.txt")));
            BufferedReader reader = new BufferedReader(input);
            BufferedWriter output = new BufferedWriter(new FileWriter(new File("ip.with.domains.txt")));
        ){
            String line;
            while((line = reader.readLine()) != null){
                try{
                    InetAddress address = InetAddress.getByName(line.trim());
                    String ipAdress = address.getHostAddress();

                    output.write(line + ": " + ipAdress + "\n");
                } catch (UnknownHostException e) {
                    System.err.println(e.getMessage());
                }
            }
            output.flush();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e){
            System.err.println(e.getMessage());
        }



    }
}
