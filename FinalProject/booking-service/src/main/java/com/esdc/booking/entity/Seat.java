package com.esdc.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(name = "row_label", nullable = false, length = 8)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;
}
