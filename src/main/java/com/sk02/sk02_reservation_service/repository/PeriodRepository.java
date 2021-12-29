package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {

    Period findPeriodByStartDateBeforeAndEndDateAfterAndRoomId(Date startDate, Date endDate, Long roomId);
    Period findPeriodByStartDateAfterAndEndDateBeforeAndRoomId(Date startDate, Date endDate, Long roomId);
}
