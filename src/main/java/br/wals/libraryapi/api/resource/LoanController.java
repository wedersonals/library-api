package br.wals.libraryapi.api.resource;

import br.wals.libraryapi.api.dto.BookDTO;
import br.wals.libraryapi.api.dto.LoanDTO;
import br.wals.libraryapi.api.dto.LoanFilterDTO;
import br.wals.libraryapi.api.dto.ReturnedLoanDTO;
import br.wals.libraryapi.model.entity.Book;
import br.wals.libraryapi.model.entity.Loan;
import br.wals.libraryapi.service.BookService;
import br.wals.libraryapi.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Api("Loan API")
public class LoanController {

    private final BookService bookService;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a loan")
    public Long create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
        Loan entity = Loan.builder().book(book).customer(dto.getCustomer())
                .loanDate(LocalDate.now()).build();
        entity = loanService.save(entity);
        return entity.getId();
    }

    @PatchMapping("{id}")
    @ApiOperation("Updates loan status")
    public void returnedBook(@PathVariable("id") Long id,
                             @RequestBody ReturnedLoanDTO dto) {
        Loan loan = loanService.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());
        loanService.update(loan);
    }

    @GetMapping
    @ApiOperation("Find loans by params")
    public Page<LoanDTO> find(LoanFilterDTO filterDTO, Pageable pageRequest) {
        Page<Loan> result = loanService.find(filterDTO, pageRequest);
        List<LoanDTO> loans = result.getContent().stream()
                .map(entity -> {
                    BookDTO bookDTO = modelMapper.map(entity.getBook(), BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(loans, pageRequest, result.getTotalElements());
    }
}
