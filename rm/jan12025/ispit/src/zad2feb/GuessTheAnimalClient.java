package zad2feb;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GuessTheAnimalClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            // Send name to server
            out.println(name);

            System.out.println(name + ", guess the animal I chose:");

            while (true) {
                System.out.print("Your guess: ");
                String guess = scanner.nextLine().trim();

                if (guess.isEmpty()) {
                    continue;
                }

                // Send guess to server
                out.println(guess);

                // Get server response
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server disconnected");
                    break;
                }

                System.out.println("Server: " + response);

                if (response.equals("Correct!")) {
                    System.out.println("Congratulations! You won!");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            System.err.println("Make sure the server is running on " + SERVER_HOST + ":" + SERVER_PORT);
        }
    }
}
