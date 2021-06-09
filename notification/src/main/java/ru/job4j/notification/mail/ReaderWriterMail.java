package ru.job4j.notification.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.job4j.notification.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
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
    private final String email;
    private final String pwd;
    private final JavaMailSender sender;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderWriterMail.class.getName());

    public ReaderWriterMail(final JavaMailSender sender, String host, String email, String password) {
        this.sender = sender;
        this.host = host;
        this.email = email;
        this.pwd = password;
    }

    @Override
    public boolean write(Notification email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        this.sender.send(message);
        return true;
    }

    @Override
    public List<Notification> readAllMessages() {
        List<Notification> msgs = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(this.host, this.email, this.pwd);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            for (var msg : inbox.getMessages()) {
                msgs.add(new Notification(null, msg.getFrom()[0].toString(), msg.getSubject(), msg.getContent().toString()));
            }
            LOGGER.info("Read emails success, email - {}.", this.email);
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            LOGGER.error("Read emails failed, email - {}.", this.email);
            e.printStackTrace();
        }
        return msgs;
    }

    public List<Notification> readOnlyNewMessages() {
        List<Notification> msgs = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.host", this.host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", this.host);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(this.host, this.email, this.pwd);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (var msg : messages) {
                msgs.add(new Notification(null, msg.getFrom()[0].toString(), msg.getSubject(), msg.getContent().toString()));
            }
            LOGGER.info("Read emails success, email - {}.", this.email);
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            LOGGER.error("Read emails failed, email - {}.", this.email);
            e.printStackTrace();
        }
        return msgs;
    }
}
