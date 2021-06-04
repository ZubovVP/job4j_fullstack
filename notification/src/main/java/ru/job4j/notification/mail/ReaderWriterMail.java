package ru.job4j.notification.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.notification.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $.
 * Date: 27.05.2021.
 */
public class ReaderWriterMail implements ActionsForEmail {
    private final String host;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderWriterMail.class.getName());

    public ReaderWriterMail(String host) {
        this.host = host;
    }

    @Override
    public boolean write(Notification email, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.mime.charset", "UTF-8");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email.getFrom(), password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFrom()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email.getTo())
            );
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            Transport.send(message);
            LOGGER.info("Send email success.");
        } catch (MessagingException e) {
            LOGGER.error("Send email failed, notification - {}.", email);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<Notification> readAllMessages(String email, String password) {
        List<Notification> msgs = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(this.host, email, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            for (var msg : inbox.getMessages()) {
                msgs.add(new Notification(null, msg.getFrom()[0].toString(), msg.getSubject(), msg.getContent().toString()));
            }
            LOGGER.info("Read emails success, email - {}.", email);
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            LOGGER.error("Read emails failed, email - {}.", email);
            e.printStackTrace();
        }
        return msgs;
    }

    public List<Notification> readOnlyNewMessages(String email, String password) {
        List<Notification> msgs = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(this.host, email, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (var msg : messages) {
                msgs.add(new Notification(null, msg.getFrom()[0].toString(), msg.getSubject(), msg.getContent().toString()));
            }
            LOGGER.info("Read emails success, email - {}.", email);
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            LOGGER.error("Read emails failed, email - {}.", email);
            e.printStackTrace();
        }
        return msgs;
    }
}
