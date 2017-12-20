package com.frinika.gui.util;

import com.bulenkov.darcula.DarculaLaf;
import com.bulenkov.darcula.DarculaLookAndFeelInfo;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBlue;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalTheme;

/**
 * Utility static methods usable for windows and dialogs.
 *
 * @author hajdam
 */
public class WindowUtils {

    private static boolean darkMode = false;

    private static final int BUTTON_CLICK_TIME = 150;

    private WindowUtils() {
    }

    public static void addHeaderPanel(JDialog dialog, String headerTitle, String headerDescription, ImageIcon headerIcon) {
        WindowHeaderPanel headerPanel = new WindowHeaderPanel();
        headerPanel.setTitle(headerTitle);
        headerPanel.setDescription(headerDescription);
        if (headerIcon != null) {
            headerPanel.setIcon(headerIcon);
        }
        if (dialog instanceof WindowHeaderPanel.WindowHeaderDecorationProvider) {
            ((WindowHeaderPanel.WindowHeaderDecorationProvider) dialog).setHeaderDecoration(headerPanel);
        } else {
            Frame frame = getFrame(dialog);
            if (frame instanceof WindowHeaderPanel.WindowHeaderDecorationProvider) {
                ((WindowHeaderPanel.WindowHeaderDecorationProvider) frame).setHeaderDecoration(headerPanel);
            }
        }
        int height = dialog.getHeight() + headerPanel.getPreferredSize().height;
        dialog.getContentPane().add(headerPanel, java.awt.BorderLayout.PAGE_START);
        dialog.setSize(dialog.getWidth(), height);
    }

