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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
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
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public User createUser(Boolean indexingOn, Integer count) {
        // create user
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        // create friend 1
        User friend1 = new User();
        friend1.setUsername(UUID.randomUUID().toString());
        // create friend 2
        User friend2 = new User();
        friend2.setUsername(UUID.randomUUID().toString());

        // establish friends for user
        Set<User> friends = new LinkedHashSet<>();
        friends.add(friend1);
        friends.add(friend2);
        user.setFriends(friends);

        // save the user and cascade friends
        repository.save(user);

        // lookup to validate being saved
        User userActual = repository.findOne(user.getId());
        userActual.getFriends().size();
        return userActual;
    }

    @Override
    public Set<User> findUsers(Integer page, Integer size, Integer pages)
            throws TimeoutException, ExecutionException, InterruptedException {
        // Create async calls for each page
        Collection<Future<Page<User>>> running = new LinkedHashSet<>();
        Collection<Future<Page<User>>> done = new LinkedHashSet<>();
        for (int i = page; i < pages; i++) {
            LOGGER.log(Level.INFO, "FUTURE ADD START - " + i);
            PageRequest request = new PageRequest(i, size);
            running.add(findUsersFuture(request));
            LOGGER.log(Level.INFO, "FUTURE ADD END - " + i);
        }
        LOGGER.log(Level.INFO, "BEGINNING CHECK FOR DONE.");
        Set<User> users = new LinkedHashSet<>();
        while (running.size() > 0) {
            for (Future<Page<User>> future : running) {
                if (future.isDone()) {
                    LOGGER.log(Level.INFO, "FUTURE DONE");
                    Page<User> userPage = future.get();
                    LOGGER.log(Level.INFO, "FUTURE DONE - " + userPage.getNumber());
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

    @Override
    @Async("messageExecutor")
    public Future<Page<User>> findUsersFuture(Pageable pageable) {
        return new AsyncResult<>(findUsersAsync(pageable));
    }

    @Override
    @Transactional
    public Page<User> findUsersAsync(Pageable pageable) {
        LOGGER.log(Level.INFO, "QUERY START - " + pageable.getPageNumber());
        Page<User> users = repository.findAll(pageable);
        LOGGER.log(Level.INFO, "QUERY FINISH - " + pageable.getPageNumber());
        return users;
    }

}
