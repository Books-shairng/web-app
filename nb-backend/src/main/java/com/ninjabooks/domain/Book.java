package com.ninjabooks.domain;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

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
@Entity
@Indexed
@AnalyzerDef(
    name = "searchtokenanalyzer",
    tokenizer = @TokenizerDef(
        factory = StandardTokenizerFactory.class),
    filters = {
        @TokenFilterDef(factory = StandardFilterFactory.class),
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(factory = StopFilterFactory.class,
            params = {
                @Parameter(name = "ignoreCase", value = "true")
            })
    }
)
@Analyzer(definition = "searchtokenanalyzer")
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

    @Column(name = "STATUS")
    @Enumerated(value = EnumType.STRING)
    private BookStatus status = BookStatus.FREE;

    @Column(name = "DESCRIPTION", length = 5000)
    private String description;

    @OneToOne
    @JoinColumn(name = "QR_code_id")
    private QRCode QRCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private Borrow borrow;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<Queue> queues = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<History> histories = new ArrayList<>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private List<Comment> comments = new ArrayList<>(0);

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

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, name = "allFields")
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public com.ninjabooks.domain.QRCode getQRCode() {
        return QRCode;
    }

    public void setQRCode(com.ninjabooks.domain.QRCode QRCode) {
        this.QRCode = QRCode;
    }

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public void setQueues(List<Queue> queues) {
        this.queues = queues;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return
            Objects.equals(this.getId(), book.getId()) &&
            Objects.equals(title, book.title) &&
            Objects.equals(author, book.author) &&
            Objects.equals(isbn, book.isbn) &&
            status == book.status &&
            Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn, status, description);
    }

    @Override
    public String toString() {
        return "Book{" +
            "title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", isbn='" + isbn + '\'' +
            ", status=" + status +
            ", description='" + description + '\'' +
            ", QRCode=" + QRCode +
            '}';
    }
}
