package com.grpcflix.aggregator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.service.UserMovieService;

import java.util.List;

@RestController
public class AggregatorController {

    private final UserMovieService userMovieService;

    public AggregatorController(UserMovieService userMovieService) {
        this.userMovieService = userMovieService;
    }

    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId) {
        return userMovieService.getUserRecommendedMovies(loginId);
    }

    @PutMapping("/user")
    public void updateUserGenre(@RequestBody UserGenre userGenre) {
        userMovieService.updateUserGenre(userGenre);
    }

    @GetMapping("/movies")
    public List<RecommendedMovie> getAllMovies() {
        return userMovieService.getAllMovies();
    }
}
