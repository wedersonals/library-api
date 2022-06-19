package br.wals.libraryapi.model;

import br.wals.libraryapi.model.entity.Book;
import br.wals.libraryapi.model.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado")
    public void returnTrueIfExistsIsbn() {
        String isbn = "123";
        Book book = Book.builder().isbn(isbn).author("Fulano").title("As aventuras").build();
        entityManager.persist(book);
        boolean exists = repository.existsByIsbn(isbn);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com isbn informado")
    public void returnFalseWhenIsbnDoesntExists() {
        String isbn = "123";
        boolean exists = repository.existsByIsbn(isbn);
        assertThat(exists).isFalse();
    }
}
