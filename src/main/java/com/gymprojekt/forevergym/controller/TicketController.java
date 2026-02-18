package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.TicketProjection;
import com.gymprojekt.forevergym.model.Ticket;
import com.gymprojekt.forevergym.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/getAll")
    public List<TicketProjection> tickets() {
        return ticketService.AllTickets();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok("Ticket törölve");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.addTicket(ticket)).toString();
    }

}
