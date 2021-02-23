import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameButton extends JButton {
    private int x, y;

    public GameButton(int _x, int _y) {
        super("");
        x = _x;
        y = _y;

        addActionListener(new MoveActionListener());
    }

    public class MoveActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (Client.socket != null && !Client.socket.isClosed() && Client.outputStream != null) {
                try {
                    Client.outputStream.writeUTF(x + ":" + y);
                    Client.outputStream.flush();

                    System.out.println("Отправлены координаты: " + x + ":" + y);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}