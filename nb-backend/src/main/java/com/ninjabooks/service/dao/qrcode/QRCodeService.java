package com.ninjabooks.service.dao.qrcode;

import com.ninjabooks.domain.QRCode;
import com.ninjabooks.service.dao.generic.GenericService;

import java.util.Optional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface QRCodeService extends GenericService<QRCode, Long>
{
    Optional<QRCode> getByData(String  data);
}
