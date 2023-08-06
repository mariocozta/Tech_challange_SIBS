package com.example.technical_challenge.service.email;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.repository.ItemRepository;
import com.example.technical_challenge.dto.ItemDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("mariocozta@gmail.com");
        message.setSubject("subject");
        message.setText("text");
        javaMailSender.send(message);
    }
}
