package org.neo4j.extensions.spring.service;

import org.neo4j.extensions.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * @author bradnussbaum
 * @since 2014.09.07
 */
@Service
public interface UserService {

    Collection<User> findUsersAsync(Integer page, Integer size, Integer pages) throws TimeoutException, ExecutionException, InterruptedException;

    Future<Page<User>> findUsersFuture(Pageable pageable);

}
