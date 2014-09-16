package org.neo4j.extensions.spring.service;

import org.neo4j.extensions.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * @author bradnussbaum
 * @since 2014.09.07
 */
public interface UserService {

    User createUser(Boolean indexingOn, Integer count);

    Set<User> findUsers(Integer page, Integer size, Integer pages) throws TimeoutException, ExecutionException, InterruptedException;

    Future<Page<User>> findUsersFuture(Pageable pageable);

    Page<User> findUsersAsync(Pageable pageable);

}
