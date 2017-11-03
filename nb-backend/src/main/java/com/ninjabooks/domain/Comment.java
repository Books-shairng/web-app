package com.ninjabooks.domain;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "COMMENT")
public class Comment extends BaseEntity
{
    @Column(name = "CONTENT", length = 250)
    private String content;

    @Column(name = "DATE")
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public Comment() {
    }

    public Comment(String content, LocalDateTime date) {
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return
            Objects.equals(this.getId(), comment.getId()) &&
            Objects.equals(date, comment.date) &&
            Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, date);
    }

    @Override
    public String toString() {
        return "Comment{" +
            "content='" + content + '\'' +
            "date='" + date + '\'' +
            '}';
    }
}
