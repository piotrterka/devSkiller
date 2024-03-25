package com.devskiller.library.service;

import com.devskiller.library.model.User;
import com.devskiller.library.model.UserDueReport;
import com.devskiller.library.repository.BorrowingsRepository;

import java.math.BigDecimal;

public class ReportService {
    public static final BigDecimal DAILY_CHARGE = new BigDecimal("0.5");

    private final BorrowingsRepository borrowingsRepository;

    public ReportService(BorrowingsRepository borrowingsRepository) {
        this.borrowingsRepository = borrowingsRepository;
    }

    public UserDueReport getUserDueReport(User user) {
        throw new UnsupportedOperationException("getUserDue");
    }

}
