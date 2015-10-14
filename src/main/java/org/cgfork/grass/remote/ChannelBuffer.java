/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * This interface provides on abstract view for one or more primitive byte
 * arrays (@code byte[]) and (@linkplain ByteBuffer NIO buffers).
 *      +-------------------+------------------+------------------+
 *      | discardable bytes |  readable bytes  |  writable bytes  |
 *      |                   |     (CONTENT)    |                  |
 *      +-------------------+------------------+------------------+
 *      |                   |                  |                  |
 *      0      <=      readerIndex   <=   writerIndex    <=    capacity
 */
public interface ChannelBuffer {
    int capacity();
    void clear();
    
    ChannelBuffer copy();
    ChannelBuffer copy(int index, int length);
    
    void discardReadBytes();
    
    void ensuredWritableBytes(int writableBytes);
    
    boolean equals(Object o);
    
//    ChannelBufferFactory factory();
    
    // get bytes
    byte getByte(int index);
    void getBytes(int index, byte[] dst);
    void getBytes(int index, byte[] dst, int dstIndex, int length);
    void getBytes(int index, ByteBuffer dst);
    void getBytes(int index, ChannelBuffer dst);
    void getBytes(int index, ChannelBuffer dst, int length);
    void getBytes(int index, ChannelBuffer dst, int dstIndex, int length);
    void getBytes(int index, OutputStream dst, int length) throws IOException;
    
    /**
     * Returns {@code true} if and only if this buffer is backed by an NIO
     * direct buffer.
     */
    boolean isDirect();
    
    void markReaderIndex();
    void resetReaderIndex();
    
    void markWriterIndex();
    void resetWriterIndex();
    
    boolean isReadable();
    
    int readableBytes();
    
    byte readByte();
    void readBytes(byte[] dst);
    void readBytes(byte[] dst, int dstIndex, int length);
    void readBytes(ByteBuffer dst);
    void readBytes(ChannelBuffer dst);
    void readBytes(ChannelBuffer dst, int length);
    void readBytes(ChannelBuffer dst, int dstIndex, int length);
    void readBytes(OutputStream dst, int length) throws IOException;
    ChannelBuffer readBytes(int length);
    
    int readerIndex();
    
    void readerIndex(int readerIndex);
    
    void setByte(int index, int value);
    void setBytes(int index, byte[] src);
    void setBytes(int index, byte[] src, int srcIndex, int length);
    void setBytes(int index, ByteBuffer src);
    void setBytes(int index, ChannelBuffer src);
    void setBytes(int index, ChannelBuffer src, int length);
    void setBytes(int index, ChannelBuffer src, int srcIndex, int length);
    int setBytes(int index, InputStream src, int length) throws IOException;
    
    void setIndex(int readerIndex, int writerIndex);
    
    void skipBytes(int length);
    
    ByteBuffer toByteBuffer();
    
    ByteBuffer toByteBuffer(int index, int length);
    
    boolean isWritable();
    
    int writableBytes();
    
    void writeByte(int value);
    void writeBytes(byte[] src);
    void writeBytes(byte[] src, int index, int length);
    void writeBytes(ByteBuffer src);
    void writeBytes(ChannelBuffer src);
    void writeBytes(ChannelBuffer src, int length);
    void writeBytes(ChannelBuffer src, int srcIndex, int length);
    int writeBytes(InputStream src, int length) throws IOException;
    
    int writerIndex();
    
    void writerIndex(int writerIndex);
    
    byte[] array();
    
    boolean hasArray();
    
    int arrayOffset();
}
