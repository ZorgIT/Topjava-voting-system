package com.github.zorgit.restaurantvotingsystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"name"})})
@ToString(exclude = {"menus"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Getter @Setter
    private String name;

    @OneToMany(mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("dateTime DESC")
    @Getter @Setter
    private List<Menu> menus;
}
