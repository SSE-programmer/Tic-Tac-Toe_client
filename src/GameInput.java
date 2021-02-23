import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.Socket;

public class GameInput {

    private final int SIZE = 3;

    public boolean connection = true;

    public boolean isWin() {
        return win;
    }

    private boolean win = false;

    GameInterface gameInterface;

    GameInput(GameInterface _gameInterface) {
        gameInterface = _gameInterface;
    }

    public boolean read(MyDataInputStream inputStream, Socket socket) {
        System.out.println("Ждём ответ от сервера...");

        String in = null;

        try {
            in = inputStream.myReadUTF();

            System.out.println("Получено сообщение от сервера: " + in);
        } catch (EOFException e) {
            System.out.println("Данные от сервера " + Client.socket.getInetAddress().getHostName()
                    + " " + Client.socket.getPort() + " полученны не полностью, проверьте соединение.");

            return false;
        } catch (UTFDataFormatException e) {
            System.out.println("Данные от клиента " + Client.socket.getInetAddress().getHostName() + " " + Client.socket.getPort() + " полученны в неверном формате.");

            return false;
        } catch (IOException e) {
            System.out.println("Нет связи с клиентом: " + Client.socket.getInetAddress().getHostName() + " " + Client.socket.getPort() + ".");

            return false;
        }

        if (in != null) {
            adapter(in, socket);
        }

        return true;
    }

    private void adapter(String in, Socket socket) {
        String[] args = in.split("/");
        if (args.length > 0) {
            if (args.length == 4) {
                if (args[2].equals("_")) {
                    if (args[0].equals(args[1])) {
                        gameInterface.setStatusLabel("Ваш ход (" + args[1] + ")");
                    } else {
                        gameInterface.setStatusLabel("Ход противника (" + args[1] + ")");
                    }

                    setActiveBoard(args[0].equals(args[1]));
                } else {
                    win = true;

                    if (args[2].equals(args[0])) {
                        gameInterface.setStatusLabel("Вы победили!");

                        System.out.println("Вы победили!");
                    } else {
                        gameInterface.setStatusLabel("Вы проиграли!");

                        System.out.println("Вы проиграли!");
                    }

                    setActiveBoard(false);
                }


                setBoard(args[3]);
            } else if (args.length == 2) {
                if (args[0].equals("error")) {
                    if (args[1].equals("opponent_connection")) {
                        System.out.println("Оппонент отключился от сервера. Игра закончена.");
                        gameInterface.setStatusLabel("Оппонент отключился от сервера. Игра закончена.");
                        try {
                            socket.close();
                            connection = false;
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    } else if (args[1].equals("bad_data")) {
                        System.out.println("Сервер получил подозрительные данные от клиента. " +
                                "Сессия будет закрыта");

                        gameInterface.setStatusLabel("Сервер получил подозрительные данные от одного из клиентов. " +
                                "Игра закончена.");
                        try {
                            socket.close();
                            connection = false;
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                } else if (args[0].equals("servers")) {
                    for (int k = 0; k < args[1].length(); k++) {
                        if (args[1].charAt(k) == '1')
                        {
                            ServersList.valid[k] = true;
                        } else if (args[1].charAt(k) == '0') {
                            ServersList.valid[k] = false;
                        } else {
                            badData(socket);
                        }
                    }
                } else {
                    badData(socket);
                }
            }
        }
    }

    private void badData(Socket socket) {
        System.out.println("Плохие данные от сервера");
        gameInterface.setStatusLabel("Подозрительное поведение сервера. Соединение будет разорвано.");
        try {
            socket.close();
            connection = false;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    void setActiveBoard(boolean value) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                gameInterface.buttons[i][j].setEnabled(value);
            }
        }
    }

    private void setBoard(String board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = board.charAt(i * SIZE + j);
                if (c != '_') {
                    gameInterface.buttons[i][j].setText(Character.toString(c));
                }
            }
        }
    }
}
