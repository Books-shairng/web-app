package com.ninjabooks.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * This class represent QR code table in database.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "QR_CODE")
public class QRCode
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATA")
    @Type(type = "serializable")
    private QRCode data;

    public QRCode() {
    }

    public QRCode getData() {
        return data;
    }

    public void setData(QRCode data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QRCode{" +
                "id=" + id +
                ", data=" + data +
                '}';
    }
}
