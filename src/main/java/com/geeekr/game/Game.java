package com.geeekr.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game extends JPanel {

    private static final long serialVersionUID = 8089236299561983429L;
    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final String FONT_NAME = "Arial";
    private static final int CELL_SIZE = 64;
    private static final int MARGIN = 16;
    private static int SCORE = 0;
    private static boolean myLose = false;
    private Cell[][] cells = initCells();

    public Game() {
        setFocusable(true);
        initEvent();
        init();
    }

    private void initEvent() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    resetGame();
                }
                if (!canMove()) {
                    myLose = true;
                }
                if (!myLose) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        up();
                        break;
                    case KeyEvent.VK_DOWN:
                        down();
                        break;
                    case KeyEvent.VK_LEFT:
                        left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        right();
                        break;
                    }
                }
                if (!canMove()) {
                    myLose = true;
                }
                repaint();
            }
        });
    }

    boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Cell c = cells[x][y];
                if ((x < 3 && c.getValue() == cells[x + 1][y].getValue()) || ((y < 3) && c.getValue() == cells[x][y + 1].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在棋盘上画出图像
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                drawCells(g, cells[y][x], x, y);
            }
        }
    }

    /**
     * 画出一个方块
     * 
     * @param g
     * @param cell
     * @param x
     * @param y
     */
    private void drawCells(Graphics g2, Cell cell, int x, int y) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setColor(cell.getBackground());
        int offsetX = offset(x);
        int offsetY = offset(y);
        g.fillRoundRect(offsetX, offsetY, CELL_SIZE, CELL_SIZE, 14, 14);

        g.setColor(cell.getForeground());
        int value = cell.getValue();
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);
        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];
        if (value != 0)
            g.drawString(s, offsetX + (CELL_SIZE - w) / 2, offsetY + CELL_SIZE - (CELL_SIZE - h) / 2 - 2);

        if (myLose) {
            g.setColor(new Color(255, 255, 255, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(78, 139, 202));
            g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
            g.drawString("Game over!", 50, 130);
            g.drawString("You lose!", 64, 200);
            g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
            g.setColor(new Color(128, 128, 128, 128));
            g.drawString("Press ESC to play again", 80, getHeight() - 40);
        }

        g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        g.drawString("Score: " + SCORE, 200, 365);

        g.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
        g.drawString("Tip: press key Esc to replay", 0, 365);
    }

    private int offset(int x) {
        return x * (MARGIN + CELL_SIZE) + MARGIN;
    }

    private void init() {
        // 添加两个方块
        addCell();
        addCell();
    }

    private void resetGame() {
        myLose = false;
        SCORE = 0;
        this.cells = initCells();
        addCell();
        addCell();
    }

    private Cell[][] initCells() {
        Cell[][] cells = new Cell[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cells[i][j] = new Cell(0);
            }
        }
        return cells;
    }

    /**
     * 向棋盘添加方块
     * 
     * @param coordinates
     */
    private void addCell() {
        List<Cell> avs = availableSpace();
        if (!avs.isEmpty()) {
            int index = (int) ((Math.random() * avs.size()) % avs.size());
            Cell c = avs.get(index);
            // 2和4的出现的概率是9:1
            c.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    /**
     * 判断是否还有剩余格
     * 
     * @return
     */
    private boolean isFull() {
        return availableSpace().isEmpty();
    }

    private void down() {
        rotate(cells, 270);
        left();
        rotate(cells, 90);
    }

    private void up() {
        rotate(cells, 90);
        left();
        rotate(cells, 270);
    }

    private void right() {
        rotate(cells, 180);
        left();
        rotate(cells, 180);
    }

    private void left() {
        boolean needAdd = false;
        for (int x = 0; x < 4; x++) {
            Cell[] line = getLine(x);
            Cell[] moved = moveLine(line);
            Cell[] merged = mergeLine(moved);
            setLine(x, merged);
            if (!needAdd && !compare(line, merged)) {
                needAdd = true;
            }
        }
        if (needAdd) {
            addCell();
        }
    }

    /**
     * 旋转棋盘
     * 
     * @param arr
     * @param angle
     *            90，180,270
     */
    private void rotate(Cell[][] arr, int angle) {
        this.cells = rotation(arr, angle);
    }

    private void setLine(int x, Cell[] merged) {
        cells[x] = merged;
    }

    private boolean compare(Cell[] line, Cell[] merged) {
        if (line == merged) {
            return true;
        } else if (line.length != merged.length) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            if (line[i].getValue() != merged[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    private List<Cell> availableSpace() {
        List<Cell> avs = new LinkedList<Cell>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cells[i][j].isEmpty()) {
                    avs.add(cells[i][j]);
                }
            }
        }
        return avs;
    }

    /**
     * 合并一行相邻的两个相同的数字
     * 
     * @param line
     * @return
     */
    private Cell[] mergeLine(Cell[] line) {
        LinkedList<Cell> list = new LinkedList<Cell>();
        for (int i = 0; i < 4; i++) {
            if (line[i].isEmpty()) {
                continue;
            }
            int sum = line[i].getValue();
            if (i < 3 && sum == line[i + 1].getValue()) {
                sum *= 2;
                SCORE += sum;
                i++;
            }
            list.addLast(new Cell(sum));
        }
        if (list.size() == 0) {
            return line;
        }
        fillSize(list, 4);
        return list.toArray(new Cell[4]);
    }

    /**
     * 将不为0的数字移到一边 2，0，2，0 ---> 2,2,0,0
     * 
     * @param line
     * @return
     */
    private Cell[] moveLine(Cell[] line) {
        LinkedList<Cell> list = new LinkedList<Cell>();
        for (int i = 0; i < 4; i++) {
            if (!line[i].isEmpty()) {
                list.addLast(line[i]);
            }
        }
        if (list.size() == 0) {
            return line;
        }
        fillSize(list, 4);
        return list.toArray(new Cell[4]);
    }

    private void fillSize(LinkedList<Cell> list, int i) {
        while (list.size() < i) {
            list.addLast(new Cell());
        }
    }

    private Cell[] getLine(int x) {
        return cells[x];
    }

    /**
     * Y^ | 15 14 13 12 |3 7 11 15 | 11 10 9 8 |2 6 10 14 | 7 6 5 4 |1 5 9 13 |
     * 3 2 1 0 |0 4 8 12 ------------------------------>X 12 8 4 0 |0 1 2 3 | 13
     * 9 5 1 |4 5 6 7 | 14 10 6 2 |8 9 10 11 | 15 11 7 3 |12 13 14 15
     * 
     * 二维数组初始化遍历时，是在第一象限。90度时X轴偏移，Y轴不偏移。270度时相反。180度时X,Y都偏移。 逆时针旋转90，180，270度。
     * 
     * 用到三角函数的“两角和差公式” sin(δ+β)=sin(δ)cos(β)+cos(δ)sin(β) 即：y' = y * cos(β) + x
     * * sin(β) cos(δ+β)=cos(δ)cos(β)-sin(δ)sin(β) 即：x' = x * cos(β) - y *
     * sin(β)
     * 
     * @see http://www.cnblogs.com/ywxgod/archive/2010/08/06/1793609.html
     * @param arr
     *            待旋转数组
     * @param angle
     *            旋转角度：90，180，270
     * @return
     */
    public static Cell[][] rotation(Cell[][] arr, int angle) {
        if (angle <= 0 || angle >= 360) {
            throw new IllegalArgumentException("参数不合法：angle： 0--360（不含）");
        }
        int offsetX = 3, offsetY = 3;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }
        double rad = Math.toRadians(angle);
        int sin = (int) Math.sin(rad);
        int cos = (int) Math.cos(rad);
        Cell[][] _arr = new Cell[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int _x = x * cos - y * sin + offsetX;
                int _y = y * cos + x * sin + offsetY;
                _arr[_x][_y] = arr[x][y];
            }
        }
        return _arr;
    }

    public static void main(String[] args) {
        JFrame game = new JFrame();
        game.setTitle("2048 Game");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(340, 400);
        game.setResizable(false);

        game.add(new Game());

        game.setLocationRelativeTo(null);
        game.setVisible(true);

    }
}
