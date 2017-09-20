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
    public static final LocalDate EXPECTED_RETURN_DATE = BORROW_DATE.plusDays(30);
    public static final boolean CAN_EXTEND = true;
    //endregion

    //region User field constants
    public static final String FIRSTNAME = "John";
    public static final String LASTNAME = "Dee";
    //endregion

    //region Comment constants
    public static final  String COMMENT_CONTENT = "any comment content";
    //endregion

    //region Standard entity
    public static final Book BOOK = new Book(TITLE, AUTHOR, ISBN);
    public static final User USER = new User(NAME, EMAIL, PASSWORD);
    public static final Queue QUEUE = new Queue(ORDER_DATE);
    public static final QRCode QR_CODE = new QRCode(DATA);
    public static final Borrow BORROW = new Borrow(BORROW_DATE);
    public static final History HISTORY = new History(EXPECTED_RETURN_DATE);
    public static final Comment COMMENT = new Comment(COMMENT_CONTENT);
    //endregion

    public static final Book BOOK_FULL = new Book(TITLE, AUTHOR, ISBN);
    public static final User USER_FULL = new User(NAME, EMAIL, PASSWORD);
    public static final Queue QUEUE_FULL = new Queue(ORDER_DATE);
    public static final QRCode QR_CODE_FULL = new QRCode(DATA);
    public static final Borrow BORROW_FULL = new Borrow(BORROW_DATE);
    public static final History HISTORY_FULL = new History(EXPECTED_RETURN_DATE);
    public static final Comment COMMENT_FULL = new Comment(COMMENT_CONTENT);

    static {
        setIds();
        setRelationFieldsInBook();
        setRelationFieldsInBorrow();
        setRelationFieldsInHistory();
        setRelationFieldsInQRCode();
        setRelationFieldsInUser();
        setRelationFieldsInQueue();
        setRelationFieldsInComment();
    }

    private static void setIds() {
        BOOK.setId(ID);
        USER.setId(ID);
        QUEUE.setId(ID);
        QR_CODE.setId(ID);
        BORROW.setId(ID);
        HISTORY.setId(ID);
        COMMENT.setId(ID);
    }

    private static void setRelationFieldsInBook() {
        BOOK_FULL.setHistories(Collections.singletonList(HISTORY_FULL));
        BOOK_FULL.setQueues(Collections.singletonList(QUEUE_FULL));
        BOOK_FULL.setComments(Collections.singletonList(COMMENT_FULL));
        BOOK_FULL.setQRCode(QR_CODE_FULL);
        BOOK_FULL.setBorrow(BORROW_FULL);
    }

    private static void setRelationFieldsInUser() {
        USER_FULL.setBorrows(Collections.singletonList(BORROW_FULL));
        USER_FULL.setQueues(Collections.singletonList(QUEUE_FULL));
        USER_FULL.setComments(Collections.singletonList(COMMENT_FULL));
        USER_FULL.setHistory(HISTORY_FULL);
    }

    private static void setRelationFieldsInQueue() {
        QUEUE_FULL.setBook(BOOK_FULL);
        QUEUE_FULL.setUser(USER_FULL);
    }

    private static void setRelationFieldsInQRCode() {
        QR_CODE_FULL.setBook(BOOK_FULL);
    }

    private static void setRelationFieldsInBorrow() {
        BORROW_FULL.setBook(BOOK_FULL);
        BORROW_FULL.setUser(USER_FULL);
    }

    private static void setRelationFieldsInHistory() {
        HISTORY_FULL.setBook(BOOK_FULL);
        HISTORY_FULL.setUser(USER_FULL);
    }

    private static void setRelationFieldsInComment() {
        COMMENT_FULL.setBook(BOOK_FULL);
        COMMENT_FULL.setUser(USER_FULL);
    }
}
