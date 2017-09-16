package com.ninjabooks.util.constants;

import com.ninjabooks.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class DomainTestConstants
{
    public static final Long ID = 1L;
    public static final boolean ACTIVE = true;

    //region User field constants
    public static final String NAME = "John Dee";
    public static final String EMAIL = "john.dee@exmaple.com";
    public static final String PASSWORD = "Johny!Dee123";
    //endregion

    //region Book fields constants
    public static final String AUTHOR = "J. Bloch";
    public static final String TITLE = "Effective Java";
    public static final String ISBN = "978-0321356680";
    public static final BookStatus BOOK_STATUS = BookStatus.FREE;
    public static final String DESCRIPTION = "Some description";
    //endregion

    //region Queue field costants
    public static final LocalDateTime ORDER_DATE = LocalDateTime.of(2017, 3, 21, 8, 17);
    //endregion

    //region QRCode field constants
    public static final String DATA = "12345abcde";
    //endregion

    //region Borrow field constants
    public static final LocalDate BORROW_DATE = LocalDate.of(2017, 1, 1);
    public static final LocalDate RETURN_DATE = BORROW_DATE.plusDays(30);
    public static final boolean CAN_EXTEND = true;
    //endregion

    //region User field name
    public static final String FIRSTNAME = "John";
    public static final String LASTNAME = "Dee";
    //endregion

    //region hisory field
    public static final  String COMMENT = "any COMMENT";
    //endregion

    //region Standard entity
    public static final Book BOOK = new Book(TITLE, AUTHOR, ISBN);
    public static final User USER = new User(NAME, EMAIL, PASSWORD);
    public static final Queue QUEUE = new Queue(ORDER_DATE);
    public static final QRCode QR_CODE = new QRCode(DATA);
    public static final Borrow BORROW = new Borrow(BORROW_DATE);
    public static final History HISTORY = new History(BORROW_DATE, RETURN_DATE);
    //endregion

    public static final Book BOOK_FULL = new Book(TITLE, AUTHOR, ISBN);
    public static final User USER_FULL = new User(NAME, EMAIL, PASSWORD);
    public static final Queue QUEUE_FULL = new Queue(ORDER_DATE);
    public static final QRCode QR_CODE_FULL = new QRCode(DATA);
    public static final Borrow BORROW_FULL = new Borrow(BORROW_DATE);
    public static final History HISTORY_FULL = new History(BORROW_DATE, RETURN_DATE);

    static {
        setIds();
        setRelationFieldsInBook();
        setRelationFieldsInBorrow();
        setRelationFieldsInHistory();
        setRelationFieldsInQRCode();
        setRelationFieldsInUser();
        setRelationFieldsInQueue();
    }

    private static void setIds() {
        BOOK.setId(ID);
        USER.setId(ID);
        QUEUE.setId(ID);
        QR_CODE.setId(ID);
        BORROW.setId(ID);
        HISTORY.setId(ID);
    }

    private static void setRelationFieldsInBook() {
        BOOK_FULL.setHistories(Collections.singletonList(HISTORY));
        BOOK_FULL.setQRCode(QR_CODE);
        BOOK_FULL.setQueues(Collections.singletonList(QUEUE));
        BOOK_FULL.setBorrows(Collections.singletonList(BORROW));
    }

    private static void setRelationFieldsInUser() {
        USER_FULL.setBorrows(Collections.singletonList(BORROW));
        USER_FULL.setHistories(Collections.singletonList(HISTORY));
        USER_FULL.setQueues(Collections.singletonList(QUEUE));
    }

    private static void setRelationFieldsInQueue() {
        QUEUE_FULL.setBook(BOOK);
        QUEUE_FULL.setUser(USER);
    }

    private static void setRelationFieldsInQRCode() {
        QR_CODE_FULL.setBook(BOOK);
    }

    private static void setRelationFieldsInBorrow() {
        BORROW_FULL.setBook(BOOK);
        BORROW_FULL.setUser(USER);
    }

    private static void setRelationFieldsInHistory() {
        HISTORY_FULL.setBook(BOOK);
        HISTORY_FULL.setUser(USER);
    }
}
