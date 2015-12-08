/*
 * 
 * 
 * 
 */
package Email;

import com.sun.mail.smtp.SMTPMessage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import sun.misc.BASE64Encoder;

/**
 *
 * @author HERU
 */
public class MailClient {

    private String username;
    private String password;
    private String host;
    private String port;
    private Properties props = new Properties();

    public MailClient(final String u, final String p, final String h) {
        this(u, p, h, "587");
    }

    public MailClient(final String u, final String p, final String h, String po) {
        this.username = u;
        this.password = p;
        this.host = h;
        this.port = po;
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.quitwait ", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
    }

    public boolean Send(String to, String sub, String msg) {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(sub);
            Multipart multipart = new MimeMultipart();

            //Message Part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport.send(message);

            return true;
        } catch (Exception er) {
            return false;
        }
    }

    public boolean Send(String to, String sub, String msg, File attch) {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(sub);
            Multipart multipart = new MimeMultipart();

            //Message Part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);

            //Attachment Part
            messageBodyPart = new MimeBodyPart();
            String filesource = attch.getAbsolutePath();
            String filename = attch.getName();
            DataSource source = new FileDataSource(filesource);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            //Set MessageContent
            message.setContent(multipart);

            //Send
            Transport.send(message);

            return true;
        } catch (Exception er) {
            return false;
        }
    }

    public boolean Send(String to, String sub, String msg, File[] attch) {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(sub);
            Multipart multipart = new MimeMultipart();

            //Message Part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);

            //Attachment Parts
            for (File e : attch) {
                messageBodyPart = new MimeBodyPart();
                String filesource = e.getAbsolutePath();
                String filename = e.getName();
                DataSource source = new FileDataSource(filesource);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
            }

            //Set MessageContent
            message.setContent(multipart);

            //Send
            Transport.send(message);

            return true;
        } catch (Exception er) {
            return false;
        }
    }

    public boolean SendImageAttachment(String to, String sub, String msg, File imgatc) {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            SMTPMessage message = new SMTPMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(sub);
            Multipart multipart = new MimeMultipart("related");

            //Message Part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);

            //Image Part
            ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
            BufferedImage img = ImageIO.read(imgatc);
            ImageIO.write(img, "jpg", baos);
            baos.flush();

            BASE64Encoder enc = new BASE64Encoder();

            String base64String = enc.encode(baos.toByteArray());
            baos.close();

            MimeBodyPart imagePart = null;
            InternetHeaders headers = new InternetHeaders();
            headers.addHeader("Content-Type", "image/jpeg");
            headers.addHeader("Content-Transfer-Encoding", "base64");
            imagePart = new MimeBodyPart(headers, base64String.getBytes());
            imagePart.setDisposition(MimeBodyPart.INLINE);
            imagePart.setContentID("&lt;" + imgatc.getName() + "&gt;");
            imagePart.setFileName(imgatc.getName());
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);

            return true;
        } catch (Exception er) {
            throw new RuntimeException(er);
        }
    }

    public boolean SendTest(String to, String sub, String msg, File imgatc) {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            SMTPMessage message = new SMTPMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(sub);
            Multipart multipart = new MimeMultipart("alternative");

            //Message Part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);

            //Image Part
            
            
            
            
            
            InternetHeaders headers = new InternetHeaders();
            headers.addHeader("Content-Type", "text/html;charset=ISO-8859-1");
            headers.addHeader("Content-Transfer-Encoding", "quoted-printable");
            String html1 = "<html><body><p>Here is my image:</p><img width=3D\"75\" height=3D\"75\" style=3D\"bo=\n"
                    + "rder:solid 1px #cccccc;\" src=3D\"https://lh3.googleusercontent.com/-PMa2W02_=\n"
                    + "W5k/AAAAAAAAAAI/AAAAAAAAANg/gwXa8eS_fL4/s75-c-k-a-no/photo.jpg\" /></body></html>";
            String html2 = "<html><body><p>Here is my image:</p>"+"<img src=\"cid:myimage\" width=\"30%\" height=\"30%\" /><br>"+"</body></html>";
            String html = "<html dir=3D\"ltr\"><body><!-- X-Notifications: 1:6c01887eb0800000 --><div st=\n"
                    + "yle=3D\"border:solid 1px #dfdfdf;color:#686868;font:13px Arial\"><div style=\n"
                    + "=3D\"background-color:#fff;padding:20px;\"><table cellpadding=3D0 cellspacing=\n"
                    + "=3D0><tr><td style=3D\"padding-right:15px;vertical-align:top\"><a href=3D\"htt=\n"
                    + "ps://plus.google.com/_/notifications/emlink?emr=3D07033345157898750420&emid=\n"
                    + "=3DCJDsv9C5y8kCFSIMTAoddXcGeA&path=3D%2F106236986236094885136&dt=3D14495497=\n"
                    + "52131&ub=3DCIRCLE_PERSONAL_ADD\"><img width=3D\"75\" height=3D\"75\" style=3D\"bo=\n"
                    + "rder:solid 1px #cccccc;\" src=3D\"https://lh3.googleusercontent.com/-PMa2W02_=\n"
                    + "W5k/AAAAAAAAAAI/AAAAAAAAANg/gwXa8eS_fL4/s75-c-k-a-no/photo.jpg\" /></a></td>=\n"
                    + "<td style=3D\"width:578px;color:#333;font:13px Arial;vertical-align:top;colo=\n"
                    + "r:#686868;font:16px Arial\">Sydney is already in your circles. <a href=3D\"ht=\n"
                    + "tp://www.google.com/support/+/bin/answer.py?answer=3D1047805\" style=3D\"colo=\n"
                    + "r:#3366CC;text-decoration:none\">Learn more</a>.<div style=3D\"margin-top:10p=\n"
                    + "x\"><a href=3D\"https://plus.google.com/_/notifications/emlink?emr=3D07033345=\n"
                    + "157898750420&emid=3DCJDsv9C5y8kCFSIMTAoddXcGeA&path=3D%2F106236986236094885=\n"
                    + "136&dt=3D1449549752131&ub=3DCIRCLE_PERSONAL_ADD\" style=3D\"background-color:=\n"
                    + "#d44b38;border:solid 1px #dfdfdf;border-radius:3px;color:#fff;display:inlin=\n"
                    + "e-block;font-family: Arial;font-size:13px;height:30px;line-height:30px;min-=\n"
                    + "width:54px;padding:1px 20px;text-align:center;text-decoration:none;white-sp=\n"
                    + "ace:nowrap;\"rel=3D\"acb106236986236094885136\">View profile</a></div></td></t=\n"
                    + "r></table></div><div style=3D\"border-top:solid 1px #dfdfdf;padding:0 20px;b=\n"
                    + "ackground-color:#f5f5f5\"><table cellpadding=3D0 cellspacing=3D0 style=3D\"he=\n"
                    + "ight:50px\"><tbody><tr><td style=3D\"vertical-align:middle;width:100%; color:=\n"
                    + "#636363;font:11px Arial; line-height:120%\"><a href=3D\"https://plus.google.c=\n"
                    + "om/_/notifications/emlink?emr=3D07033345157898750420&emid=3DCJDsv9C5y8kCFSI=\n"
                    + "MTAoddXcGeA&path=3D%2Fsettings%2Funsubscribe%3Fueat%3DAJ7SsMm9VkLuiUeDwlPsL=\n"
                    + "todHk9lTTxgnIXNeYSd0gO9BzdAK8AXA7j6MKenBPmv8GLGU5NAGNEJQpCRCgQeVlkk7sW1XtDy=\n"
                    + "9AWRi3g-0lZaunntHkGEgnoEGczr5Ynn92hOAMQfdaBA25obJOh5WgYL2IaMysH4uGCwzoFElWi=\n"
                    + "StZl8_38JVPhMuzEH0W13MTcQPc39BcvF&dt=3D1449549752131&ub=3DCIRCLE_PERSONAL_A=\n"
                    + "DD\" style=3D\"color:#3366CC;text-decoration:none\">Unsubscribe</a> from these=\n"
                    + " emails.<br>Google Inc., 1600 Amphitheatre Pkwy, Mountain View, CA 94043 US=\n"
                    + "A<br></td><td style=3D\"padding:0px;\"><img src=3D\"https://ssl.gstatic.com/im=\n"
                    + "ages/branding/google_plus/1x/gplus_word_89x27dp.png\" /></td></tr></tbody></=\n"
                    + "table></div></div></body></html>";

            BASE64Encoder enc = new BASE64Encoder();

            MimeBodyPart htmlPart = new MimeBodyPart(headers, html2.getBytes());

            //htmlPart.setText(""
            //        + "<html>"
            //        + " <body>"
            //        + "  <p>Here is my image:</p>"
            //        + "  <img src=\"cid:" + cid + "\" />"
            //        + " </body>"
            //        + "</html>"
            //       , "utf-8", "html");//"US-ASCII"
            
            multipart.addBodyPart(htmlPart);

            String cid = "<myimage>";
            MimeBodyPart imagePart = new MimeBodyPart();
            
            //imagePart.setContentID(cid);
            imagePart.setHeader("Content-ID", "<myimage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            imagePart.attachFile(imgatc.getAbsolutePath());
            multipart.addBodyPart(imagePart);


            message.setContent(multipart);
            Transport.send(message);

            return true;
        } catch (Exception er) {
            throw new RuntimeException(er);
        }
    }

}
