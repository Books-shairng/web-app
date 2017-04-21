package com.ninjabooks.dao;

import com.ninjabooks.domain.QRCode;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QRCodeDao extends GenericDao<QRCode, Long>
{
    /**
     * Find qr code by specified generated data.
     *
     * @param data as search argument
     * @return qr data
     */

    QRCode getByData(String data);
}
