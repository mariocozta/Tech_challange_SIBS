package com.example.technical_challenge.service.email;

import com.example.technical_challenge.dto.ItemDto;

import java.util.List;

public interface IEmailService {

    void sendEmail (String to, String subject, String text);


}
