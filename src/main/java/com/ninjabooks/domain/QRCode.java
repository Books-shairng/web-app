package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "Qr_code")
public class QRCode extends BaseEntity
{
    @Column(name = "data")
    private QRCode data;

    public QRCode() {
    }

    public QRCode getData() {
        return data;
    }

    public void setData(QRCode data) {
        this.data = data;
    }
}
