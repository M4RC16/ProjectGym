package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.model.TicketType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {

    Optional<TicketType> findByTicketName(String ticketName);

    List<TicketType> findByPriceLessThan(Integer price);

    List<TicketType> findAllByOrderByPriceAsc();

}
