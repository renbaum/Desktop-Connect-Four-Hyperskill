package four;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

enum CustomColor {
    DARKERGREEN(new Color(156, 204, 80)),
    LIGHTERGREEN(new Color(156, 204, 80)),
//    LIGHTERGREEN(new Color(175, 213, 130)),
    HIGHLIGTED(new Color(1, 231, 119));

    private final Color color;

    CustomColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

public class ConnectFour extends JFrame {

    int currentPlayer = 0;
    JButton[][] board = new JButton[6][7];
    boolean win = false;

    private int[][] getGrid(String player){
        int [][] grid = new int[6][7];
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 7; j++){
                if(board[i][j].getText().equals(player)){
                    grid[i][j] = 1;
                }else {
                    grid[i][j] = 0;
                }
            }
        }
        return grid;
    }

    private boolean checkWin(String player) {
        int grid[][] = getGrid(player);

        // row-wise
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (Math.abs(Arrays.stream(grid[i], j, j + 4).sum()) == 4) {
                    board[i][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i][j+1].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i][j+2].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i][j+3].setBackground(CustomColor.HIGHLIGTED.getColor());

                    return true;
                }
            }
        }
        // column-wise
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (Math.abs(grid[i][j] + grid[i + 1][j] + grid[i + 2][j] + grid[i + 3][j]) == 4) {
                    board[i][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i+1][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i+2][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i+3][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    return true;
                }
            }
        }
        // diagonal \
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (Math.abs(grid[i][j] + grid[i + 1][j + 1] + grid[i + 2][j + 2] + grid[i + 3][j + 3]) == 4) {
                    board[i][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i + 1][j + 1].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i + 2][j + 2].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i + 3][j + 3].setBackground(CustomColor.HIGHLIGTED.getColor());
                    return true;
                }
            }
        }
        // diagonal /
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (Math.abs(grid[i][j] + grid[i - 1][j + 1] + grid[i - 2][j + 2] + grid[i - 3][j + 3]) == 4) {
                    board[i][j].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i - 1][j + 1].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i - 2][j + 2].setBackground(CustomColor.HIGHLIGTED.getColor());
                    board[i - 3][j + 3].setBackground(CustomColor.HIGHLIGTED.getColor());
                    return true;
                }
            }
        }
        return false;
    }

    private void reset(){
        CustomColor color = CustomColor.DARKERGREEN;
        win = false;
        currentPlayer = 0;

        for(int i = 0; i<6; i++) {
            for (int j = 0; j < 7; j++) {
                color = color == CustomColor.LIGHTERGREEN ? CustomColor.DARKERGREEN : CustomColor.LIGHTERGREEN;
                board[i][j].setText(" ");
                board[i][j].putClientProperty("isOccupied", false);
                board[i][j].setFocusPainted(false);
                board[i][j].setBackground(color.getColor());
            }
        }
    }

    public ConnectFour() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setTitle("Connect Four");

        JPanel outerPanel = new JPanel(new BorderLayout());
        JPanel innerPanel = new JPanel(new GridLayout(6, 7));
        outerPanel.add(innerPanel, BorderLayout.CENTER);
        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        outerPanel.add(resetButton, BorderLayout.SOUTH);

        add(outerPanel, BorderLayout.CENTER);
        //setLayout(outerPanel);
        for(int i = 6; i>0; i--){
            for(int j = 0; j<7; j++){
                board[i-1][j] = new JButton(" ");
                String title = String.format("%c%d", j + 'A', i);
                board[i-1][j].setName("Button" + title);
                board[i-1][j].putClientProperty("column", j);
                board[i-1][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        setStone((int)button.getClientProperty("column"));
                    }
                });
                innerPanel.add(board[i-1][j]);
            }
        }
        reset();
        setVisible(true);
    }

    private JButton getButton(String name){
        for(Component c : getContentPane().getComponents()){
            if (c instanceof JButton && ((JButton) c).getName().equals(name)) {
                return (JButton) c;
            }
        }
        return null;
    }

    private void setButton(JButton button){
        Font font = button.getFont().deriveFont(Font.BOLD).deriveFont(20.0f);
        button.setFont(font);
        button.putClientProperty("isOccupied", true);
        if(currentPlayer == 0){
            currentPlayer = 1;
            button.setText("X");
            win = checkWin("X");
        }else{
            currentPlayer = 0;
            button.setText("O");
            win = checkWin("O");
        }
    }

    public void setStone(int col){
        JButton button = null;

        if(win) return;

        for(int i = 0; i<6; i++){
            String name = String.format("Button%c%d", col + 'A', i);
            button = board[i][col];
            if((boolean)button.getClientProperty("isOccupied") == false){
                setButton(button);
                return;
            }
        }
    }
}