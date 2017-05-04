import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by alima on 4/5/17.
 */
public class FormControls {
    public static final ButtonGroup addStockStatusGroup(Container c){
        ButtonGroup group = new ButtonGroup();

        final JRadioButton rbtnActive = new JRadioButton("Active", true);
        final JRadioButton rbtnInactive = new JRadioButton("Inactive");
        rbtnActive.setBounds(100, 157, 80, 20);
        rbtnActive.setActionCommand("a");

        rbtnActive.setForeground(Color.WHITE);
        rbtnActive.setBackground(Color.DARK_GRAY);

        rbtnInactive.setActionCommand("i");
        rbtnInactive.setBounds(180, 157, 80, 20);

        rbtnInactive.setForeground(Color.WHITE);
        rbtnInactive.setBackground(Color.DARK_GRAY);

        group.add(rbtnActive);
        group.add(rbtnInactive);

        c.add(rbtnActive);
        c.add(rbtnInactive);

        return group;
    }

    public static void setButtonGroup(String rdValue, Enumeration elements ){
        while (elements.hasMoreElements()){
            AbstractButton button = (AbstractButton)elements.nextElement();

            if(rdValue.equals(button.getActionCommand())){
                button.setSelected(true);
            }
        }
    }
}
