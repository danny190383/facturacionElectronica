package com.jvc.factunet.utilitarios;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSenderThread {

    private String usuario;
    private String password;
    private String destinatarios;
    private String asunto;
    private String mensaje;
    private String pathArchivoPDF;
    private String pathArchivoXML;

    public EmailSenderThread(String usuario, String password, String destinatarios, String asunto, String mensaje, String archivo, String xml) {
        this.usuario = usuario;
        this.password=password;
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.pathArchivoPDF = archivo;
        this.pathArchivoXML = xml;
    }

    public Properties mailProperties() {
        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", usuario);
        props.setProperty("mail.smtp.password", password);
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.auth", "true");

        return props;
    }

    public String sendMail() {
        Properties props = mailProperties();
        Session mailSession = Session.getInstance(props, new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    usuario, password);// Specify the Username and the PassWord
            }
        });
        mailSession.setDebug(false);
        try {
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(asunto);
            message.setFrom(new InternetAddress(usuario));
            message.addRecipients(Message.RecipientType.TO, destinatarios);
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mensaje, "text/html");
            multipart.addBodyPart(messageBodyPart);
            
            if(this.pathArchivoPDF != null){
                messageBodyPart = new MimeBodyPart();
                DataSource src= new FileDataSource(pathArchivoPDF);
                messageBodyPart.setDataHandler(new DataHandler(src));
                messageBodyPart.setFileName("DocumentoElectrónico.pdf");
                multipart.addBodyPart(messageBodyPart);
            }
            
            if(this.pathArchivoXML != null){
                messageBodyPart = new MimeBodyPart();
                DataSource src= new FileDataSource(pathArchivoXML);
                messageBodyPart.setDataHandler(new DataHandler(src));
                messageBodyPart.setFileName("DocumentoElectrónico.xml");
                multipart.addBodyPart(messageBodyPart);
            }
            
            message.setContent(multipart);
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            return "SUCCESS";
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return "INVALID_EMAIL";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
