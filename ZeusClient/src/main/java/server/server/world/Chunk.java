package server.server.world;

import helpers.ArrayTrans3D;
import helpers.RLE;
import org.joml.Vector3i;

public class Chunk {
    public short[] blocks;
    boolean generated;
    Vector3i pos;

    public Chunk(byte[] blocks, Vector3i pos) {
        this(blocks, false, pos);
    }

    public Chunk(short[] blocks, Vector3i pos) {
        this(blocks, false, pos);
    }

    public Chunk(byte[] blocks, boolean generated, Vector3i pos) {
        this(RLE.decodeShorts(blocks), generated, pos);
    }

    public Chunk(short[] blocks, boolean generated, Vector3i pos) {
        this.pos = pos;
        this.blocks = blocks;
        this.generated = generated;
    }

    public short getBlock(Vector3i pos) {
        return getBlock(pos.x, pos.y, pos.z);
    }

    public short getBlock(int x, int y, int z) {
        return ArrayTrans3D.get(this.blocks, x, y, z);
    }

    public short[] getBlockArray() {
        return blocks;
    }

    public void setBlockArray(short[] blocks) {
        this.blocks = blocks;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public Vector3i getPos() {
        return pos;
    }

    public Chunk mergeChunk(Chunk newChunk) throws Exception {
        if (generated) {
            if (newChunk.generated) {
                throw new Exception("Two Existing Chunks should never be merged!");
            }
            else {
                for (var i = 0; i < newChunk.blocks.length; i++) {
                    if (newChunk.blocks[i] != 0) blocks[i] = newChunk.blocks[i];
                    generated = true;
                }
            }
        }
        else {
            if (newChunk.generated) {
                for (var i = 0; i < newChunk.blocks.length; i++) {
                    if (blocks[i] == 0) blocks[i] = newChunk.blocks[i];
                    generated = true;
                }
            }
            else {
                for (var i = 0; i < newChunk.blocks.length; i++) {
                    if (newChunk.blocks[i] != 0) blocks[i] = newChunk.blocks[i];
                    generated = false;
                }
            }
        }

        return this;
    }
}
