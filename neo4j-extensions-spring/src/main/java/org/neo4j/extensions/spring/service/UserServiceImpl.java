package org.neo4j.extensions.spring.service;

import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bradnussbaum
 * @since 2014.09.07
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository repository;

    @Override
    public Collection<User> findUsersAsync() throws TimeoutException, ExecutionException, InterruptedException {
        Collection<Future<Page<User>>> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PageRequest request = new PageRequest(i, 10);
            results.add(findUsersFuture(request));
        }
        Collection<User> users = new LinkedHashSet<>();
        for (Future<Page<User>> result : results) {
            Page<User> userPage = result.get(60, TimeUnit.SECONDS);
            LOGGER.log(Level.INFO, "Method: UserServiceImp.findUsers" +
                    " number: " + userPage.getNumber() +
                    " totalElements: " + userPage.getTotalElements() +
                    " totalPages: " + userPage.getTotalPages());
            for (User u : userPage) {
                users.add(u);
            }
        }
        return users;
    }

    @Async("messageExecutor")
    public Future<Page<User>> findUsersFuture(Pageable pageable) {
        LOGGER.log(Level.INFO, "Method: UserServiceImp.findUsersAsync" +
                " pageNumber: " + pageable.getPageNumber() +
                " pageSize: " + pageable.getPageSize() +
                " offset: " + pageable.getOffset());
        Page<User> users = repository.findAll(pageable);
        return new AsyncResult<>(users);
    }

}
