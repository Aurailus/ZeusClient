package ZeusClient.game;

import ZeusClient.engine.helpers.ArrayTrans3D;
import ZeusClient.game.blockmodels.MeshPart;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ChunkMeshBuilder {
    public float[] verts;
    public float[] texCoords;
    public float[] normals;
    public int[] indices;


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
        for (var i = 0; i < face.positions.length/3; i++) {
            vertsList.add(face.positions[i*3] + offset.x);
            vertsList.add(face.positions[i*3+1] + offset.y);
            vertsList.add(face.positions[i*3+2] + offset.z);
        }

        for (var i = 0; i < face.normals.length; i++) {
            normalsList.add(face.normals[i]);
        }

        for (var i = 0; i < face.texData.length; i++) {
            texCoordsList.add(face.texData[i]);
        }

        for (var i = 0; i < face.indices.length; i++) {
            var indiceVal = currIndiceVal + face.indices[i];
            indicesList.add(indiceVal);
        }
        currIndiceVal += face.positions.length/3;
    }

    public ChunkMeshBuilder(BlockChunk blockChunk, BlockAtlas atlas) {

        vertsList = new ArrayList<>();
        texCoordsList = new ArrayList<>();
        normalsList = new ArrayList<>();
        indicesList = new ArrayList<>();

        Vector3i offset = new Vector3i(0, 0, 0);

        for (var i = 0; i < blockChunk.getVisibleArray().length; i++) {
            ArrayTrans3D.indToVec(i, offset); //Set offset

            if (blockChunk.getVisible(offset)) {
                var adj = blockChunk.getAdjacent(offset);
                var bm = atlas.blockDefs.get(blockChunk.getBlock(offset)).getModel();

                addFaces(bm.noCulledMP, offset);

                if (!atlas.blockDefs.get(adj[0]).getCulls()) { //X Pos
                    addFaces(bm.xPosMP, offset);
//                    for (MeshPart p : bm.xPosMP) addFace(p, offset);
                }
                if (!atlas.blockDefs.get(adj[1]).getCulls()) { //X Neg
                    addFaces(bm.xNegMP, offset);
//                    for (MeshPart p : bm.xNegMP) addFace(p, offset);
                }
                if (!atlas.blockDefs.get(adj[2]).getCulls()) { //Y Pos
                    addFaces(bm.yPosMP, offset);
//                    for (MeshPart p : bm.yPosMP) addFace(p, offset);
                }
                if (!atlas.blockDefs.get(adj[3]).getCulls()) { //Y Neg
                    addFaces(bm.yNegMP, offset);
//                    for (MeshPart p : bm.yNegMP) addFace(p, offset);
                }
                if (!atlas.blockDefs.get(adj[4]).getCulls()) { //Z Pos
                    addFaces(bm.zPosMP, offset);
//                    for (MeshPart p : bm.zPosMP) addFace(p, offset);
                }
                if (!atlas.blockDefs.get(adj[5]).getCulls()) { //Z Neg
                    addFaces(bm.zNegMP, offset);
//                    for (MeshPart p : bm.zNegMP) addFace(p, offset);
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