package org.verapdf.pdfa.parsers.pkcs7;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DEREncodedInputStream {
    private byte valueTag;
    protected InputBuffer inputBuffer;

    public DEREncodedInputStream(byte[] data) throws IOException {
        int len = data.length;
        if (data.length < 2) {
            throw new IOException("Byte array is too short");
        }
        if (IndefiniteLengthConverter.isIndefinite(data[1])) {
            byte[] bytes = new byte[len];
            System.arraycopy(data, 0, bytes, 0, len);
            byte[] convertedBytes = (new IndefiniteLengthConverter()).convertBytes(bytes);
            if (convertedBytes == null) {
                throw new IOException("Error resolving indefinite length BER");
            } else {
                inputBuffer = new InputBuffer(convertedBytes);
            }
        } else {
            inputBuffer = new InputBuffer(data);
        }
        inputBuffer.mark(Integer.MAX_VALUE);
    }

    public DEREncodedInputStream(InputBuffer inputBuffer) {
        this.inputBuffer = inputBuffer;
        this.inputBuffer.mark(Integer.MAX_VALUE);
    }

    public DEREncodedValue getDEREncodedValue() throws IOException {
        return new DEREncodedValue(inputBuffer);
    }

    public byte[] getOID() throws IOException {
        byte tag = (byte) (0x00FF & this.inputBuffer.read());
        if (tag == DEREncodedValue.TAG_OBJECT_ID) {
            int length = DEREncodedInputStream.getDefLength(this.inputBuffer);
            if (length > this.available()) {
                throw new IOException("OID: length = " + length + " is greater than data available = " + this.available());
            }
            byte[] oid = new byte[length];
            if ((oid.length != 0) && (this.inputBuffer.read(oid) != oid.length)) {
                throw new IOException("OID: Invalid read of DER bytes sequence");
            }
            if (length < 1 || (oid[length - 1] & IndefiniteLengthConverter.LENGTH_LONG) != 0) {
                throw new IOException("OID: DER encoding is not ended");
            }
            for (int i = 0; i < length; ++i) {
                if (oid[i] == (byte) IndefiniteLengthConverter.LENGTH_LONG &&
                        (i == 0 || (oid[i - 1] & IndefiniteLengthConverter.LENGTH_LONG) == 0)) {
                    throw new IOException("OID: DER encoding contains extra bytes");
                }
            }
            return oid;
        }
        throw new IOException("OID: invalid object ID tag: " + tag);
    }

    public void getBigInteger() throws IOException {
        if (inputBuffer.read() == DEREncodedValue.TAG_INTEGER) {
            inputBuffer.getBigInteger(getDefLength(inputBuffer));
        } else {
            throw new IOException("Invalid integer tag in DER");
        }
    }

    public DEREncodedValue[] getSequence(int len) throws IOException {
        valueTag = (byte) inputBuffer.read();
        if (valueTag == DEREncodedValue.TAG_SEQUENCE) {
            return readValues(len);
        }
        throw new IOException("Invalid sequence tag in DER");
    }

    public DEREncodedValue[] getSet(int len) throws IOException {
        return getSet(len, false);
    }

    public DEREncodedValue[] getSet(int len, boolean implicit) throws IOException {
        valueTag = (byte) inputBuffer.read();
        if (valueTag == DEREncodedValue.TAG_SET || implicit) {
            return readValues(len);
        }
        throw new IOException("Invalid set tag in DER");
    }

    public DEREncodedInputStream getSubStream(int len) throws IOException {
        InputBuffer buffer = inputBuffer.dup();
        buffer.truncate(len);
        inputBuffer.skip(len);
        return new DEREncodedInputStream(buffer);
    }

    public int peekByte() throws IOException {
        return inputBuffer.peek();
    }

    public void mark(int value) {
        inputBuffer.mark(value);
    }

    public void reset() {
        inputBuffer.reset();
    }

    public int available() {
        return inputBuffer.available();
    }

    protected DEREncodedValue[] readValues(int len) throws IOException {
        byte lengthByte = (byte) inputBuffer.read();
        int length = getLength(lengthByte, inputBuffer);
        if (length == -1) {
            inputBuffer = new InputBuffer(IndefiniteLengthConverter.convertStream(inputBuffer, lengthByte, valueTag));
            if (valueTag != inputBuffer.read()) {
                throw new IOException("Data length is indefinite");
            }
            length = DEREncodedInputStream.getDefLength(inputBuffer);
        }
        if (length == 0) {
            return new DEREncodedValue[0];
        }
        DEREncodedInputStream newStr = inputBuffer.available() == length ? this : getSubStream(length);
        List<DEREncodedValue> values = new ArrayList<>(len);
        do {
            values.add(new DEREncodedValue(newStr.inputBuffer));
        } while (newStr.available() > 0);
        if (newStr.available() != 0) {
            throw new IOException("Values contain extra data");
        }
        DEREncodedValue[] result = new DEREncodedValue[values.size()];
        for (int i = 0; i < values.size(); ++i) {
            result[i] = values.get(i);
        }
        return result;
    }

    static int getDefLength(InputStream in) throws IOException {
        int length = getLength(in.read(), in);
        if (length >= 0) {
            return length;
        }
        throw new IOException("Data length is indefinite");
    }

    static int getLength(int lengthByteValue, InputStream in) throws IOException {
        int lengthByte = lengthByteValue;
        if (lengthByte == -1) {
            throw new IOException("Invalid DER length");
        }
        int resultValue;
        if ((lengthByte & 0x080) == 0x00) {    // short form
            resultValue = lengthByte;
        } else {                            // indefinite or long form
            lengthByte &= 0x07F;
            if (lengthByte == 0) {    // indefinite
                return -1;
            }
            if (lengthByte > 4) {     // more than 4Gb of data
                throw new IOException("Length tag = " + lengthByte + " is too big");
            }
            resultValue = 0x0FF & in.read();
            if (resultValue == 0) {
                throw new IOException("DER requires length value to be encoded in minimum number of bytes. Redundant length bytes found");
            }
            while (--lengthByte > 0) {
                resultValue <<= 8;
                resultValue += 0x0FF & in.read();
            }
            if (resultValue < 0) {
                throw new IOException("Invalid length bytes in DER");
            } else if (resultValue <= 127) {
                throw new IOException("DER requires length value to be encoded in minimum number of bytes. Use short form for length");
            }
        }
        return resultValue;
    }
}
