package Zad2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RockPaperScissorsServer {
    public static void main(String[] args) {
        System.out.println("Rock Paper Scissors Server is running...");

        try(DatagramSocket socket = new DatagramSocket(5555);){
            byte buffer[] = new byte[1024];

            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String requestMessage = new String(request.getData(), 0, request.getLength());
                String clientName = requestMessage.split(":")[0];
                Move clientMove = Move.valueOf(requestMessage.split(":")[1]);
                System.out.println("Client: " +  clientName + " sent move: " + clientMove);
                Move serverMove = Move.getRandomMove();
                boolean result = serverMove.beats(clientMove);

                String responseMessage;
                if(result){
                    responseMessage = "Server move: ".concat(serverMove.getDisplayName()).concat("You lose!");
                }else{
                    responseMessage = "Server move: " + serverMove.getDisplayName() + (serverMove == clientMove ? " It's a tie!" : " You win!");
                }

                DatagramPacket response = new DatagramPacket(responseMessage.getBytes(), responseMessage.length(), request.getAddress(), request.getPort());
                socket.send(response);
            }
        } catch (SocketException e){
            System.err.println(e.getMessage());
        } catch (IOException e){
            System.err.println(e.getMessage());
        }

    }
}
