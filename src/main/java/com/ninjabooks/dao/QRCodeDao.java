package com.ninjabooks.dao;

import com.ninjabooks.domain.QRCode;

import java.util.Optional;

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

    Optional<QRCode> getByData(String data);
}
