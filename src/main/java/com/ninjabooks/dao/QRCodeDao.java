package com.ninjabooks.dao;

import com.ninjabooks.domain.QRCode;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QRCodeDao extends GenericDao<QRCode, Long>
{
    QRCode getByData(String data);
}
