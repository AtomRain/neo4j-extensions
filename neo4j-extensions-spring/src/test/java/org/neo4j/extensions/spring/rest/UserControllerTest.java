package org.neo4j.extensions.spring.rest;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * The User controller test.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Configurable
@TransactionConfiguration
@Ignore
@ContextConfiguration(locations = "classpath*:META-INF/spring/test-springContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    public void testCreate() {
        Response response = userController.create(false);
        FriendResult result = (FriendResult) response.getEntity();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getFriends().size() > 0);
    }

}
