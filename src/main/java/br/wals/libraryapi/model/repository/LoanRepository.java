package br.wals.libraryapi.model.repository;

import br.wals.libraryapi.model.entity.Book;
import br.wals.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("select case when ( count(l.id) > 0 ) then true else false end " +
            "from Loan l " +
            "where l.book = :book and coalesce(l.returned, false) = false")
    boolean existsByBookAndNotReturned(@Param("book") Book book);

    @Query(value = "select l from Loan as l join l.book as b where (b.isbn = :isbn or l.customer = :customer)")
    Page<Loan> findByBookIsbnOrCustomer(@Param("isbn") String isbn, @Param("customer") String customer, Pageable pageRequest);

    Page<Loan> findByBook(Book book, Pageable pageable);

    @Query("select l from Loan l where l.loanDate < :limitDate and coalesce(l.returned, false) = false")
    List<Loan> findByLoanDateLessThanAndNotReturned(@Param("limitDate") LocalDate limitDate);
}
