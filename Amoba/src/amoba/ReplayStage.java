package amoba;

import com.google.gson.internal.StringMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ReplayStage extends BaseStage{
    private JList steps;
    private DefaultListModel dlm;
    private ArrayList<StringMap> list;
    private JScrollPane scrollForSteps;
    
    private int i = 0;
    
    public ReplayStage(Frame parent, boolean modal, String name, int width, int height) {
        super(parent, modal);
        
        EAST_PANEL_WIDTH = 180;
        
        initStage(width, height, name + " :: lejátszott meccs");
        
        statePanel.setLayout(new BorderLayout());
        
        dlm = new DefaultListModel();
        steps = new JList(dlm);
        steps.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateStageState();
            }
        });
        
        scrollForSteps = new JScrollPane();
        scrollForSteps.setViewportView(steps);
        
        statePanel.add(scrollForSteps);
        
        Dimension ss = getToolkit().getScreenSize();
        setLocation(ss.width / 2 - this.getWidth() / 2,ss.height / 2 - this.getHeight() / 2);
    }
    
    public void setSteps(ArrayList<StringMap> list){
        this.list = list;
        int c = 0;
        
        for(StringMap map : this.list){
            if(map.containsKey("signal") && map.containsKey("x") && map.containsKey("y")){
                c++;
                dlm.addElement(c + ". lépés: Jel=" + map.get("signal").toString() + 
                        ", X=" + map.get("x").toString() + ", Y=" + map.get("y").toString());
            }
        }
    }

    private void updateStageState(){
        if(steps.getSelectedIndex() == i) return;
        
        for(StringMap map : list){
            if(map.containsKey("x") && map.containsKey("y")){
                int x = Integer.parseInt(map.get("x").toString());
                int y = Integer.parseInt(map.get("y").toString());
                squares[x][y].setText("");
                squares[x][y].setForeground(Color.BLACK);
            }
        }
        
        i = steps.getSelectedIndex();
        int c = 0;
        
        for(StringMap map : list){
            if(map.containsKey("signal") && map.containsKey("x") && map.containsKey("y")){
                String signal = map.get("signal").toString();
                int x = Integer.parseInt(map.get("x").toString());
                int y = Integer.parseInt(map.get("y").toString());
                squares[x][y].setText(signal);
                
                if(c == i){
                    squares[x][y].setForeground(Color.RED);
                    break;
                }
                
                c++;
            }
        }
    }
}
