package com.manoj.spring.ai.app.service;

import com.manoj.spring.ai.app.entity.HelpDeskTicket;
import com.manoj.spring.ai.app.model.TicketRequest;
import com.manoj.spring.ai.app.repository.HelpDeskTicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpDeskTicketService {

    private final HelpDeskTicketRepository helpDeskTicketRepository;


    public HelpDeskTicketService(HelpDeskTicketRepository helpDeskTicketRepository) {
        this.helpDeskTicketRepository = helpDeskTicketRepository;
    }

    public HelpDeskTicket createTicket(TicketRequest ticketRequest, String username) {
        HelpDeskTicket ticket = HelpDeskTicket.builder()
                .issue(ticketRequest.issue())
                .username(username)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();
        return helpDeskTicketRepository.save(ticket);
    }

    public List<HelpDeskTicket> getTicketsByUsername(String username) {
        return helpDeskTicketRepository.findByUsername(username);
    }
}
