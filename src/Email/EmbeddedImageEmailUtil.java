/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Email;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Late7dusk
 */
public class EmbeddedImageEmailUtil {

    /**
     * Sends an HTML e-mail with inline images.
     *
     * @param host SMTP host
     * @param port SMTP port
     * @param userName e-mail address of the sender's account
     * @param password password of the sender's account
     * @param toAddress e-mail address of the recipient
     * @param subject e-mail subject
     * @param htmlBody e-mail content with HTML tags
     * @param mapInlineImages key: Content-ID value: path of the image file
     * @throws AddressException
     * @throws MessagingException
     */
    public static void send(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String htmlBody,
            Map<String, String> mapInlineImages)
            throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds inline image attachments
        if (mapInlineImages != null && mapInlineImages.size() > 0) {
            Set<String> setImageID = mapInlineImages.keySet();

            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);

                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(imagePart);
            }
        }
        
        File e = new File("C:\\Users\\Late7dusk\\NSoftwares\\VThemeProps.def");
        
        messageBodyPart = new MimeBodyPart();
        String filesource = e.getAbsolutePath();
        String filename = e.getName();
        DataSource source = new FileDataSource(filesource);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);

        Transport.send(msg);
    }

    public static void main(String[] args) {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "kelvin.nakpil.heru@gmail.com";
        String password = "ulolkaba";

        // message info
        String mailTo = "kelvin.nakpil.heru@gmail.com";
        String subject = "Test e-mail with inline images";
        StringBuffer body
                = new StringBuffer("<html>This message contains two inline images.<br>");
        body.append("The first image is a chart:<br>");
        body.append("<img src=\"cid:image1\" width=\"30%\" height=\"30%\" /><br>");
        body.append("The second one is a cube:<br>");
        body.append("<img src=\"cid:image2\" width=\"30%\" height=\"30%\" /><br>");
        body.append("End of message.");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
        inlineImages.put("image1", "E:\\Wallpapers\\minimalistic-hackers_00428560.png");
        inlineImages.put("image2", "E:\\Wallpapers\\wp_1920x1200.jpg");

        try {
            EmbeddedImageEmailUtil.send(host, port, mailFrom, password, mailTo,
                    subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }
}
