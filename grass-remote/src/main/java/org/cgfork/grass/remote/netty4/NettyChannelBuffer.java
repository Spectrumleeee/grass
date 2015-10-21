/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.remote.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.cgfork.grass.remote.ChannelBuffer;

/**
 * 
 */
public class NettyChannelBuffer implements ChannelBuffer {
    private final ByteBuf byteBuf;
    
    public NettyChannelBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }
    
    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public void clear() {
        byteBuf.clear();
    }

    @Override
    public ChannelBuffer copy() {
        return new NettyChannelBuffer(byteBuf.copy());
    }

    @Override
    public ChannelBuffer copy(int index, int length) {
        return new NettyChannelBuffer(byteBuf.copy(index, length));
    }

    @Override
    public void discardReadBytes() {
        byteBuf.discardReadBytes();
    }

    @Override
    public void ensuredWritableBytes(int writableBytes) {
        byteBuf.ensureWritable(writableBytes);
    }

    @Override
    public byte getByte(int index) {
        return byteBuf.getByte(index);
    }

    @Override
    public void getBytes(int index, byte[] dst) {
        byteBuf.getBytes(index, dst);
    }

    @Override
    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        byteBuf.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public void getBytes(int index, ByteBuffer dst) {
        byteBuf.getBytes(index, dst);
    }

    @Override
    public void getBytes(int index, ChannelBuffer dst) {
        // TODO:
    }

    @Override
    public void getBytes(int index, ChannelBuffer dst, int length) {
     // TODO:
    }

    @Override
    public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {
     // TODO:
    }

    @Override
    public void getBytes(int index, OutputStream dst, int length)
            throws IOException {
        byteBuf.getBytes(index, dst, length);
    }
    
    @Override
    public short getShort(int index) {
        return byteBuf.getShort(index);
    }
    
    @Override
    public long getLong(int index) {
        return byteBuf.getLong(index);
    }
    
    @Override
    public boolean getBoolean(int index) {
        return byteBuf.getBoolean(index);
    }

    @Override
    public boolean isDirect() {
        return byteBuf.isDirect();
    }

    @Override
    public void markReaderIndex() {
        byteBuf.markReaderIndex();
    }

    @Override
    public void resetReaderIndex() {
        byteBuf.resetReaderIndex();
    }

    @Override
    public void markWriterIndex() {
        byteBuf.markWriterIndex();
    }

    @Override
    public void resetWriterIndex() {
        byteBuf.resetWriterIndex();
    }

    @Override
    public boolean isReadable() {
        return byteBuf.isReadable();
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public void readBytes(byte[] dst) {
        byteBuf.readBytes(dst);
    }

    @Override
    public void readBytes(byte[] dst, int dstIndex, int length) {
        byteBuf.readBytes(dst, dstIndex, length);
    }

    @Override
    public void readBytes(ByteBuffer dst) {
        byteBuf.readBytes(dst);
    }

    @Override
    public void readBytes(ChannelBuffer dst) {
        //TODO:
    }

    @Override
    public void readBytes(ChannelBuffer dst, int length) {
        //TODO:
    }

    @Override
    public void readBytes(ChannelBuffer dst, int dstIndex, int length) {
        //TODO:
    }

    @Override
    public void readBytes(OutputStream dst, int length) throws IOException {
        byteBuf.readBytes(dst, length);
    }

    @Override
    public ChannelBuffer readBytes(int length) {
        return new NettyChannelBuffer(byteBuf.readBytes(length));
    }
    
    @Override
    public short readShort() {
        return byteBuf.readShort();
    }
    
    @Override
    public long readLong() {
        return byteBuf.readLong();
    }
    
    @Override
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    @Override
    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    @Override
    public void readerIndex(int readerIndex) {
        byteBuf.readerIndex(readerIndex);
    }

    @Override
    public void setByte(int index, int value) {
        byteBuf.setByte(index, value);
    }

    @Override
    public void setBytes(int index, byte[] src) {
        byteBuf.setBytes(index, src);
    }

    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        byteBuf.setBytes(index, src, srcIndex, length);
    }

    @Override
    public void setBytes(int index, ByteBuffer src) {
        byteBuf.setBytes(index, src);
    }

    @Override
    public void setBytes(int index, ChannelBuffer src) {
        // TODO:
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int length) {
        //TODO:
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        // TODO:
    }

    @Override
    public int setBytes(int index, InputStream src, int length)
            throws IOException {
        return byteBuf.setBytes(index, src, length);
    }
    
    @Override
    public void setShort(int index, short value) {
        byteBuf.setShort(index, value);
    }
    
    @Override
    public void setLong(int index, long value) {
        byteBuf.setLong(index, value);
    }
    
    @Override
    public void setBoolean(int index, boolean value) {
        byteBuf.setBoolean(index, value);
    }

    @Override
    public void setIndex(int readerIndex, int writerIndex) {
        byteBuf.setIndex(readerIndex, writerIndex);
    }

    @Override
    public void skipBytes(int length) {
        byteBuf.skipBytes(length);
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return byteBuf.nioBuffer();
    }

    @Override
    public ByteBuffer toByteBuffer(int index, int length) {
        return byteBuf.nioBuffer(index, length);
    }

    @Override
    public boolean isWritable() {
        return byteBuf.isWritable();
    }

    @Override
    public int writableBytes() {
        return byteBuf.writableBytes();
    }

    @Override
    public void writeByte(int value) {
        byteBuf.writeByte(value);
    }

    @Override
    public void writeBytes(byte[] src) {
        byteBuf.writeBytes(src);
    }

    @Override
    public void writeBytes(byte[] src, int index, int length) {
        byteBuf.writeBytes(src, index, length);
    }

    @Override
    public void writeBytes(ByteBuffer src) {
        byteBuf.writeBytes(src);
    }

    @Override
    public void writeBytes(ChannelBuffer src) {
        //TODO:
    }

    @Override
    public void writeBytes(ChannelBuffer src, int length) {
        //TODO:
    }

    @Override
    public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
        //TODO:
    }

    @Override
    public int writeBytes(InputStream src, int length) throws IOException {
        return byteBuf.writeBytes(src, length);
    }
    
    @Override
    public void writeShort(short value) {
        byteBuf.writeShort(value);
    }
    
    @Override
    public void writeLong(long value) {
        byteBuf.writeLong(value);
    }
    
    @Override
    public void writeBoolean(boolean value) {
        byteBuf.writeBoolean(value);
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public void writerIndex(int writerIndex) {
        byteBuf.writerIndex(writerIndex);
    }

    @Override
    public byte[] array() {
        return byteBuf.array();
    }

    @Override
    public boolean hasArray() {
        return byteBuf.hasArray();
    }

    @Override
    public int arrayOffset() {
        return byteBuf.arrayOffset();
    }
    
}
