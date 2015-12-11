/*
 * 
 * 
 * 
 */
package Email;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author HERU
 */
public class VMailNoti {

    private static MailUI mailer;
    private static Options opts;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Options.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        startNotificationApp();
        if (args.length == 0) {//Print usage

        } else {
            List<String> params = Arrays.asList(args);
            if (params.contains("-ui")) {

            } else if (params.contains("-send")) {

            }
        }
    }

    public static void startNotificationApp() {
        try {
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
            } else {
                final PopupMenu popup = new PopupMenu();
                Image image = ImageIO.read(new File("D:\\Nakpil Softwares\\Nakpil Softwares\\VMail\\VMail.png"));
                final TrayIcon trayIcon = new TrayIcon(image, "VMail");
                final SystemTray tray = SystemTray.getSystemTray();

                // Create a pop-up menu components
                MenuItem sendui = new MenuItem("Send an eMail");
                //CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
                //CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
                //Menu displayMenu = new Menu("Options");
                MenuItem optionm = new MenuItem("Options");
                MenuItem aboutm = new MenuItem("About");
                //MenuItem infoItem = new MenuItem("Info");
                //MenuItem noneItem = new MenuItem("None");
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        System.exit(0);
                    }
                });

                optionm.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if (opts != null) {
                            if (opts.isVisible()) {
                                opts.dispose();
                                opts = null;
                            } else {
                                opts = new Options();
                                opts.setVisible(true);
                            }
                        } else {
                            opts = new Options();
                            opts.setVisible(true);
                        }
                    }
                });

                //Add components to pop-up menu
                popup.add(sendui);
                //popup.addSeparator();
                //popup.add(cb1);
                //popup.add(cb2);
                //popup.add(displayMenu);
                //displayMenu.add(optionm);
                //displayMenu.add(aboutm);
                //displayMenu.add(infoItem);
                //displayMenu.add(noneItem);
                popup.add(optionm);
                popup.add(aboutm);
                popup.addSeparator();
                popup.add(exitItem);

                trayIcon.setPopupMenu(popup);
                trayIcon.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (evt.getButton() == MouseEvent.BUTTON1) {
                            if (mailer != null) {
                                if (mailer.isVisible()) {
                                    mailer.dispose();
                                    mailer = null;
                                } else {
                                    mailer = new MailUI();
                                    mailer.setVisible(true);
                                }
                            } else {
                                mailer = new MailUI();
                                mailer.setVisible(true);
                            }
                        }
                    }
                });
                tray.add(trayIcon);
            }
        } catch (Exception er) {

        }
    }

}
