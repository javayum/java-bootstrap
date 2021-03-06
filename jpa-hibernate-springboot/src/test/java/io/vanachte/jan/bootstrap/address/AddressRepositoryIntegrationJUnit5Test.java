package io.vanachte.jan.bootstrap.address;

import io.vanachte.jan.bootstrap.jpa.HibernateConfiguration;
import io.vanachte.jan.bootstrap.jpa.JpaRepositoryConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JpaRepositoryConfiguration.class,
        HibernateConfiguration.class
})
@AutoConfigurationPackage // otherwise java.lang.IllegalStateException: Unable to retrieve @EnableAutoConfiguration base packages
@DataJpaTest // https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/orm/jpa/DataJpaTest.html
// https://www.baeldung.com/spring-boot-testing
// schema.sql and data.sql are= loaded automagically
public class AddressRepositoryIntegrationJUnit5Test {

    @Inject
    TestEntityManager entityManager;

    @Inject
    AddressRepositoryJpaImpl repository;

    @Test
    public void save_should_return_entity_with_id_filled() {

        // given
        AddressJpaEntity entity = new AddressJpaEntity();
        entityManager.persist(entity);
        entityManager.flush();

        // when
        AddressJpaEntity actual = repository.save(entity);
        entityManager.flush(); // id is only generated when saving to db

        // then
        assertNotNull(actual);
        assertTrue(actual.getId() != 0,"Id should not be null / 0");
    }

    @Test
    public void save_should_persist_entity() {

        // given
        AddressJpaEntity entity = new AddressJpaEntity();
        Long id = repository.save(entity).getId();

        // when
        AddressJpaEntity actual = entityManager.find(AddressJpaEntity.class, id);

        // then
        assertNotNull(actual);
        assertTrue(actual.getId() != 0,"Id should not be null / 0");
    }
}
