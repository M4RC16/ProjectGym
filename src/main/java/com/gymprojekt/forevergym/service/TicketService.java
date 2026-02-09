package com.gymprojekt.forevergym.service;

import com.gymprojekt.forevergym.TicketProjection;
import com.gymprojekt.forevergym.UserProjection;
import com.gymprojekt.forevergym.exception.AlreadyExistsException;
import com.gymprojekt.forevergym.exception.NotFoundException;
import com.gymprojekt.forevergym.model.Ticket;
import com.gymprojekt.forevergym.repository.TicketRepository;
import org.springframework.http.ResponseEntity;
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

        if (ticket.getPrice() == null) {
            throw new IllegalArgumentException("A jegy ára kötelező!");
        }

        if(ticketRepository.findByTicketName(ticket.getTicketName()).isPresent()){
            throw new AlreadyExistsException("Van már ilyen nevű jegynév");
        }

        ticketRepository.save(ticket);
        return "Sikeres hozzáadás";
    }

}
