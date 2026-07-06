package com.manoj.spring.ai.app.tool;

import com.manoj.spring.ai.app.entity.HelpDeskTicket;
import com.manoj.spring.ai.app.model.TicketRequest;
import com.manoj.spring.ai.app.service.HelpDeskTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

public class HelpDeskTools {

    private static final Logger logger = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService helpDeskTicketService;

    public HelpDeskTools(HelpDeskTicketService helpDeskTicketService) {
        this.helpDeskTicketService = helpDeskTicketService;
    }

    @Tool(name = "createTicket", description = "Create the Support Ticket", returnDirect = true)
    String createTicket(@ToolParam(description = "Details to create a Support ticket")
                        TicketRequest ticketRequest, ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        logger.info("Creating support ticket for user: {} with details: {}", username, ticketRequest);
        HelpDeskTicket savedTicket = helpDeskTicketService.createTicket(ticketRequest, username);
        logger.info("Ticket created successfully. Ticket ID: {}, Username: {}", savedTicket.getId(), savedTicket.getUsername());
        return "Ticket #" + savedTicket.getId() + " created successfully for user " + savedTicket.getUsername();
    }

    @Tool(description = "Fetch the status of the tickets based on a given username")
    List<HelpDeskTicket> getTicketStatus(ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        logger.info("Fetching tickets for user: {}", username);
        List<HelpDeskTicket> tickets = helpDeskTicketService.getTicketsByUsername(username);
        logger.info("Found {} tickets for user: {}", tickets.size(), username);
        // throw new RuntimeException("Unable to fetch ticket status");
        return tickets;
    }
}
