import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static Socket socket = null;
    public static DataOutputStream outputStream = null;
    public static MyDataInputStream inputStream = null;

    public static int createValidConnection() {
       try {
           Thread.sleep(2000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

        for (int i = 0; i < ServersList.ServersAddresses.length; i++) {
                try {
                    if (outputStream != null) outputStream.close();
                    if (inputStream != null) inputStream.close();
                    if (socket != null && !socket.isClosed()) socket.close();

                    if (ServersList.valid[i] == true) {
                        socket = new Socket(ServersList.ServersAddresses[i][0], Integer.parseInt(ServersList.ServersAddresses[i][1]));
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        inputStream = new MyDataInputStream(socket.getInputStream());

                        System.out.println("Клиент подключился к серверу: "
                                + ServersList.ServersAddresses[i][0] + "-"
                                + ServersList.ServersAddresses[i][1]);

                        System.out.println();
                        System.out.println("Каналы ввода и вывода инициализированны.");

                        break; //Выходим из цикла, если удалось подключиться хоть к чему-то;
                    }

                } catch (UnknownHostException e) {
                    System.out.println("Не удалось определить IP адрес хоста: "
                            + ServersList.ServersAddresses[i][0] + "-"
                            + ServersList.ServersAddresses[i][1]);
                } catch (IOException e) {
                    System.out.println("Не удалось подключиться к хосту: "
                            + ServersList.ServersAddresses[i][0] + "-"
                            + ServersList.ServersAddresses[i][1]);
                }
        }
        if (socket == null || socket.isClosed()) {
            System.out.println("Не найдено доступных подключений.");
            return -1;
        }

        return 0;
    }

    public static void main(String[] args) {
        /*Создание окна*/
        GameInterface gameInterface = new GameInterface();
        gameInterface.pack();
        gameInterface.setVisible(true);

        for (int i = 0; i < ServersList.ServersAddresses.length; i++) {
            ServersList.valid[i] = true;
        }

        if (createValidConnection() < 0) {
            gameInterface.setStatusLabel("Не удалось подключиться к серверу.");
        } else {

            GameInput gameInput = new GameInput(gameInterface);

            while (!socket.isOutputShutdown() && !gameInput.isWin() && gameInput.connection) {
                if (gameInput.read(inputStream, socket) == false) {
                    if (Client.createValidConnection() < 0) {
                        gameInterface.setStatusLabel("Не удалось подключиться к серверу.");
                        gameInput.connection = false;
                    } else {
                        gameInput.read(inputStream, socket);
                    }
                }
            }

            try {
                outputStream.close();
                inputStream.close();
                if (!socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.out.println("По каким-то причинам не удалось закрыть сокет или его каналы.");
            }

            System.out.println("Соединения и каналы клиента успешно закрыты.");
        }
    }
}
