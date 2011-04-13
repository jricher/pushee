package org.mitre.pushee.hub.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.pushee.hub.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;

/**
 * @author mfranklin
 *         Date: 4/12/11
 *         Time: 11:08 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/application-context.xml", "classpath:test-context.xml"})
public class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository repository;

    @Test
    public void getById_validId_validResult() {
        Publisher publisher = repository.get(1L);
        assertThat(publisher, CoreMatchers.<Publisher>notNullValue());
        assertThat(publisher.getCallbackURL(), CoreMatchers.equalTo("http://example.com/pub/1"));

    }

    @Test
    public void getById_invalidId_nullResult() {
        Publisher publisher = repository.get(10000L);
        assertThat(publisher, CoreMatchers.<Publisher>nullValue());

    }
}
