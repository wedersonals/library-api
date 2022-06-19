package br.wals.libraryapi.service.impl;

import br.wals.libraryapi.exception.BusinessException;
import br.wals.libraryapi.model.entity.Book;
import br.wals.libraryapi.model.repository.BookRepository;
import br.wals.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(book);
    }
}
