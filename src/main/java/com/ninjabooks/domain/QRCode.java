package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    @Column(name = "DATA")
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
