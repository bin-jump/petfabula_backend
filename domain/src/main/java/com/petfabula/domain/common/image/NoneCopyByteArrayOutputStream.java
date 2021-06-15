package com.petfabula.domain.common.image;

import java.io.ByteArrayOutputStream;

/**
 * Avoid copying of internal byte array
 */
public class NoneCopyByteArrayOutputStream extends ByteArrayOutputStream {

    @Override
    public synchronized byte[] toByteArray() {
        return this.buf;
    }
}
