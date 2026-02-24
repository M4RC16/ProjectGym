package com.gymprojekt.projectgym.service;

import com.gymprojekt.projectgym.repository.TicketProjection;
import com.gymprojekt.projectgym.exception.AlreadyExistsException;
import com.gymprojekt.projectgym.exception.NotFoundException;
import com.gymprojekt.projectgym.model.Ticket;
import com.gymprojekt.projectgym.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<TicketProjection> AllTickets() {
       return ticketRepository.findAllProjectedBy();
    }

    public void deleteTicket(Long id){

        if (!ticketRepository.existsById(id)){
            throw new NotFoundException("Nincs ilyen jegy");
        }

        ticketRepository.deleteById(id);

    }

    public String addTicket(Ticket ticket){

         if (ticket.getTicketName() == null || ticket.getTicketName().trim().isEmpty()) {
            throw new IllegalArgumentException("A jegy neve kötelező!");
        }

        if (ticket.getPrice() == null || ticket.getPrice() <= 0) {
            throw new IllegalArgumentException("A jegy ára kötelező!");
        }

        if(ticketRepository.findByTicketName(ticket.getTicketName()).isPresent()){
            throw new AlreadyExistsException("Van már ilyen nevű jegy");
        }

        ticketRepository.save(ticket);
        return "Sikeres hozzáadás";
    }

}
