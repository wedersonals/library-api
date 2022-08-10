package br.wals.libraryapi.model.repository;

import br.wals.libraryapi.model.entity.Book;
import br.wals.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static br.wals.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
    public void existsByBookAndNotReturnedTest() {
        Loan loan = createAndPersistLoan();
        boolean exists = repository.existsByBookAndNotReturned(loan.getBook());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustomer() {
        Loan loan = createAndPersistLoan();
        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).contains(loan);
    }

    private Loan createAndPersistLoan() {
        Book book = createNewBook("123");
        entityManager.persist(book);
        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        return entityManager.persist(loan);
    }
}
