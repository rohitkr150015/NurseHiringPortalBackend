package com.nuhi.Nuhi.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long serviceId;

    private String name;

    private String description;

    @Column(name = "base_price")
    private String basePrice;
}
