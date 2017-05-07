import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alima on 7/5/17.
 */
public abstract class CustomInternalFrame extends JInternalFrame implements InternalFrameListener {
    private final Map<String, Object>  bundle = new HashMap<String, Object>();

    public CustomInternalFrame(){
        addInternalFrameListener(this);
    }

    public void putExtra(String key, Object value){
        bundle.put(key, value);
    }

    public Object getExtra(String key){
        return bundle.getOrDefault(key, null);
    }

    protected void showMessage(String m){
        Component parent = getDesktopPane() != null ? getDesktopPane() : getParent();
        JOptionPane.showMessageDialog(parent, m);
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }
}
