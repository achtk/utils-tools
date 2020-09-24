package com.chua.utils.tools.common.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * MagicNumberFileFilter
 *
 * @author CH
 */
public class MagicNumberFileFilter implements FileFilter {


    /**
     * The magic number to compare against the file's bytes at the provided
     * offset.
     */
    private final byte[] magicNumbers;

    /**
     * The offset (in bytes) within the files that the magic number's bytes
     * should appear.
     */
    private final long byteOffset;

    public MagicNumberFileFilter(final byte[] magicNumber) {
        this(magicNumber, 0);
    }

    public MagicNumberFileFilter(final String magicNumber) {
        this(magicNumber, 0);
    }

    public MagicNumberFileFilter(final String magicNumber, final long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.isEmpty()) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }

        this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset()); // explicitly uses the platform default
        // charset
        this.byteOffset = offset;
    }

    public MagicNumberFileFilter(final byte[] magicNumber, final long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }

        this.magicNumbers = new byte[magicNumber.length];
        System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
        this.byteOffset = offset;
    }

    @Override
    public boolean accept(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            try {
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
                    final byte[] fileBytes = new byte[this.magicNumbers.length];
                    randomAccessFile.seek(byteOffset);
                    final int read = randomAccessFile.read(fileBytes);
                    if (read != magicNumbers.length) {
                        return false;
                    }
                    return Arrays.equals(this.magicNumbers, fileBytes);
                }
            } catch (final IOException ioe) {
                // Do nothing, fall through and do not accept file
            }
        }

        return false;
    }
}
