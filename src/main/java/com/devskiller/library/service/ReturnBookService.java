package com.devskiller.library.service;

import com.devskiller.library.exception.BookBorrowingNotFoundException;
import com.devskiller.library.model.Book;
import com.devskiller.library.model.User;
import com.devskiller.library.repository.BooksRepository;
import com.devskiller.library.repository.BorrowingsRepository;

public class ReturnBookService {

    private final BooksRepository booksRepository;
    private final BorrowingsRepository borrowingsRepository;

    public ReturnBookService(BooksRepository booksRepository, BorrowingsRepository borrowingsRepository) {
        this.booksRepository = booksRepository;
        this.borrowingsRepository = borrowingsRepository;
    }

    public void returnBook(User user, Book book) throws BookBorrowingNotFoundException {
        var bookCount = booksRepository.getBookCount(book).orElse(0);
        var usersBooks = borrowingsRepository.getByUser(user);
        var returnBook = usersBooks.stream().filter(userBook -> userBook.getBook().equals(book)).findAny().orElseThrow(BookBorrowingNotFoundException::new);
        usersBooks.remove(returnBook);
        borrowingsRepository.save(user, usersBooks);
        booksRepository.save(book, bookCount + 1);
    }
}
