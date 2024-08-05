/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.pdfa.parsers.pkcs7;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class DEREncodedValue {
    public final byte valueTag;
    public final DEREncodedInputStream in;
    private int length;

    public static final byte TAG_SEQUENCE = 0x30;
    public static final byte TAG_SET = 0x31;
    public static final byte TAG_OBJECT_ID = 0x06;
    public static final byte TAG_BIT_STRING = 0x03;
    public static final byte TAG_NULL = 0x05;
    public static final byte TAG_INTEGER = 0x02;
    public static final byte TAG_UTC_TIME = 0x17;
    public static final byte TAG_GENERALIZED_TIME = 0x18;

    public DEREncodedValue(byte valueTag, byte[] bytes) {
        this.length = bytes.length;
        this.valueTag = valueTag;
        this.in = new DEREncodedInputStream(new InputBuffer(bytes.clone()));
        this.in.mark(Integer.MAX_VALUE);
    }

    public DEREncodedValue(InputBuffer in) throws IOException {
        this.valueTag = (byte) in.read();
        byte lengthByte = (byte) in.read();
        this.length = DEREncodedInputStream.getLength(lengthByte, in);
        if (this.length == -1) {
            InputBuffer buffer = new InputBuffer(IndefiniteLengthConverter.convertStream(in.dup(), lengthByte, valueTag));
            if (this.valueTag != buffer.read()) {
                throw new IOException("Data length is indefinite");
            }
            this.length = DEREncodedInputStream.getDefLength(buffer);
            InputBuffer inputBuffer = buffer.dup();
            inputBuffer.truncate(length);
            this.in = new DEREncodedInputStream(inputBuffer);
            in.skip(length + 2);
        } else {
            InputBuffer inputBuffer = in.dup();
            inputBuffer.truncate(length);
            this.in = new DEREncodedInputStream(inputBuffer);
            in.skip(length);
        }
    }

    public boolean areEmpty() {
        return length == 0;
    }

    public final byte getValueTag() {
        return valueTag;
    }

    public byte[] getBitString() throws IOException {
        if (valueTag == TAG_BIT_STRING) {
            return in.inputBuffer.getBitString();
        }
        throw new IOException("Invalid bit string tag: " + valueTag);
    }

    public BigInteger getBigInteger() throws IOException {
        if (valueTag == TAG_INTEGER) {
            return in.inputBuffer.getBigInteger(in.available());
        }
        throw new IOException("Invalid Integer tag: " + valueTag);
    }

    public boolean isConstructed() {
        return ((valueTag & 0x020) == 0x020);
    }

    public boolean isContextSpecific(byte tag) {
        if ((valueTag & 0x0c0) != 0x080) {
            return false;
        }
        return ((valueTag & 0x01F) == tag);
    }

    public DEREncodedInputStream toDEREncodedInputStream() throws IOException {
        if (valueTag != TAG_SET && valueTag != TAG_SEQUENCE) {
            throw new IOException("Invalid tag type " + valueTag + " while converting to DEREncodedInputStream");
        }
        return new DEREncodedInputStream(in.inputBuffer);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        encode(stream);
        in.reset();
        return stream.toByteArray();
    }

    public void encode(ByteArrayOutputStream stream) throws IOException {
        stream.write(valueTag);
        putLen(stream, length);
        if (length > 0) {
            byte[] val = new byte[length];
            synchronized (in) {
                in.inputBuffer.reset();
                if (in.inputBuffer.read(val) == length) {
                    stream.write(val);
                } else {
                    throw new IOException("Invalid reading of DER value");
                }
            }
        }
    }

    public void putLen(ByteArrayOutputStream out, int length) {
        byte[] lengthBytes = IndefiniteLengthConverter.getLenBytes(length);
        out.write(lengthBytes, 0, lengthBytes.length);
    }
}
