package com.grpcflix.movie.service;

import com.grpcflix.movie.MovieDTO;
import com.grpcflix.movie.MovieSearchAllRequest;
import com.grpcflix.movie.MovieSearchRequest;
import com.grpcflix.movie.MovieSearchResponse;
import com.grpcflix.movie.MovieServiceGrpc;
import com.grpcflix.movie.entity.Movie;
import com.grpcflix.movie.repository.MovieRepository;

import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDTO> movieDTOList = movieRepository.getMovieByGenreOrderByReleaseYear(request.getGenre().name())
                .stream()
                .map(this::parseToMovieDTO)
                .collect(Collectors.toList());
        MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder().addAllMovies(movieDTOList).build();
        responseObserver.onNext(movieSearchResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllMovies(MovieSearchAllRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDTO> allMoviesDTOList = movieRepository.findAll()
                .stream()
                .map(this::parseToMovieDTO)
                .collect(Collectors.toList());
        MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder().addAllMovies(allMoviesDTOList).build();
        responseObserver.onNext(movieSearchResponse);
        responseObserver.onCompleted();
    }

    private MovieDTO parseToMovieDTO(Movie movie) {
        return MovieDTO.newBuilder()
                .setTitle(movie.getTitle())
                .setYear(movie.getReleaseYear())
                .setRating(movie.getRating())
                .build();
    }
}
