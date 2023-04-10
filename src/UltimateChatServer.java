import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class UltimateChatServer {

    ServerSocket serverSocket;
    ArrayList<Client> clients = new ArrayList<>();

    public UltimateChatServer() throws IOException {
        serverSocket = new ServerSocket(1984);// создаем серверный сокет на порту 1984
    }

    public static void main(String[] args) throws IOException {
        new UltimateChatServer().run();

    }

    public void run() {
        while (true) {
            System.out.println("Waiting...");
            // ждем клиента из сети
            try {
                Socket s = serverSocket.accept();
                System.out.println("Client connected!");
                // создаем клиента на своей стороне
                clients.add(new Client(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    void tellChat(String message) {
        for (Client client : clients) {
            client.receive(message);
        }
    }

    class Client implements Runnable {
        Socket socket;
        Scanner in;
        PrintStream out;

        public Client(Socket socket) {

            this.socket = socket;
            new Thread(this).start();// запускаем поток
        }

        void receive(String message) {
            out.println(message);
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());// получаем поток ввода и создаем удобное средство ввода
                out = new PrintStream(socket.getOutputStream());// получаем поток вывода и создаем удобное средство  вывода
                // читаем из сети и пишем в сеть
                out.println("Welcome to chat!");
                String input = in.nextLine();
                while (!(input == "quit")) {
                    tellChat(input);
                    input = in.nextLine();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
