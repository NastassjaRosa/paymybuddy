package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.controller.TransactionController;
import com.github.nastassjarosa.paymybuddy.model.Transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService service;

    @Test
    void send_shouldReturn200_andMessageWithId() throws Exception {
        Transaction tx = mock(Transaction.class);
        when(tx.getId()).thenReturn(42);

        when(service.sendMoney(eq("a@mail.com"), eq("b@mail.com"), eq(10.0), eq("desc")))
                .thenReturn(tx);

        mockMvc.perform(post("/api/transaction/send")
                        .with(user("a@mail.com").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {"senderEmail":"a@mail.com","receiverEmail":"b@mail.com","amount":10.0,"description":"desc"}
        """))
                .andExpect(status().isOk());

    }


    @Test
    void history_shouldReturn200_andList() throws Exception {
        when(service.getUserHistory("a@mail.com")).thenReturn(List.of());

        mockMvc.perform(get("/api/transaction/history/a@mail.com")
                        .with(user("a@mail.com").roles("USER")))
                .andExpect(status().isOk());

    }
}
