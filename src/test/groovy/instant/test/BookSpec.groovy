package instant.test

import grails.test.mixin.TestFor
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Book)
class BookSpec extends Specification {

    /**
     * This test fails to query on {@link java.time.Instant}s, and from what I can see it is because Instant is
     * not in org.grails.datastore.mapping.model.MappingFactory#SIMPLE_TYPES
     * <br /><br />
     * Hitting a breakpoint in @{link org.grails.datastore.mapping.model.config.GormMappingConfigurationStrategy#getPersistentProperties(org.grails.datastore.mapping.model.PersistentEntity, org.grails.datastore.mapping.model.MappingContext, org.grails.datastore.mapping.model.ClassMapping, boolean)}
     * and adding it manually using the simple types method adds it to the domain and lets the query pass
     * <br /><br />
     * <code>
     *     persistentProperties.add(propertyFactory.createSimple(entity, context, descriptor));
     * </code>
     */
    def "Test can use Java 8 date types with Dynamic Finders"() {
        given: "A book"
        Book book = new Book(title: "Grails Manual", publishedDate: LocalDate.now()).save(failOnError: true)

        expect: "The book was saved"
        book
        and: "Can find by title"
        Book.countByTitle("Grails Manual") == 1
        and:"Can find by lastUpdated"
        Book.countByLastUpdatedIsNotNull() == 1
        and:"Can find by publishedDate"
        Book.countByPublishedDateIsNotNull() == 1
        and:"Can find by dateCreated"
        Book.countByDateCreatedIsNotNull() == 1
        and:"Can find by instant"
        Book.countByDateCreated(book.dateCreated) == 1
    }
}
