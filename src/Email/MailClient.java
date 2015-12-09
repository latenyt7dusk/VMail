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

    public boolean SendInlinedImage(String to, String sub, String msg, File imgatc) {
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
            
            MimeBodyPart imagePart =null;
            InternetHeaders headers = new InternetHeaders();
            headers.addHeader("Content-Type", "image/jpeg");
            headers.addHeader("Content-Transfer-Encoding", "base64");
            imagePart = new MimeBodyPart(headers, base64String.getBytes());
            imagePart.setDisposition(MimeBodyPart.INLINE);
            imagePart.setContentID("&lt;"+imgatc.getName()+"&gt;");
            imagePart.setFileName(imgatc.getName());
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);
           
            Transport.send(message);

            return true;
        } catch (Exception er) {
            throw new RuntimeException(er);
        }
    }

}
