package com.projectgym.repository;

import java.time.LocalDateTime;

public interface FreeTrainingProjection {

    Integer getReservationId();

    LocalDateTime getScheduledAt();

    Boolean getIsFree();

}
