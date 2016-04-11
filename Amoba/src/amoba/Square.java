package amoba;

import java.awt.Font;
import java.awt.Point;
import javax.swing.JToggleButton;

public class Square extends JToggleButton{
    private Point coord;
    
    public Square(int coordX, int coordY){
        super();
        
        this.setFont(new Font("Arial",Font.BOLD,10));
        this.coord = new Point(coordX, coordY);
    }

    public Point getCoord() {
        return coord;
    }
}
