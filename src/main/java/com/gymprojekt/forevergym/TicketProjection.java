package com.gymprojekt.forevergym;

import com.gymprojekt.forevergym.model.Ticket;

public interface TicketProjection {

    Integer getId();
    String getTicketName();
    Integer getValidityLength();
    String getUnit();
    Integer getPrice();

}
