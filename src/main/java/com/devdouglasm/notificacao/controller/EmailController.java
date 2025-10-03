package com.devdouglasm.notificacao.controller;

import com.devdouglasm.notificacao.business.EmailService;
import com.devdouglasm.notificacao.business.dto.TarefaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> enviaEmail(@RequestBody TarefaDTO dto) {
        emailService.enviaEmail(dto);
        return ResponseEntity.ok().build();
    }
}
