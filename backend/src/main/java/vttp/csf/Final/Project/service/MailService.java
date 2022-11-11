package vttp.csf.Final.Project.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import vttp.csf.Final.Project.exception.HomeworkNerdException;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    // @Autowired
    // private MailContentBuilder mailContentBuilder;

    private final Logger logger = Logger.getLogger(MailService.class.getName());

    @Async
    public void sendMail(vttp.csf.Final.Project.model.NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("homeworkhelper@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try {
            mailSender.send(messagePreparator);
            logger.info("Activation email sent");
        } catch (MailException e) {
            logger.warning("occurred when sending mail");
            throw new HomeworkNerdException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }
}
