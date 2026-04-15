package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.PaymentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRegistrationRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentRegistrationResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.PaymentMapper;
import edu.dosw.TECHCUP.controller.mapper.TournamentRegistrationMapper;
import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.core.model.TournamentRegistration;
import edu.dosw.TECHCUP.core.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock PaymentService paymentService;
    @Mock PaymentMapper paymentMapper;
    @Mock TournamentRegistrationMapper registrationMapper;

    @InjectMocks PaymentController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void registerTeam_returns201() throws Exception {
        TournamentRegistrationRequestDTO request = TournamentRegistrationRequestDTO.builder()
                .teamId(10L).tournamentId(20L).build();
        when(paymentService.registerTeam(eq(1L), any())).thenReturn(new TournamentRegistration());
        when(registrationMapper.toDto(any())).thenReturn(new TournamentRegistrationResponseDTO());

        mockMvc.perform(post("/api/payments/register")
                        .param("captainId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void submitPayment_returns201() throws Exception {
        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .registrationId(100L).fileUrl("http://proof.jpg").paymentMethod("TRANSFER").build();
        when(paymentService.submitPayment(eq(1L), any())).thenReturn(new Payment());
        when(paymentMapper.toDto(any())).thenReturn(new PaymentResponseDTO());

        mockMvc.perform(post("/api/payments/submit")
                        .param("captainId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void approvePayment_returns200() throws Exception {
        when(paymentService.approvePayment(1L, 200L)).thenReturn(new Payment());
        when(paymentMapper.toDto(any())).thenReturn(new PaymentResponseDTO());

        mockMvc.perform(patch("/api/payments/200/approve")
                        .param("organizerId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectPayment_returns200() throws Exception {
        when(paymentService.rejectPayment(1L, 200L, "Invalid")).thenReturn(new Payment());
        when(paymentMapper.toDto(any())).thenReturn(new PaymentResponseDTO());

        mockMvc.perform(patch("/api/payments/200/reject")
                        .param("organizerId", "1")
                        .param("reason", "Invalid"))
                .andExpect(status().isOk());
    }

    @Test
    void getPaymentsByTournament_returns200() throws Exception {
        when(paymentService.getPaymentsByTournament(1L, 20L)).thenReturn(List.of(new Payment()));
        when(paymentMapper.toDto(any())).thenReturn(new PaymentResponseDTO());

        mockMvc.perform(get("/api/payments/tournament/20")
                        .param("organizerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getRegistrationsByTournament_returns200() throws Exception {
        when(paymentService.getRegistrationsByTournament(20L))
                .thenReturn(List.of(new TournamentRegistration()));
        when(registrationMapper.toDto(any())).thenReturn(new TournamentRegistrationResponseDTO());

        mockMvc.perform(get("/api/payments/registrations/tournament/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
