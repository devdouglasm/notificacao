package com.devdouglasm.notificacao.business;

import com.devdouglasm.notificacao.business.dto.TarefaDTO;
import com.devdouglasm.notificacao.infrastructure.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    // importa envio de email e de template
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    // cria uma variavel para o remetente do email, que sera buscado do properties
    @Value("${envio.email.remetente}")
    private String remetente;

    // cria uma variavel para o nome do remetente do email
    @Value("${envio.email.nomeRemetente}")
    private String nomeRemetente;

    public void enviaEmail(TarefaDTO dto) {
        try {
            // cria uma nova mensagem de email
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            // seta o remetente
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));

            // seta quem recebe o email
            mimeMessageHelper.setTo(InternetAddress.parse(dto.getEmailUsuario()));

            // seta o nome do email
            mimeMessageHelper.setSubject("Notificação de Tarefa");

            // cria um novo contexto para o template
            Context context = new Context();

            // seta as variaveis do arquivo html (thymelyaf)
            context.setVariable("nomeTarefa", dto.getNomeTarefa());
            context.setVariable("dataEvento", dto.getDataEvento());
            context.setVariable("descricao", dto.getDescricao());
            // processa o template com o contexto
            String template = templateEngine.process("notificacao", context);
            // adiciona o template a mensagem
            mimeMessageHelper.setText(template, true);

            // envia o email
            javaMailSender.send(mensagem);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Email não enviado " + e.getCause());
        }
    }
}
