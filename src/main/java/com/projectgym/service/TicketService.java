package com.projectgym.service;

import com.projectgym.repository.TicketProjection;
import com.projectgym.exception.AlreadyExistsException;
import com.projectgym.exception.NotFoundException;
import com.projectgym.model.Ticket;
import com.projectgym.repository.TicketRepository;
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
