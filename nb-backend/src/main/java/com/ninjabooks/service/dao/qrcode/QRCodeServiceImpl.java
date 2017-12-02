package com.ninjabooks.service.dao.qrcode;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class QRCodeServiceImpl extends GenericServiceImpl<QRCode, Long> implements QRCodeService
{
    private final QRCodeDao qrCodeDao;

    @Autowired
    public QRCodeServiceImpl(GenericDao<QRCode, Long> genericDao, QRCodeDao qrCodeDao) {
        super(genericDao);
        this.qrCodeDao = qrCodeDao;
    }

    @Override
    public Optional<QRCode> getByData(String data) {
        return qrCodeDao.getByData(data);
    }
}
