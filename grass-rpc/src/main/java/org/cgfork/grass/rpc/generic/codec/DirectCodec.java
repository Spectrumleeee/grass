package org.cgfork.grass.rpc.generic.codec;

import org.cgfork.grass.common.UnsupportedException;
import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.common.utils.NumberUtils;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelBuffer;
import org.cgfork.grass.remote.Codec;
import org.cgfork.grass.remote.support.ChannelBufferInputStream;
import org.cgfork.grass.remote.support.ChannelBufferOutputStream;
import org.cgfork.grass.rpc.generic.Flag;
import org.cgfork.grass.rpc.generic.GenericRequest;
import org.cgfork.grass.rpc.generic.GenericResponse;
import org.cgfork.grass.rpc.serialize.ObjectInput;
import org.cgfork.grass.rpc.serialize.ObjectOutput;
import org.cgfork.grass.rpc.serialize.Serializer;
import org.cgfork.grass.rpc.serialize.Serializers;

import java.io.IOException;
import java.util.List;

/**
 *      +----------------------------------------------------+----------------+
 *      |                  Header(14 byte)                   |     Payload    |
 *      +----------------------------------------------------+----------------+
 *      |MAGIC|FLAG|VERSION|RESERVE|ID(8 byte)|Length(2 byte)| ACTUAL CONTENT |
 *      |0x95 |0x00| 0x01  | 0x00  |   long   |    0x0000    | "hello grass"  |
 *      +--------------------------+-------------------------+----------------+
 *            /    \
 *           /      \
 *      +--------------------------------------------------------+
 *      |                         FLAG                           |
 *      +--------------------------------------------------------+
 *      |NO|NO|NO|NO|EVENT/NORMAL|BUSY/NOT_BUSY|NOT_OK/OK|REQ/RSP|
 *      |  |  |  |  |      1/0   |   1/0       |   1/0   |  1/0  |
 *      +--------------------------------------------------------+
 *
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
@Addon("directCodec")
public class DirectCodec implements Codec {

    private static final int HEADER_LENGTH = 14;

    private static final byte MAGIC = (byte)0x95;

    public DirectCodec() {}

    @Override
    public void encode(Channel channel, ChannelBuffer out, Object message) throws IOException {
        if (message instanceof GenericRequest) {
            encodeRequest(channel, out, (GenericRequest)message);
            return;
        } else if (message instanceof GenericResponse) {
            encodeResponse(channel, out, (GenericResponse)message);
            return;
        }
        throw new UnsupportedException("Unsupported message type");
    }

    @Override
    public boolean decode(Channel channel, ChannelBuffer in, List<Object> out) throws IOException {
        if (in.readableBytes() < HEADER_LENGTH) {
            return false;
        }
        byte[] header = new byte[HEADER_LENGTH];
        in.readBytes(header);
        if (header[0] != MAGIC) {
            throw new IOException("Wrong magic");
        }
        Flag flag = new Flag(header[1]);
        if (flag.isReq()) {
            return decodeRequest(channel, in, out, flag, header);
        }
        return decodeResponse(channel, in, out, flag, header);
    }

    protected void encodeRequest(Channel channel, ChannelBuffer out, GenericRequest request) throws IOException {
        byte[] header = new byte[HEADER_LENGTH];
        //set magic
        header[0] = MAGIC;

        // set flag
        Flag flag = request.getFlag();
        if (flag == null) {
            flag = new Flag(Flag.FLAG_REQ);
        }
        flag.setReqFlag();
        header[1] = flag.getFlag();

        // set id
        NumberUtils.putLong(header, 4, request.getId());

        // serialize data and write to buffer
        int markWriteIndex = out.writerIndex();
        out.writerIndex(markWriteIndex + HEADER_LENGTH);
        ChannelBufferOutputStream stream = new ChannelBufferOutputStream(out);
        try {
            Serializer serializer = Serializers.loadSerializer(channel);
            ObjectOutput output = serializer.serialize(channel.location(), stream, flag);
            output.writeObject(request.getData());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to serialize invoke data", e);
        }
        stream.flush();
        stream.close();
        // write header
        short bodyLength = (short)stream.writtenBytes();
        NumberUtils.putShort(header, 12, bodyLength);
        out.writerIndex(markWriteIndex);
        out.writeBytes(header);
        // reset write index
        out.writerIndex(markWriteIndex + HEADER_LENGTH + bodyLength);
    }

    protected void encodeResponse(Channel channel, ChannelBuffer out, GenericResponse response) throws IOException {
        byte[] header = new byte[HEADER_LENGTH];
        //set magic
        header[0] = MAGIC;

        // set flag
        Flag flag = response.getFlag();
        if (flag == null) {
            flag = new Flag(Flag.FLAG_RSP);
        }
        flag.setRspFlag();
        header[1] = flag.getFlag();

        // set id
        NumberUtils.putLong(header, 4, response.getId());
        // serialize data and write to buffer
        int markWriteIndex = out.writerIndex();
        out.writerIndex(markWriteIndex + HEADER_LENGTH);
        ChannelBufferOutputStream stream = new ChannelBufferOutputStream(out);
        try {
            Serializer serializer = Serializers.loadSerializer(channel);
            ObjectOutput output = serializer.serialize(channel.location(), stream, flag);
            output.writeObject(response.getData());
            output.flush();
        } catch (Exception e) {
            throw new IOException("Failed to serialize response data", e);
        }
        stream.flush();
        stream.close();
        // write header
        short bodyLength = (short)stream.writtenBytes();
        NumberUtils.putShort(header, 12, bodyLength);
        out.writerIndex(markWriteIndex);
        out.writeBytes(header);
        // reset write index
        out.writerIndex(markWriteIndex + HEADER_LENGTH + bodyLength);
    }

    protected boolean decodeRequest(Channel channel, ChannelBuffer in,
               List<Object> out, Flag flag, byte[] header) throws IOException {
        long id = NumberUtils.getLong(header, 4);
        short length = NumberUtils.getShort(header, 12);
        if (in.readableBytes() < length) {
            return false;
        }

        ChannelBufferInputStream stream = new ChannelBufferInputStream(in);

        try {
            Serializer serializer = Serializers.loadSerializer(channel);
            ObjectInput input = serializer.deserialize(channel.location(), stream, flag);
            GenericRequest request = new GenericRequest(id);
            request.setFlag(flag);
            request.setData(input.readObject(length));
            out.add(request);
            return true;
        } catch (Exception e) {
            throw new IOException("Failed to deserialize request data", e);
        }
    }

    protected boolean decodeResponse(Channel channel, ChannelBuffer in,
               List<Object> out, Flag flag, byte[] header) throws IOException {
        long id = NumberUtils.getLong(header, 4);
        short length = NumberUtils.getShort(header, 12);
        if (in.readableBytes() < length) {
            return false;
        }

        ChannelBufferInputStream stream = new ChannelBufferInputStream(in);

        try {
            Serializer serializer = Serializers.loadSerializer(channel);
            ObjectInput input = serializer.deserialize(channel.location(), stream, flag);
            GenericResponse response = new GenericResponse();
            response.setId(id);
            response.setFlag(flag);
            response.setData(input.readObject(length));
            out.add(response);
            return true;
        } catch (Exception e) {
            throw new IOException("Failed to deserialize response data", e);
        }
    }
}
