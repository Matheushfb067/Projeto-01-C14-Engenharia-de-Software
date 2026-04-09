package cinema.notification;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotification {
    public static void main(String[] args) {
        // 1. Captura as variáveis das Secrets do GitHub
        String dest = System.getenv("EMAIL_TO");
        String user = System.getenv("SMTP_USER");
        String rawPassword = System.getenv("SMTP_PASSWORD");
        
        // Limpa espaços da senha de app do Google
        String password = (rawPassword != null) ? rawPassword.replace(" ", "") : null;

        // Validação das variáveis de ambiente
        if (dest == null || user == null || password == null) {
            System.err.println("Erro: As variáveis de ambiente EMAIL_TO, SMTP_USER ou SMTP_PASSWORD não foram configuradas.");
            System.exit(1);
        }

        // 2. Captura o status do pipeline enviado pelo GitHub Actions
        String status = (args.length > 0) ? args[0].toLowerCase() : "finalizado";

        // 3. Personaliza a mensagem baseada no status
        String label = status.equals("success") ? "OK" : "ERRO";
        String subject = "[" + label + "] Status do Pipeline: " + status.toUpperCase();
        String body = "O pipeline do Sistema de Filmes foi executado.\nResultado: " + status.toUpperCase();

        // Configurações do servidor SMTP do Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 4. Sessão com autenticação segura
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            // Criação da mensagem
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dest));
            message.setSubject(subject);
            message.setText(body);

            // Envio do e-mail
            Transport.send(message);
            
            System.out.println("Notificação de " + status + " enviada com sucesso para " + dest);
            
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}