package com.sk02.sk02_reservation_service.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "periods")
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startDate;
    private Date endDate;

    @ManyToOne
    private Room room;
}
