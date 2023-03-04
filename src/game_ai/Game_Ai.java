
package game_ai;

import application.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class Game_Ai extends JPanel{
  private static final Color BG_COLOR = new Color(0xd0e7e2);

  private static final String FONT_NAME = "BuloRounded-Black";
  private static final int TILE_SIZE = 64;
  private static final int TILES_MARGIN = 16;
  
  public Tile[] myTiles;
  boolean myWin = false;
  boolean myLose = false;
  int myScore = 0;
  	
  public Game_Ai() {
	 
    setPreferredSize(new Dimension(340, 400));
    setFocusable(true);
    /*addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          resetGame();
        }
        if (!canMove()) {
          myLose = true;
        }
        
        if (!myWin && !myLose) {
          switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            	
              left();  
              break;
            case KeyEvent.VK_RIGHT:	
              right();
              break;
            case KeyEvent.VK_DOWN:
              down();
              break;
            case KeyEvent.VK_UP:
              up();
              break;
          }
        }

    
        if (!myWin && !canMove()) {
          myLose = true;
        }
        repaint();
      }
    });
  
*/
    	
    resetGame();
  }
  
 
  private void stampaTile() {
      for(int i = 0; i < 4; i++) {
	    	for(int j = 0; j < 4; j++) {
	    		System.out.print(tileAt(i, j).value);
	    	}
	    
      }
      System.out.println();
     }
    
  
  public void resetGame() {
    myScore = 0;
    myWin = false;
    myLose = false;
    myTiles = new Tile[4 * 4];
    for (int i = 0; i < myTiles.length; i++) {
      myTiles[i] = new Tile();
    }
    addTile();
    addTile();
  }
  
  public void left() {
    boolean needAddTile = false;
    for (int i = 0; i < 4; i++) {
      Tile[] line = getLine(i);
      Tile[] merged = mergeLine(moveLine(line));
      setLine(i, merged);
      if (!needAddTile && !compare(line, merged)) {
        needAddTile = true;
      }
    }

    if (needAddTile) {
      addTile();
    }

  }

  public void right() {
    myTiles = rotate(180);
    left();
    myTiles = rotate(180);
  }

  public void up() {
    myTiles = rotate(270);
    left();
    myTiles = rotate(90);
  }

  public void down() {
    myTiles = rotate(90);
    left();
    myTiles = rotate(270);
  }
  
  public Tile tileAt(int x, int y) {
    return myTiles[x + y * 4];
  }

  private void addTile() {
    List<Tile> list = availableSpace();
    if (!availableSpace().isEmpty()) {
      int index = (int) (Math.random() * list.size()) % list.size();
      Tile emptyTile = list.get(index);
      double randomNum = Math.random();
      if (randomNum < 0.5) {
        emptyTile.value = 1;
      } else if (randomNum < 0.9) {
        emptyTile.value = 2;
      } else {
        emptyTile.value = 3;
      }
    }
  }
  
  private List<Tile> availableSpace() {
    final List<Tile> list = new ArrayList<Tile>(16);
    for (Tile t : myTiles) {
      if (t.isEmpty()) {
        list.add(t);
      }
    }
    return list;
  }
  
  private boolean isFull() {
    return availableSpace().size() == 0;
  }

  boolean canMove() {
    if (!isFull()) {
      return true;
    }
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        Tile t = tileAt(x, y);
        if ((x < 3 && t.value == tileAt(x + 1, y).value && t.value != 1 && t.value != 2)
                || ((y < 3) && t.value == tileAt(x, y + 1).value && t.value != 1 && t.value != 2)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean compare(Tile[] line1, Tile[] line2) {
    if (line1 == line2) {
      return true;
    } else if (line1.length != line2.length) {
      return false;
    }
    		
    for (int i = 0; i < line1.length; i++) {
      if (line1[i].value != line2[i].value) {
        return false;
      }
    }
    return true;
  }

  private Tile[] rotate(int angle) {
    Tile[] newTiles = new Tile[4 * 4];
    int offsetX = 3, offsetY = 3;
    if (angle == 90) {
      offsetY = 0;
    } else if (angle == 270) {
      offsetX = 0;
    }

    double rad = Math.toRadians(angle);
    int cos = (int) Math.cos(rad);
    int sin = (int) Math.sin(rad);
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        int newX = (x * cos) - (y * sin) + offsetX;
        int newY = (x * sin) + (y * cos) + offsetY;
        newTiles[(newX) + (newY) * 4] = tileAt(x, y);
      }
    }
    return newTiles;
  }

  private Tile[] moveLine(Tile[] oldLine) {
    LinkedList<Tile> l = new LinkedList<Tile>();
    for (int i = 0; i < 4; i++) {
      if (!oldLine[i].isEmpty())
        l.addLast(oldLine[i]);
    }
    if (l.size() == 0) {
      return oldLine;
    } else {
      Tile[] newLine = new Tile[4];
      ensureSize(l, 4);
      for (int i = 0; i < 4; i++) {
        newLine[i] = l.removeFirst();
      }
      return newLine;
    }
  }

  private Tile[] mergeLine(Tile[] oldLine) {
    LinkedList<Tile> list = new LinkedList<Tile>();
    for (int i = 0; i < 4; i++) {
      int value = oldLine[i].value;
      if (value != 0) {
        if (list.isEmpty()) {
          list.add(new Tile(value));
        } else {
          Tile prev = list.getLast();
          if (prev.value + value == 3 || ((prev.value + value) % 3 == 0 && prev.value == value)) { // Only merge tiles that add up to a multiple of 3
            list.removeLast();
            int newValue = prev.value + value;
            list.add(new Tile(newValue));
            int score = newValue;
            myScore += score;
          } else {
            list.add(new Tile(value));
          }
        }
      }
    }
    if (list.size() == 0) {
      return oldLine;
    } else {
      ensureSize(list, 4);
      return list.toArray(new Tile[4]);
    }
  }

  private static void ensureSize(java.util.List<Tile> l, int s) {
    while (l.size() != s) {
      l.add(new Tile());
    }
  }

  private Tile[] getLine(int index) {
    Tile[] result = new Tile[4];
    for (int i = 0; i < 4; i++) {
      result[i] = tileAt(i, index);
    }
    return result;
  }

  private void setLine(int index, Tile[] re) {
    System.arraycopy(re, 0, myTiles, index * 4, 4);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(BG_COLOR);
    g.fillRect(0, 0, this.getSize().width, this.getSize().height);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        drawTile(g, myTiles[x + y * 4], x, y);
      }
    }
  }

  
  private void drawTile(Graphics g2, Tile tile, int x, int y) {
    Graphics2D g = ((Graphics2D) g2);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
    int value = tile.value;
    int xOffset = offsetCoors(x);
    int yOffset = offsetCoors(y);
    g.setColor(tile.getBackground());
    g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
    g.setColor(tile.getForeground());
    final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
    final Font font = new Font(FONT_NAME, Font.BOLD, size);
    g.setFont(font);

    String s = String.valueOf(value);
    final FontMetrics fm = getFontMetrics(font);

    final int w = fm.stringWidth(s);
    final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

    if (value != 0)
      g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

    if (myWin || myLose) {
      g.setColor(new Color(255, 255, 255, 30));
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(new Color(0x000000));
      g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
      if (myWin) {
        g.drawString("You won!", 68, 150);
      }
      if (myLose) {
        g.drawString("Game over!", 50, 130);
        g.drawString("You lose!", 64, 200);
      }
      if (myWin || myLose) {
        g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
        g.setColor(new Color(128, 128, 128, 128));
        g.drawString("Press ESC to play again", 80, getHeight() - 40);
      }
    }
    g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
    g.drawString("Score: " + myScore, 200, 365);

  }

  private static int offsetCoors(int arg) {
    return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
  }
  
  static class Tile {
    int value;
    
    public Tile() {
      this(0);
    }
    
    public Tile(int num) {
      value = num;
    }

    public boolean isEmpty() {
      return value == 0;
    }

    public Color getForeground() {
      return value < 3 ? new Color(0xffffff) :  new Color(0x000000);
    }

    public Color getBackground() {
      switch (value) {
        case 1:    return new Color(0x73caf3);
        case 2:    return new Color(0xf26880);
        case 3:
        case 6:
        case 12:
        case 24:
        case 48:
        case 96:
        case 192:
        case 384:
          return new Color(0xffffff);
      }
      return new Color(0xbcd9d8);
    }
  }
  
}
