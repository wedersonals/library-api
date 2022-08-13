package br.wals.libraryapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(length = 100)
    private String customer;

    @Column(name = "customer_email")
    private String customerEmail;

    @JoinColumn(name = "id_book")
    @ManyToOne
    @ToString.Exclude
    private Book book;

    @Column
    private LocalDate loanDate;

    @Column
    private Boolean returned;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(getId(), loan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
