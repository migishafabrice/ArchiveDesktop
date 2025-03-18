/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Archive;

/**
 *
 * @author RIIO
 */
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class SendEmail {

    public static String sendEmail(String toAddress,String subject,String htmlContent) {
        // Gmail SMTP server details
        String host = "smtp.gmail.com";
        int port = 465; // SSL port
        String username = "administration@riio.com";
        String password = "riio@administration#123";

        // Recipient's email
        //String toAddress = "recipient@example.com";

        // Email content
        
        //String htmlContent = "<html><body><h1>Hello!</h1><p>This is a test email with HTML content.</p></body></html>";

        // Send email
        try {
            sendHtmlEmail(host, port, username, password, toAddress, subject, htmlContent);
            return "Email sent successfully.";
        } catch (EmailException e) {
            return"Failed to send email: " + e.getMessage();
        }
    }

    public static void sendHtmlEmail(String host, int port, String username, String password, String toAddress, String subject, String htmlContent)
            throws EmailException {
        // Create the email
        HtmlEmail email = new HtmlEmail();
        email.setHostName(host);
        email.setSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true); // Enable SSL connection
        email.setFrom(username);
        email.addTo(toAddress);
        email.setSubject(subject);
        email.setHtmlMsg(htmlContent);

        // Send the email
        email.send();
    }
}
