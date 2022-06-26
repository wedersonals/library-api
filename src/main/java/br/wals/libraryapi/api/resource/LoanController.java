package br.wals.libraryapi.api.resource;

import br.wals.libraryapi.api.dto.LoanDTO;
import br.wals.libraryapi.model.entity.Book;
import br.wals.libraryapi.model.entity.Loan;
import br.wals.libraryapi.service.BookService;
import br.wals.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final BookService bookService;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
        Loan entity = Loan.builder().book(book).customer(dto.getCustomer())
                .loanDate(LocalDate.now()).build();
        entity = loanService.save(entity);
        return entity.getId();
    }
}
