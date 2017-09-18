package com.ninjabooks.domain;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public Comment() {
    }

    public Comment(String content) {
        this.content = content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(content, comment.content) &&
            Objects.equals(user, comment.user) &&
            Objects.equals(book, comment.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, user, book);
    }

    @Override
    public String toString() {
        return "Comment{" +
            "content='" + content + '\'' +
            ", user=" + user +
            ", book=" + book +
            '}';
    }
}
