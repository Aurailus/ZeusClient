package client.game;

import client.game.blockmodels.MeshInfo;
import client.game.blockmodels.MeshPart;
import helpers.ArrayTrans3D;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ChunkMeshBuilder {
    public float[] verts;
    public float[] texCoords;
    public float[] normals;
    public int[] indices;

    MeshInfo m;

    private List<Float> vertsList;
    private List<Float> texCoordsList;
    private List<Float> normalsList;
    private List<Integer> indicesList;

    public int currIndiceVal = 0;

    private void addFaces(MeshPart[] parts, Vector3i offset) {
        if (parts != null)
            for (MeshPart p : parts) addFace(p, offset);
    }

    private void addFace(MeshPart face, Vector3i offset) {
        face.getMeshData(m);

        for (var i = 0; i < m.positions.length/3; i++) {
            vertsList.add(m.positions[i*3] + offset.x);
            vertsList.add(m.positions[i*3+1] + offset.y);
            vertsList.add(m.positions[i*3+2] + offset.z);
        }

        for (var i = 0; i < m.normals.length; i++) {
            normalsList.add(m.normals[i]);
        }

        for (var i = 0; i < m.texData.length; i++) {
            texCoordsList.add(m.texData[i]);
        }

        for (var i = 0; i < m.indices.length; i++) {
            var indiceVal = currIndiceVal + m.indices[i];
            indicesList.add(indiceVal);
        }
        currIndiceVal += m.positions.length/3;
    }

    public ChunkMeshBuilder(BlockChunk blockChunk, BlockChunk[] adjacent) {
        m = new MeshInfo();

        vertsList = new ArrayList<>();
        texCoordsList = new ArrayList<>();
        normalsList = new ArrayList<>();
        indicesList = new ArrayList<>();

        Vector3i offset = new Vector3i(0, 0, 0);

        boolean[] visible = blockChunk.calcVisible(adjacent);

        for (var i = 0; i < visible.length; i++) {
            ArrayTrans3D.indToVec(i, offset); //Set offset

            if (visible[i]) {
                var adj = blockChunk.getAdjacentOpaque(offset, adjacent);
                var bm = Game.definitions.getDef(blockChunk.getBlock(offset)).getModel();

                addFaces(bm.noCulledMP, offset);

                if (!adj[0]) { //X Pos
                    addFaces(bm.xPosMP, offset);
                }
                if (!adj[1]) { //X Neg
                    addFaces(bm.xNegMP, offset);
                }
                if (!adj[2]) { //Y Pos
                    addFaces(bm.yPosMP, offset);
                }
                if (!adj[3]) { //Y Neg
                    addFaces(bm.yNegMP, offset);
                }
                if (!adj[4]) { //Z Pos
                    addFaces(bm.zPosMP, offset);
                }
                if (!adj[5]) { //Z Neg
                    addFaces(bm.zNegMP, offset);
                }
            }
        }

        verts = new float[vertsList.size()];
        for (var i = 0; i < vertsList.size(); i++) {
            verts[i] = vertsList.get(i);
        }
        vertsList.clear();

        texCoords = new float[texCoordsList.size()];
        for (var i = 0; i < texCoordsList.size(); i++) {
            texCoords[i] = texCoordsList.get(i);
        }
        texCoordsList.clear();

        normals = new float[normalsList.size()];
        for (var i = 0; i < normalsList.size(); i++) {
            normals[i] = normalsList.get(i);
        }
        normalsList.clear();

        indices = new int[indicesList.size()];
        for (var i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }
        indicesList.clear();

    }
}
