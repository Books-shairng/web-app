package com.ninjabooks.domain;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represent book in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @author Andrzej Zajst
 * @since 1.0
 */
//@AnalyzerDef(name = "searchtokenanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//    filters = {
//        @TokenFilterDef(factory = StandardFilterFactory.class),
//        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//        @TokenFilterDef(factory = StopFilterFactory.class, params = {
//            @Parameter(name = "ignoreCase", value = "true")})})
//@Analyzer(definition = "searchtokenanalyzer")

@Entity
@Indexed
@Table(name = "BOOK")
public class Book extends BaseEntity
{
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "TITLE")
    private String title;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "AUTHOR")
    private String author;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "ISBN")
    private String isbn;

    @OneToOne
    @JoinColumn(name = "QR_code_id")
    private QRCode QRCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<Queue> queues = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<Borrow> borrows = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<History> histories = new ArrayList<>(0);

    public Book() {
    }

    /**
     * Create new istance of book object
     *
     * @param title
     * @param author
     * @param isbn
     */

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, name = "searchField")
    public String getSearchQuery() {
        return author + " " + title + " " + isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public QRCode getQRCode() {
        return QRCode;
    }

    public void setQRCode(QRCode QRCode) {
        this.QRCode = QRCode;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public void setQueues(List<Queue> queues) {
        this.queues = queues;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
            Objects.equals(author, book.author) &&
            Objects.equals(isbn, book.isbn) &&
            Objects.equals(QRCode, book.QRCode) &&
            Objects.equals(queues, book.queues) &&
            Objects.equals(borrows, book.borrows) &&
            Objects.equals(histories, book.histories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn, QRCode, queues, borrows, histories);
    }

    @Override
    public String toString() {
        return "Book{" +
            "title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", isbn='" + isbn + '\'' +
            ", QRCode=" + QRCode +
            ", queues=" + queues +
            ", borrows=" + borrows +
            ", histories=" + histories +
            '}';
    }
}
