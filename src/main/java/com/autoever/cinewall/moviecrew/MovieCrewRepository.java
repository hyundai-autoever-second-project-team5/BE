package com.autoever.cinewall.moviecrew;

import com.autoever.cinewall.crew.dto.RecommendedCrew;
import com.autoever.cinewall.movie.MovieEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCrewRepository extends JpaRepository<MovieCrewEntity, Long> {
    @Query("SELECT new  com.autoever.cinewall.crew.dto.RecommendedCrew( " +
            "c.id, c.name, c.profilePath, AVG((r.rating / 5) * 100) AS avgScore " +
            ") " +
            "FROM ReviewEntity r " +
            "JOIN r.movie m " +
            "JOIN MovieCrewEntity mc ON mc.movie.movieId = m.movieId " +
            "JOIN mc.crew c " +
            "WHERE r.user.userId = :userId " +
            "GROUP BY c.id, c.name, c.profilePath " +
            "ORDER BY avgScore DESC ")
    List<RecommendedCrew> findCrewByUserId(int userId, Pageable topThree);

    @Query("SELECT mc.movie " +
            "FROM MovieCrewEntity mc " +
            "WHERE mc.crew.crewId = :crewId")
    List<MovieEntity> findMovieByCrew(int crewId);
}
