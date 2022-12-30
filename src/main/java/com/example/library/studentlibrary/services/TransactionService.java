package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.*;
import com.example.library.studentlibrary.repositories.BookRepository;
import com.example.library.studentlibrary.repositories.CardRepository;
import com.example.library.studentlibrary.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    BookRepository bookRepository5;

    @Autowired
    CardRepository cardRepository5;

    @Autowired
    TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    int max_allowed_books;

    @Value("${books.max_allowed_days}")
    int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception
    {
        Card card = cardRepository5.findById(cardId).get();
        Book book = bookRepository5.findById(bookId).get();
        if(book == null || book.isAvailable() == false )
            throw new Exception("Book is either unavailable or not present");

        if(card == null || card.getCardStatus() == CardStatus.DEACTIVATED)
            throw new Exception("Card is invalid");

        if(card.getBooks().size() >= max_allowed_books)
            throw new Exception("Book limit has reached for this card");


        Transaction transaction = new Transaction();
        transaction.setFineAmount(0);
        transaction.setIssueOperation(true);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setBook(book);
        transaction.setCard(card);
        book.setAvailable(false);

        transactionRepository5.save(transaction);

        //check whether bookId and cardId already exist
        //conditions required for successful transaction of issue book:
        //1. book is present and available
        // If it fails: throw new Exception("Book is either unavailable or not present");
        //2. card is present and activated
        // If it fails: throw new Exception("Card is invalid");
        //3. number of books issued against the card is strictly less than max_allowed_books
        // If it fails: throw new Exception("Book limit has reached for this card");
        //If the transaction is successful, save the transaction to the list of transactions and return the id

        //Note that the error message should match exactly in all cases

       return transaction.getTransactionId(); //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId,TransactionStatus.SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size() - 1);

        Card card = cardRepository5.findById(cardId).get();
        Book book = bookRepository5.findById(bookId).get();

        Date issuedDate=transaction.getTransactionDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(issuedDate);

        // manipulate date
        cal.add(Calendar.DATE, getMax_allowed_days);
        Date allowedDate = cal.getTime();
        Date currentDate=new Date();
        long time_difference = currentDate.getTime() - allowedDate.getTime();
        // Calculate time difference in days
        long days_difference = (time_difference / (1000*60*60*24)) % 365;
        int fine=(int)days_difference*fine_per_day;



        book.setAvailable(true);

        Transaction returnBookTransaction = new Transaction();
        returnBookTransaction.setFineAmount(fine);
        returnBookTransaction.setIssueOperation(true);
        returnBookTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        returnBookTransaction.setBook(book);
        returnBookTransaction.setCard(card);

        transactionRepository5.save(returnBookTransaction);
        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        //make the book available for other users
        //make a new transaction for return book which contains the fine amount as well


        return returnBookTransaction; //return the transaction after updating all details
    }
}