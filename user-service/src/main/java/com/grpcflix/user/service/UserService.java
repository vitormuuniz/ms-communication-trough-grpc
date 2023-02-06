package com.grpcflix.user.service;

import javax.transaction.Transactional;

import com.grpcflix.common.Genre;
import com.grpcflix.user.UserGenreUpdateRequest;
import com.grpcflix.user.UserSearchRequest;
import com.grpcflix.user.UserSearchResponse;
import com.grpcflix.user.UserServiceGrpc;
import com.grpcflix.user.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        UserSearchResponse.Builder userResponse = UserSearchResponse.newBuilder();
        userRepository.findById(request.getLoginId())
                .ifPresent(user ->
                        userResponse.setName(user.getName())
                                .setLoginId(user.getLogin())
                                .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
                );
        responseObserver.onNext(userResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserSearchResponse> responseObserver) {
        UserSearchResponse.Builder userResponse = UserSearchResponse.newBuilder();
        userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().name());
                    userRepository.save(user);
                    userResponse.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(userResponse.build());
        responseObserver.onCompleted();
    }
}
