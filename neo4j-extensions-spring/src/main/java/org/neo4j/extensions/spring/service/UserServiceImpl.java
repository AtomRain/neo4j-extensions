package org.neo4j.extensions.spring.service;

import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private static final org.slf4j.Logger LOGGR = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    private Neo4jTemplate template;

    @Override
    @Transactional
    public User createUser(Boolean indexingOn, Integer count) {
        LOGGER.info(String.format("POST /user/create?indexingOn=%s&count=%d", indexingOn, count));
        long startTimeTx = System.currentTimeMillis();

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
        User[] friends = new User[2];
        friends[0] = friend1;
        friends[1] = friend2;
        user.setFriends(friends);

        // save the user and cascade friends
        user = repository.save(user);

        // lookup to validate being saved
        User userActual = repository.findOne(user.getId());
        List<User> friendsActual = new ArrayList<>();
        for (User friend : userActual.getFriends()) {
            friend = template.fetch(friend);
            friend = template.convert(friend, User.class);
            friendsActual.add(friend);
        }
        if (template.isManaged(userActual)) {
            LOGGER.info("IS MANAGED!");
        } else {
            LOGGER.info("IS NOT MANAGED!");
        }
        return user;
    }

    @Override
    public User[] findUsers(Integer page, Integer size, Integer pages)
            throws TimeoutException, ExecutionException, InterruptedException {
        // Create async calls for each page
        Collection<Future<Page<User>>> running = new ArrayList<>();
        Collection<Future<Page<User>>> done = new ArrayList<>();
        for (int i = page; i < pages; i++) {
            LOGGER.log(Level.ALL, "FUTURE ADD START - " + i);
            LOGGR.info("FUTURE ADD START - " + i);
            PageRequest request = new PageRequest(i, size);
            running.add(findUsersFuture(request));
            LOGGER.log(Level.ALL, "FUTURE ADD END - " + i);
            LOGGR.info("FUTURE ADD END - " + i);
        }
        LOGGER.log(Level.ALL, "BEGINNING CHECK FOR DONE.");
        LOGGR.info("BEGINNING CHECK FOR DONE.");
        List<User> users = new ArrayList<>();
        while (running.size() > 0) {
            for (Future<Page<User>> future : running) {
                if (future.isDone()) {
                    LOGGER.log(Level.ALL, "FUTURE DONE");
                    LOGGR.info("FUTURE DONE");
                    Page<User> userPage = future.get();
                    LOGGER.log(Level.ALL, "FUTURE DONE - " + userPage.getNumber());
                    LOGGR.info("FUTURE DONE - " + userPage.getNumber());
                    for (User u : userPage) {
                        users.add(u);
                    }
                    done.add(future);
                }
            }
            running.removeAll(done);
        }
        User[] usersArray = new User[users.size()];
        usersArray = users.toArray(usersArray);
        return usersArray;
    }

    @Override
    @Async("messageExecutor")
    public Future<Page<User>> findUsersFuture(Pageable pageable) {
        return new AsyncResult<>(findUsersAsync(pageable));
    }

    @Override
    @Transactional
    public Page<User> findUsersAsync(Pageable pageable) {
        LOGGER.log(Level.ALL, "QUERY START - " + pageable.getPageNumber());
        LOGGR.info("QUERY START - " + pageable.getPageNumber());
        Page<User> users = repository.findAll(pageable);
        LOGGER.log(Level.ALL, "QUERY FINISH - " + pageable.getPageNumber());
        LOGGR.info("QUERY FINISH - " + pageable.getPageNumber());
        return users;
    }

}
