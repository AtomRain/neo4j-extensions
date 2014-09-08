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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bradnussbaum
 * @since 2014.09.07
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository repository;

    @Override
    public Collection<User> findUsersAsync(Integer page, Integer size, Integer pages)
            throws TimeoutException, ExecutionException, InterruptedException {
        // Create async calls for each page
        Collection<Future<Page<User>>> running = new ArrayList<>();
        Collection<Future<Page<User>>> done = new ArrayList<>();
        for (int i = page; i < pages; i++) {
            PageRequest request = new PageRequest(i, size);
            running.add(findUsersFuture(request));
        }
        Collection<User> users = new LinkedHashSet<>();
        while (running.size() > 0) {
            Iterator<Future<Page<User>>> iterator = running.iterator();
            while (iterator.hasNext()) {
                Future<Page<User>> future = iterator.next();
                if (future.isDone()) {
                    Page<User> userPage = future.get();
                    LOGGER.log(Level.INFO, "Method: UserServiceImp.findUsers" +
                            " number: " + userPage.getNumber() +
                            " totalElements: " + userPage.getTotalElements() +
                            " totalPages: " + userPage.getTotalPages());
                    for (User u : userPage) {
                        users.add(u);
                    }
                    done.add(future);
                }
            }
            running.removeAll(done);
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
