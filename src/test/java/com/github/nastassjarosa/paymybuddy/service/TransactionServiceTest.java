package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.TransactionRepository;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserConnectionService connectionService;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = new User("Sender", "sender@mail.com", "hash");
        receiver = new User("Receiver", "receiver@mail.com", "hash");
    }


    @Test
    void sendMoney_shouldThrow_whenAmountIsZeroOrNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.sendMoney("a@mail.com", "b@mail.com", 0, "desc"));

        assertEquals("Amount must be > 0", ex.getMessage());

        verifyNoInteractions(userRepository, transactionRepository, connectionService);
    }

    @Test
    void sendMoney_shouldThrow_whenSendingToYourself() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.sendMoney("same@mail.com", "same@mail.com", 10, "desc"));

        assertEquals("Cannot send money to yourself", ex.getMessage());

        verifyNoInteractions(userRepository, transactionRepository, connectionService);
    }

    @Test
    void sendMoney_shouldThrow_whenSenderNotFound() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.sendMoney("sender@mail.com", "receiver@mail.com", 10, "desc"));

        assertEquals("Sender not found", ex.getMessage());

        verify(userRepository).findByEmail("sender@mail.com");
        verify(userRepository, never()).findByEmail("receiver@mail.com");
        verifyNoInteractions(connectionService, transactionRepository);
    }

    @Test
    void sendMoney_shouldThrow_whenReceiverNotFound() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.sendMoney("sender@mail.com", "receiver@mail.com", 10, "desc"));

        assertEquals("Receiver not found", ex.getMessage());

        verify(userRepository).findByEmail("sender@mail.com");
        verify(userRepository).findByEmail("receiver@mail.com");
        verifyNoInteractions(connectionService, transactionRepository);
    }

    @Test
    void sendMoney_shouldThrow_whenUsersAreNotConnected() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));
        when(connectionService.areConnected(sender, receiver)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.sendMoney("sender@mail.com", "receiver@mail.com", 10, "desc"));

        assertEquals("Users are not connected", ex.getMessage());

        verify(connectionService).areConnected(sender, receiver);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void sendMoney_shouldSaveTransaction_whenValid() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));
        when(connectionService.areConnected(sender, receiver)).thenReturn(true);

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.sendMoney("sender@mail.com", "receiver@mail.com", 10, "desc");

        assertNotNull(result);
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals(10, result.getAmount());
        assertEquals("desc", result.getDescription());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        assertEquals(sender, saved.getSender());
        assertEquals(receiver, saved.getReceiver());
        assertEquals(10, saved.getAmount());
        assertEquals("desc", saved.getDescription());
    }


    @Test
    void getUserHistory_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.getUserHistory("unknown@mail.com"));

        assertEquals("User not found", ex.getMessage());

        verify(userRepository).findByEmail("unknown@mail.com");
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void getUserHistory_shouldReturnTransactions_whenUserExists() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));

        Transaction t1 = new Transaction(sender, receiver, 5.0, "A");
        Transaction t2 = new Transaction(receiver, sender, 8.0, "B");

        when(transactionRepository.findBySenderOrReceiver(sender, sender)).thenReturn(List.of(t1, t2));

        List<Transaction> result = transactionService.getUserHistory("sender@mail.com");

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getDescription());
        assertEquals("B", result.get(1).getDescription());

        verify(transactionRepository).findBySenderOrReceiver(sender, sender);
    }
    @Test
    void sendMoney_shouldNotCheckConnection_whenReceiverNotFound() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.sendMoney("sender@mail.com", "receiver@mail.com", 10, "desc"));

        verifyNoInteractions(connectionService);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void sendMoney_shouldSaveTransaction_whenDescriptionIsNull() {
        when(userRepository.findByEmail("sender@mail.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@mail.com")).thenReturn(Optional.of(receiver));
        when(connectionService.areConnected(sender, receiver)).thenReturn(true);

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.sendMoney("sender@mail.com", "receiver@mail.com", 10, null);

        assertNotNull(result);
        assertNull(result.getDescription());
        verify(transactionRepository).save(any(Transaction.class));
    }



}
