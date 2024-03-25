package com.devskiller.library.service;

import com.devskiller.library.exception.BookBorrowingNotFoundException;
import com.devskiller.library.exception.BookNotAvailableException;
import com.devskiller.library.exception.BookNotFoundException;
import com.devskiller.library.model.Book;
import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;
import com.devskiller.library.repository.BooksRepository;
import com.devskiller.library.repository.BorrowingsRepository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BookService {
    private final BooksRepository booksRepository;
    private final BorrowingsRepository borrowingsRepository;

    public BookService(BooksRepository booksRepository, BorrowingsRepository borrowingsRepository) {
        this.booksRepository = booksRepository;
        this.borrowingsRepository = borrowingsRepository;
    }

    public void borrowBook(User user, Book book) {
        validateBorrow(book);
        var usersBooks = borrowingsRepository.getByUser(user);
        usersBooks.add(new BookBorrowing(book));
        borrowingsRepository.save(user, usersBooks);

    }

    private void validateBorrow(Book book) {
        var bookCount = booksRepository.getBookCount(book).orElseThrow(BookNotFoundException::new);
        if (bookCount == 0) throw new BookNotAvailableException();
    }

    public void returnBook(User user, Book book) throws BookBorrowingNotFoundException {
        var bookCount = booksRepository.getBookCount(book).orElse(0);
        var usersBooks = borrowingsRepository.getByUser(user);
        var returnBook = usersBooks.stream().filter(userBook -> userBook.getBook().equals(book)).findAny().orElseThrow(BookBorrowingNotFoundException::new);
        usersBooks.remove(returnBook);
        borrowingsRepository.save(user, usersBooks);
        booksRepository.save(book, bookCount + 1);
    }

    public void addBookCopy(Book book) {
        var bookCount = booksRepository.getBookCount(book).orElse(0);
        booksRepository.save(book, bookCount + 1);
    }

    public void removeBookCopy(Book book) {
        if (booksRepository.getDistinctBooksCount() == 0) throw new BookNotFoundException();
        var bookCount = booksRepository.getBookCount(book).orElseThrow(BookNotAvailableException::new);
        if (bookCount > 0) {
            booksRepository.save(book, bookCount - 1);
        } else throw new BookNotAvailableException();

    }

    public int getAvailableBookCopies(Book book) {
        var bookCount = booksRepository.getBookCount(book);
        return bookCount.orElseThrow(BookNotFoundException::new);

    }
}
