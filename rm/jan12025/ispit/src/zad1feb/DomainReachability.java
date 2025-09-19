package zad1feb;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.*;

public class DomainReachability {

    public static void main(String[] args) {
        String path = "/home/marko/Documents/rm/jan12025/ispit/src/Zad1/domains.txt";
        try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            BufferedWriter output = new BufferedWriter(new FileWriter(new File("reachable.txt")));
        ){
            List<java.util.Map.Entry<String, Long>> reachableDomainsWithTime = new ArrayList<>();

            int reachableCounter = 0;
            int counter = 0;
            String line;
            while((line = input.readLine()) != null){
                try {
                    InetAddress address = InetAddress.getByName(line.trim());

                    if (address == null) {
                        continue;
                    }
                    long startTime = System.currentTimeMillis();
                    boolean reachable = address.isReachable(3000);
                    long endTime = System.currentTimeMillis();
                    if (reachable) {
                        reachableCounter++;
                        reachableDomainsWithTime.add(new AbstractMap.SimpleEntry<>(line.trim(), endTime - startTime));
                    }
                }catch (UnknownHostException e){
                    System.err.println(e.getMessage());
                } catch (IOException e){
                    System.err.println(e.getMessage());
                }
                counter++;
            }
            reachableDomainsWithTime.sort(Map.Entry.comparingByValue());
            output.write("Ukupan broj dostupnih domena: " + reachableCounter + "\n");
            output.write("Ukupan broj nedostupnih domena: " + (counter - reachableCounter) + "\n\n");
            output.write("Dostupni domeni sa vremonom odziva: \n");
            for(var a : reachableDomainsWithTime){
                output.write(a.getKey() + " - " + a.getValue() + "\n");
            }
            output.write("Top 3 najbrza domena: \n");
            for(int i = 0; i < 3; i++){
                if(reachableDomainsWithTime.size() <= i){
                    break;
                }
                output.write(i + "." + reachableDomainsWithTime.get(i).getKey() + " - " + reachableDomainsWithTime.get(i).getValue() + "\n");
            }
        }
        catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }
        catch(IOException e){
                System.err.println(e.getMessage());
        }
    }
}
