package com.gymprojekt.projectgym.repository;

public interface TicketProjection {

    Integer getId();
    String getTicketName();
    Integer getValidityLength();
    String getUnit();
    Integer getPrice();

}
