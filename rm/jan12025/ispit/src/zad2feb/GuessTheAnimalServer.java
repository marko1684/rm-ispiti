package zad2feb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GuessTheAnimalServer {

    static final ArrayList<String> animals = loadAnimalsFromFile("/home/marko/Documents/rm/jan12025/ispit/src/zad2feb/animals.txt");

    public static final int PORT = 5555;

    public static void main(String[] args) {
        if(animals == null || animals.isEmpty()){
            System.err.println("No animals loaded. Exiting.");
            return;
        }

        // Display startup messages as per specification
        System.out.println("Welcome to server for guessing the animal!");
        System.out.println("Available animals are: " + animals);

        // Add SIGINT handler
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nServer shutting down...");
        }));

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started on port " + PORT);
            while(true){
                Socket clientSocket = serverSocket.accept();
                // Each client gets their own random animal
                String animal = animals.get((int) (Math.random() * animals.size()));
                ServerWorker s = new ServerWorker(clientSocket, animal);
                Thread thread = new Thread(s);
                thread.start();
            }
        }catch(IOException e){
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadAnimalsFromFile(String path){
        ArrayList<String> animals = new ArrayList<>();
        try(BufferedReader input = new BufferedReader(new FileReader(path))){
            String line;
            while((line = input.readLine()) != null){
                animals.add(line.trim());
            }
        }catch(IOException e){
            System.err.println("Error loading animals file: " + e.getMessage());
            return null;
        }
        return animals;
    }

    public static class ServerWorker implements Runnable {

        private final Socket client;
        private final String animal;

        ServerWorker(Socket client, String animal){
            this.client = client;
            this.animal = animal;
        }

        @Override
        public void run(){
            try(BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter toClient = new PrintWriter(client.getOutputStream(), true)){

                // Read client name first
                String clientName = fromClient.readLine();
                if(clientName == null){
                    return; // Client disconnected
                }
                System.out.println("New client connected: " + clientName + " (" + client.getInetAddress() + ":" + client.getPort() + ") - Target: " + animal);

                String request;
                while((request = fromClient.readLine()) != null){
                    if(request.trim().isEmpty()){
                        continue;
                    }
                    String response = check(request.trim().toLowerCase());
                    toClient.println(response);

                    // Exit if correct guess
                    if(response.equals("Correct!")){
                        System.out.println(clientName + " guessed correctly: " + animal);
                        break;
                    }
                }

            }catch(IOException e){
                System.err.println("Error handling client: " + e.getMessage());
            }finally{
                try {
                    client.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        public String check(String guess){
            int comparison = this.animal.compareTo(guess);
            if(comparison > 0){
                return "Too early in alphabet";
            }else if(comparison < 0){
                return "Too late in alphabet";
            }else{
                return "Correct!";
            }
        }
    }
}