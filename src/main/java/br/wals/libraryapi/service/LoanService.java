package br.wals.libraryapi.service;

import br.wals.libraryapi.model.entity.Loan;

public interface LoanService {
    Loan save(Loan loan);
}
