package com.example.library.studentlibrary.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @Enumerated(value = EnumType.STRING)
    private CardStatus cardStatus;

    public Card(){
        this.cardStatus = CardStatus.ACTIVATED;
    }


    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("card")
    private Student student;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("card")
    private List<Book> books;

    // addded this
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("card")
    private List<Transaction> transactions;


}
