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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class IndefiniteLengthConverter {
    public static final int LENGTH_LONG = 0x80; // bit 8 set
    private static final int TAG_MASK = 0x1F; // bits 5-1
    private static final int FORM_MASK = 0x20; // bits 6
    private static final int CLASS_MASK = 0xC0; // bits 8 and 7
    private static final int LENGTH_MASK = 0x7F; // bits 7 - 1

    private final ArrayList<Object> indefiniteDataList = new ArrayList<>();
    private int unresolved = 0;
    private int totalLenBytesCount = 0;
    private byte[] curData;
    private byte[] newData;
    private int newDataIndex;
    private int curDataIndex;
    private int curDataSize;
    private int index;

    public static boolean isIndefinite(int lenByte) {
        return (isLongForm(lenByte) && ((lenByte & LENGTH_MASK) == 0));
    }

    public static boolean isLongForm(int lenByte) {
        return ((lenByte & LENGTH_LONG) == LENGTH_LONG);
    }

    public static byte[] convertStream(InputStream in, byte lengthByte, byte tag) throws IOException {
        int bytesReadLength = in.available();
        int offset = 2;
        byte[] indefiniteData = new byte[bytesReadLength + offset];
        indefiniteData[0] = tag;
        indefiniteData[1] = lengthByte;
        while (true) {
            int bytesReadCount = in.read(indefiniteData, offset, bytesReadLength);
            if (bytesReadLength != bytesReadCount) {
                indefiniteData = Arrays.copyOf(indefiniteData, offset + bytesReadCount);
                bytesReadLength = bytesReadCount;
            }
            byte[] bytes = (new IndefiniteLengthConverter()).convertBytes(indefiniteData);
            if (bytes != null) {
                return bytes;
            } else {
                int nextByte = in.read();
                if (nextByte == -1) {
                    throw new IOException("Error resolving indefinite length BER");
                }
                int available = in.available();
                indefiniteData = Arrays.copyOf(indefiniteData, offset + bytesReadLength + available + 1);
                indefiniteData[bytesReadLength + offset] = (byte) nextByte;
                offset += bytesReadLength + 1;
                bytesReadLength = available;
            }
        }
    }

    public byte[] convertBytes(byte[] indefiniteData) throws IOException {
        index = 0;
        curDataIndex = 0;
        curData = indefiniteData;
        curDataSize = curData.length;
        int unusedBytes = 0;
        while (curDataIndex < curDataSize) {
            if (curDataIndex + 2 <= curDataSize) {
                parseTag();
                int length = parseLen();
                if (length >= 0) {
                    curDataIndex += length;
                    if (unresolved == 0) {
                        unusedBytes = curDataSize - curDataIndex;
                        curDataSize = curDataIndex;
                        break;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (unresolved != 0) {
            return null;
        }
        curDataIndex = 0;
        newDataIndex = 0;
        index = 0;
        newData = new byte[curDataSize + totalLenBytesCount + unusedBytes];
        while (curDataIndex < curDataSize) {
            writeTag();
            writeLenAndVal();
        }
        System.arraycopy(indefiniteData, curDataSize, newData, curDataSize + totalLenBytesCount, unusedBytes);
        return newData;
    }

    private boolean isEndOfContents(int tag) {
        return (((tag & TAG_MASK) == 0x00) &&  // end-of-contents type tag
                ((tag & CLASS_MASK) == 0x00)) && // universal
                ((tag & FORM_MASK) == 0x00); // primitive
    }

    private void parseTag() throws IOException {
        if (curData[curDataIndex + 1] != 0 || !isEndOfContents(curData[curDataIndex])) {
            curDataIndex++;
            return;
        }
        int i;
        int encapsulatedLengthBytesCount = 0;
        Object item = null;
        for (i = indefiniteDataList.size() - 1; i >= 0; --i) {
            item = indefiniteDataList.get(i);
            if (!(item instanceof Integer)) {
                encapsulatedLengthBytesCount += ((byte[]) item).length - 3;
            } else {
                break;
            }
        }
        if (i < 0) {
            throw new IOException("End of contents doesn't have matching indefinite len tag");
        }
        byte[] sectLengthBytes = getLenBytes(curDataIndex + encapsulatedLengthBytesCount - (Integer) item);
        indefiniteDataList.set(i, sectLengthBytes);
        unresolved--;
        totalLenBytesCount += (sectLengthBytes.length - 3);
        curDataIndex++;
    }

    private void writeTag() {
        if (curDataSize != curDataIndex) {
            int tag = curData[curDataIndex++];
            if (curData[curDataIndex] != 0 || !isEndOfContents(tag)) {
                newData[newDataIndex++] = (byte) tag;
            } else {
                curDataIndex++;
                writeTag();
            }
        }
    }

    // parse the length and if it is an indefinite length then add the current position to the indefiniteDataList.
    // return the length of definite length data next, or -1 if there is not enough bytes to determine it
    private int parseLen() throws IOException {
        int len = 0;
        if (curDataSize != curDataIndex) {
            int lengthByte = curData[curDataIndex++] & 0xFF;
            if (isIndefinite(lengthByte)) {
                indefiniteDataList.add(curDataIndex);
                unresolved++;
                return len;
            }
            if (!isLongForm(lengthByte)) {
                len = (lengthByte & LENGTH_MASK);
            } else {
                lengthByte &= LENGTH_MASK;
                if (lengthByte > 4) {
                    throw new IOException("Data is too big");
                }
                if (curDataSize - curDataIndex < lengthByte + 1) {
                    return -1;
                }
                for (int i = 0; i < lengthByte; i++) {
                    len = (len << 8) + (curData[curDataIndex++] & 0xFF);
                }
                if (len < 0) {
                    throw new IOException("Length bytes are invalid");
                }
            }
        }
        return len;
    }

    // write the length and if it is an indefinite length then calculate the definite length
    // from the positions of the indefinite length and its matching EOC terminator.
    // then, write the value.
    private void writeLenAndVal() throws IOException {
        if (curDataSize != curDataIndex) {
            int lengthByte = curData[curDataIndex++] & 0xFF;
            if (isIndefinite(lengthByte)) {
                byte[] lenBytes = (byte[]) indefiniteDataList.get(index++);
                System.arraycopy(lenBytes, 0, newData, newDataIndex, lenBytes.length);
                newDataIndex += lenBytes.length;
                return;
            }
            int len = 0;
            if (!isLongForm(lengthByte)) {
                len = (lengthByte & LENGTH_MASK);
            } else {
                lengthByte &= LENGTH_MASK;
                for (int i = 0; i < lengthByte; i++) {
                    len = (len << 8) + (curData[curDataIndex++] & 0xFF);
                }
                if (len < 0) {
                    throw new IOException("Length bytes are invalid");
                }
            }
            writeLen(len);
            for (int i = 0; i < len; ++i) {
                newData[newDataIndex++] = curData[curDataIndex++];
            }
        }
    }

    private void writeLen(int len) {
        byte[] lengthBytes = getLenBytes(len);
        System.arraycopy(lengthBytes, 0, newData, newDataIndex, lengthBytes.length);
        newDataIndex += lengthBytes.length;
    }

    protected static byte[] getLenBytes(int len) {
        int i = 0;
        byte[] lengthBytes;
        if (len >= (1 << 24)) {
            lengthBytes = new byte[5];
            lengthBytes[i++] = (byte) 0x84;
            lengthBytes[i++] = (byte) (len >> 24);
            lengthBytes[i++] = (byte) (len >> 16);
            lengthBytes[i++] = (byte) (len >> 8);
            lengthBytes[i] = (byte) len;
        } else if (len >= (1 << 16)) {
            lengthBytes = new byte[4];
            lengthBytes[i++] = (byte) 0x83;
            lengthBytes[i++] = (byte) (len >> 16);
            lengthBytes[i++] = (byte) (len >> 8);
            lengthBytes[i] = (byte) len;
        } else if (len >= (1 << 8)) {
            lengthBytes = new byte[3];
            lengthBytes[i++] = (byte) 0x82;
            lengthBytes[i++] = (byte) (len >> 8);
            lengthBytes[i] = (byte) len;
        } else if (len >= 128) {
            lengthBytes = new byte[2];
            lengthBytes[i++] = (byte) 0x81;
            lengthBytes[i] = (byte) len;
        } else {
            lengthBytes = new byte[1];
            lengthBytes[i] = (byte) len;
        }
        return lengthBytes;
    }
}
