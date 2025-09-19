package Zad2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class RockPaperScissorsClient {

    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in);
            DatagramSocket socket = new DatagramSocket();){
            System.out.println("Welcome to the Rock Paper Scissors Client");
            System.out.println("Enter your name: ");

            String name;
            name = sc.nextLine().trim();
            while (true){
                System.out.println("Enter your move: ");
                String move = sc.nextLine().trim().toLowerCase();

                if(move.equals("exit")){
                    System.out.println("Exiting the game...");
                    break;
                }

                if(!move.equals("rock") && !move.equals("paper") && !move.equals("scissors")){
                    System.out.println("Invalid move. Please enter rock, paper, or scissors.");
                    continue;
                }
                socket.setSoTimeout(5000);
                String message = name + ":" + move.toUpperCase();
                byte[] messageBytes = message.getBytes();

                InetAddress serverAddress = InetAddress.getByName("localhost");
                DatagramPacket sendPacket = new DatagramPacket(messageBytes, messageBytes.length, serverAddress, 5555);

                socket.send(sendPacket);
                System.out.println("Sending packet to " + serverAddress.getHostAddress() + ":" + message);

                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                socket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Server response: " + response);
                System.out.println("-----------------------------");

            }
        } catch (SocketTimeoutException e){
            System.err.println("No response from server, please try again.");
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