    public static void switchLookAndFeel(SupportedLaf selectedLaf) {
        switch (selectedLaf) {
            case DEFAULT: {
                try {
                    UIManager.setLookAndFeel(PlasticXPLookAndFeel.class.getCanonicalName());
                    MetalTheme currentTheme = PlasticXPLookAndFeel.getCurrentTheme();
                    if (!(currentTheme instanceof SkyBlue)) {
                        PlasticXPLookAndFeel.setCurrentTheme(PlasticXPLookAndFeel.getPlasticTheme());
                        UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    try {
                        PlasticXPLookAndFeel plasticXPLookAndFeel = new PlasticXPLookAndFeel();
                        UIManager.setLookAndFeel(plasticXPLookAndFeel);
                    } catch (UnsupportedLookAndFeelException ex2) {
                        Logger.getLogger(WindowUtils.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
                WindowUtils.setDarkMode(false);
                break;
            }
            case DARCULA: {
                try {
                    // Workaround for https://github.com/bulenkov/iconloader/issues/14
                    javax.swing.UIManager.getFont("Label.font");

                    UIManager.setLookAndFeel(DarculaLaf.class.getCanonicalName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    try {
                        UIManager.installLookAndFeel(new DarculaLookAndFeelInfo());
                        UIManager.setLookAndFeel(new DarculaLaf());
                    } catch (UnsupportedLookAndFeelException ex2) {
                        Logger.getLogger(WindowUtils.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
                WindowUtils.setDarkMode(true);
                break;
            }
        }
    }

    public static void invokeWindow(final Window window) {
        java.awt.EventQueue.invokeLater(() -> {
            if (window instanceof JDialog) {
                ((JDialog) window).setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            }

            window.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            window.setVisible(true);
        });
    }

    public static JDialog createDialog(final Component component, Window parent, Dialog.ModalityType modalityType) {
        JDialog dialog = new JDialog(parent, modalityType);
        Dimension size = component.getPreferredSize();
        dialog.add(component);
        dialog.setSize(size.width + 8, size.height + 24);
        return dialog;
    }

    public static JDialog createDialog(final Component component) {
        JDialog dialog = new JDialog();
        Dimension size = component.getPreferredSize();
        dialog.add(component);
        dialog.setSize(size.width + 8, size.height + 24);
        return dialog;
    }

    public static void invokeDialog(final Component component) {
        JDialog dialog = createDialog(component);
        invokeWindow(dialog);
    }

    public static void initWindow(Window window) {
//        if (window.getParent() instanceof XBEditorFrame) {
//            window.setIconImage(((XBEditorFrame) window.getParent()).getMainFrameManagement().getFrameIcon());
//        }
    }

    public static void closeWindow(Window window) {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    public static JDialog createBasicDialog() {
        JDialog dialog = new JDialog(new javax.swing.JFrame(), true);
        dialog.setSize(640, 480);
        dialog.setLocationByPlatform(true);
        return dialog;
    }

    /**
     * Find frame component for given component.
     *
     * @param component instantiated component
     * @return frame instance if found
     */
    public static Frame getFrame(Component component) {
        Component parentComponent = SwingUtilities.getWindowAncestor(component);
        while (!(parentComponent == null || parentComponent instanceof Frame)) {
            parentComponent = parentComponent.getParent();
        }
        return (Frame) parentComponent;
    }

    /**
     * Assign ESCAPE/ENTER key for all focusable components recursively.
     *
     * @param component target component
     * @param closeButton button which will be used for closing operation
     */
    public static void assignGlobalKeyListener(Container component, final JButton closeButton) {
        assignGlobalKeyListener(component, closeButton, closeButton);
    }

    /**
     * Assign ESCAPE/ENTER key for all focusable components recursively.
     *
     * @param component target component
     * @param okButton button which will be used for default ENTER
     * @param cancelButton button which will be used for closing operation
     */
    public static void assignGlobalKeyListener(Container component, final JButton okButton, final JButton cancelButton) {
        assignGlobalKeyListener(component, new OkCancelListener() {
            @Override
            public void okEvent() {
                doButtonClick(okButton);
            }

            @Override
            public void cancelEvent() {
                doButtonClick(cancelButton);
            }
        });
    }

    /**
     * Assign ESCAPE/ENTER key for all focusable components recursively.
     *
     * @param component target component
     * @param listener ok and cancel event listener
     */
    public static void assignGlobalKeyListener(Container component, final OkCancelListener listener) {
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    boolean performOkAction = true;

                    if (evt.getSource() instanceof JButton) {
                        ((JButton) evt.getSource()).doClick(BUTTON_CLICK_TIME);
                        performOkAction = false;
                    } else if (evt.getSource() instanceof JTextArea) {
                        performOkAction = !((JTextArea) evt.getSource()).isEditable();
                    } else if (evt.getSource() instanceof JTextPane) {
                        performOkAction = !((JTextPane) evt.getSource()).isEditable();
                    } else if (evt.getSource() instanceof JEditorPane) {
                        performOkAction = !((JEditorPane) evt.getSource()).isEditable();
                    }

                    if (performOkAction && listener != null) {
                        listener.okEvent();
                    }

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE && listener != null) {
                    listener.cancelEvent();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        assignGlobalKeyListener(component, keyListener);
    }

    /**
     * Assign key listener for all focusable components recursively.
     *
     * @param component target component
     * @param keyListener key lisneter
     */
    public static void assignGlobalKeyListener(Container component, KeyListener keyListener) {
        Component[] comps = component.getComponents();
        for (Component item : comps) {
            if (item.isFocusable()) {
                item.addKeyListener(keyListener);
            }

            if (item instanceof Container) {
                assignGlobalKeyListener((Container) item, keyListener);
            }
        }
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        WindowUtils.darkMode = darkMode;
    }

    /**
     * Performs visually visible click on the button component.
     *
     * @param button button component
     */
    public static void doButtonClick(JButton button) {
        button.doClick(BUTTON_CLICK_TIME);
    }

    /**
     * Creates panel for given main and control panel.
     *
     * @param mainPanel main panel
     * @param controlPanel control panel
     * @return panel
     */
    public static JPanel createDialogPanel(JPanel mainPanel, JPanel controlPanel) {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(mainPanel, BorderLayout.CENTER);
        dialogPanel.add(controlPanel, BorderLayout.SOUTH);
        return dialogPanel;
    }

    public static void setWindowCenterPosition(Window window) {
        setWindowCenterPosition(window, 0);
    }

    public static void setWindowCenterPosition(Window window, int screen) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] allDevices = env.getScreenDevices();
        int topLeftX, topLeftY, screenX, screenY, windowPosX, windowPosY;

        if (screen < allDevices.length && screen > -1) {
            topLeftX = allDevices[screen].getDefaultConfiguration().getBounds().x;
            topLeftY = allDevices[screen].getDefaultConfiguration().getBounds().y;

            screenX = allDevices[screen].getDefaultConfiguration().getBounds().width;
            screenY = allDevices[screen].getDefaultConfiguration().getBounds().height;
        } else {
            topLeftX = allDevices[0].getDefaultConfiguration().getBounds().x;
            topLeftY = allDevices[0].getDefaultConfiguration().getBounds().y;

            screenX = allDevices[0].getDefaultConfiguration().getBounds().width;
            screenY = allDevices[0].getDefaultConfiguration().getBounds().height;
        }

        windowPosX = ((screenX - window.getWidth()) / 2) + topLeftX;
        windowPosY = ((screenY - window.getHeight()) / 2) + topLeftY;

        window.setLocation(windowPosX, windowPosY);
    }

    public static interface OkCancelListener {

        void okEvent();

        void cancelEvent();
    }
}
