package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.TicketProjection;
import com.gymprojekt.forevergym.model.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    Optional<Ticket> findByTicketName(String ticketName);

    List<Ticket> findByPriceLessThan(Integer price);

    List<Ticket> findAllByOrderByPriceAsc();

    List<TicketProjection> findAllProjectedBy();

}
