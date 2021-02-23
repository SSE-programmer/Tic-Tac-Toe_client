import javax.swing.*;
import java.awt.*;

public class GameInterface extends JFrame {

    public GameButton[][] buttons;
    private JLabel statusLabel, extraLabel;
    private final int SIZE = 3;

    public GameInterface() {
        super("Tic-Tac-Toe");
        createGUI();
    }

    public void setStatusLabel(String status){
        statusLabel.setText(status);
    }
    public void setExtraLabel(String status){
        statusLabel.setText(status);
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("res/icon.png");
        setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE, 2, 2));
        gridPanel.setBackground(Color.DARK_GRAY);

        statusLabel = new JLabel("Ожидание подключения противника...");
        statusLabel.setPreferredSize(new Dimension(600, 40));
        statusLabel.setMinimumSize(new Dimension(100, 20));

        /*Лейбл для вывода ошибок, потом удалить*/
        extraLabel = new JLabel();
        extraLabel.setPreferredSize(new Dimension(600, 40));
        extraLabel.setMinimumSize(new Dimension(100, SIZE));
        extraLabel.setForeground(Color.RED);

        gridPanel.setPreferredSize(new Dimension(600, 600));

        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(extraLabel, BorderLayout.SOUTH);

        buttons = new GameButton[SIZE][];
        for (int i = 0; i < SIZE; i++) {
            buttons[i] = new GameButton[SIZE];
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new GameButton(i, j);
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setEnabled(false);

                gridPanel.add(buttons[i][j]);
            }
        }
        setSize(600, 600);
    }
}
