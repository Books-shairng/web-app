package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * This class represent QR code table in database.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "QR_CODE")
public class QRCode extends BaseEntity
{
    @Column(name = "DATA", length = 10)
    private String data;

    @OneToOne(mappedBy = "QRCode")
    private Book book;

    public QRCode() {
    }

    public QRCode(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        QRCode qrCode = (QRCode) o;
        return Objects.equals(data, qrCode.data) &&
            Objects.equals(book, qrCode.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, book);
    }

    @Override
    public String toString() {
        return "QRCode{" +
            "data='" + data + '\'' +
            ", book=" + book +
            '}';
    }
}
