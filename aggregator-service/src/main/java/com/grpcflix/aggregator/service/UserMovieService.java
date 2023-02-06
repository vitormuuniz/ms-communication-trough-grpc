package com.grpcflix.aggregator.service;

import org.springframework.stereotype.Service;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.common.Genre;
import com.grpcflix.movie.MovieDTO;
import com.grpcflix.movie.MovieSearchAllRequest;
import com.grpcflix.movie.MovieSearchRequest;
import com.grpcflix.movie.MovieSearchResponse;
import com.grpcflix.movie.MovieServiceGrpc;
import com.grpcflix.user.UserGenreUpdateRequest;
import com.grpcflix.user.UserSearchRequest;
import com.grpcflix.user.UserSearchResponse;
import com.grpcflix.user.UserServiceGrpc;

import java.util.List;
import java.util.stream.Collectors;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userBlockingStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieBlockingStub;

    public List<RecommendedMovie> getUserRecommendedMovies(String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserSearchResponse userSearchResponse = userBlockingStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userSearchResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = movieBlockingStub.getMovies(movieSearchRequest);

        return movieSearchResponse.getMoviesList()
                .stream()
                .map(this::parseToRecommendedMovie)
                .collect(Collectors.toList());
    }

    private RecommendedMovie parseToRecommendedMovie(MovieDTO movie) {
        return new RecommendedMovie(movie.getTitle(), movie.getYear(), movie.getRating());
    }

    public void updateUserGenre(UserGenre userGenre) {
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        userBlockingStub.updateUserGenre(userGenreUpdateRequest);
    }

    public List<RecommendedMovie> getAllMovies() {
        MovieSearchAllRequest movieSearchAllRequest = MovieSearchAllRequest.newBuilder().build();
        MovieSearchResponse movieSearchAllResponse = movieBlockingStub.getAllMovies(movieSearchAllRequest);

        return movieSearchAllResponse.getMoviesList()
                .stream()
                .map(this::parseToRecommendedMovie)
                .collect(Collectors.toList());
    }
}
