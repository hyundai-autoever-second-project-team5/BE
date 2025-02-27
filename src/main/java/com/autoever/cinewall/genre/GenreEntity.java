package com.autoever.cinewall.genre;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "genre")
public class GenreEntity {
    @Id
    @Column(name = "genre_id")
    private int genreId;

    @Column(nullable = false)
    private String name;
}
