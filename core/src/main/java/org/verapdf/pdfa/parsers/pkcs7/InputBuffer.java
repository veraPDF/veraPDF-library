/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class InputBuffer extends ByteArrayInputStream implements Cloneable {

    public InputBuffer(byte[] buffer) {
        super(buffer);
    }

    public InputBuffer dup() {
        try {
            InputBuffer result = (InputBuffer) this.clone();
            result.mark(Integer.MAX_VALUE);
            return result;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public void truncate(int length) throws IOException {
        if (length <= available()) {
            count = pos + length;
        } else {
            throw new IOException("Invalid data length");
        }
    }

    public int peek() throws IOException {
        if (pos < count) {
            return buf[pos];
        }
        throw new IOException("Out of buffer");
    }

    public BigInteger getBigInteger(int length) throws IOException {
        if (length <= available() || length != 0) {
            byte[] data = new byte[length];
            System.arraycopy(buf, pos, data, 0, length);
            skip(length);
            return new BigInteger(data);
        }
        throw new IOException("Integer value length is invalid: " + length + ", available length: " + available());
    }

    public byte[] getBitString() throws IOException {
        int paddingBitsCount = buf[pos];
        if (paddingBitsCount < 0 || paddingBitsCount > 7) {
            throw new IOException("Invalid padding bits count");
        }
        int length = available();
        byte[] result = new byte[length - 1];
        System.arraycopy(buf, pos + 1, result, 0, length - 1);
        if (paddingBitsCount != 0) {
            result[length - 2] &= (0xFF << paddingBitsCount);
        }
        skip(length);
        return result;
    }

    public byte[] toByteArray() {
        int available = available();
        if (available <= 0) {
            return null;
        }
        byte[] result = new byte[available];
        System.arraycopy(buf, pos, result, 0, available);
        return result;
    }
}
