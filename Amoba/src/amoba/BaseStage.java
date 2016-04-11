package amoba;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public abstract class BaseStage extends JDialog{
    public final int MIN_COUNT_X = 10;
    public final int MIN_COUNT_Y = 10;
    
    protected static final String O = "O";
    protected static final String X = "X";
    
    protected int EAST_PANEL_WIDTH = 110;
    protected int SQUARE_WIDTH_HEIGHT = 20;
    protected int MARGIN = 10;
    protected int LEFT_TEXT_WIDTH = 80;
    protected int RIGHT_TEXT_WIDTH = 20;
    protected int TEXT_HEIGHT = 10;
    
    protected boolean initializedStage = false;
    
    protected Square[][] squares;
    protected Point lastPoint = new Point();
    
    protected Menu menu;
    protected JPanel statePanel, mainPanel, gamePanel;
    protected Font defaultFont;
    
    protected JScrollPane scrollPanel;
    
    public BaseStage(java.awt.Frame parent, boolean modal){
        super(parent,modal);
        menu = (Menu)parent;
        
        defaultFont = new Font("arial", Font.BOLD, 12);
        
        menu.enableComponents(false);
    }

    public Menu getMenu(){
        return menu;
    }
    
    protected void initStage(int countX, int countY, String name){
        if(initializedStage) return;
        
        if(countX < MIN_COUNT_X){
            throw new IllegalArgumentException("Minimum " + MIN_COUNT_X + 
                    " négyzet szélesnek kell lennie a területnek!");
        }
        
        if(countY < MIN_COUNT_Y){
            throw new IllegalArgumentException("Minimum " + MIN_COUNT_Y + 
                    " négyzet magasnak kell lennie a területnek!");
        }
        
        initSquares(countX, countY, name);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        
        initializedStage = true;
    }
    
    protected void initSquares(int countX, int countY, String name){
        int sizeWidth = countX * SQUARE_WIDTH_HEIGHT + getInsets().right + getInsets().left;
        int sizeHeight = countY * SQUARE_WIDTH_HEIGHT + getInsets().top + getInsets().bottom;
        
        setTitle("Amőba :: " + name);
        
        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setPreferredSize(new Dimension(sizeWidth, sizeHeight));
        
        squares = new Square[countX][countY];
        
        for(int x = 0; x < squares.length; x++){
            for(int y = 0; y < squares[x].length; y++){
                squares[x][y] = new Square(x,y);
                squares[x][y].setSize(SQUARE_WIDTH_HEIGHT, SQUARE_WIDTH_HEIGHT);
                squares[x][y].setLocation(x * SQUARE_WIDTH_HEIGHT, y * SQUARE_WIDTH_HEIGHT);
                squares[x][y].setMargin(new Insets(0,0,0,0));
                
                gamePanel.add(squares[x][y]);
            }
        }
        
        scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(gamePanel);
        
        
        statePanel = new JPanel();
        statePanel.setLayout(null);
        statePanel.setPreferredSize(new Dimension(EAST_PANEL_WIDTH, sizeHeight));
        
        add(scrollPanel,BorderLayout.CENTER);
        add(statePanel,BorderLayout.EAST);
        
        int scrollBarWidth = ((Integer)UIManager.get("ScrollBar.width")).intValue() + 2;
        this.setSize(sizeWidth + scrollBarWidth + EAST_PANEL_WIDTH, 
                sizeHeight + scrollBarWidth + SQUARE_WIDTH_HEIGHT+2);
    }

    @Override
    public void setSize(int width, int height) {
        Dimension resolution = this.getToolkit().getScreenSize();
        resolution.height = resolution.height - 40;
        
        if(width > resolution.width){
            width = resolution.width;
        }
        
        if(height > (resolution.height - this.getInsets().top - this.getInsets().bottom)){
            height = (resolution.height - this.getInsets().top - this.getInsets().bottom);
        }
        
        super.setSize(width, height);
    }

    @Override
    public void dispose() {
        menu.enableComponents(true);
        super.dispose();
    }
    
    
}
