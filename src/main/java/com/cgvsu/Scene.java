package com.cgvsu;

import com.cgvsu.math.vectors.vectorFloat.Vector3f;
import com.cgvsu.model.ChangedModel;
import com.cgvsu.render_engine.Camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scene {

    String currentModel;

    // private HashMap<String, ChangedModel> loadedModels = new HashMap<>();
    public List<ChangedModel> loadedMeshes = new ArrayList<>();
    //todo: Model -> ChangedModel

    public HashMap<String, ChangedModel> loadedModels = new HashMap<>();
//    public List<Model> loadedMeshes = new ArrayList<>();
    // public HashMap<String, Model> mapLoadedMeshes = new HashMap<>();

    private List<Camera> camera = new ArrayList<>(List.of(new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100)));

    public String getCurrentModel() {
        return currentModel;
    }

    /* public HashMap<String, Model> getMapLoadedMeshes() {
        return mapLoadedMeshes;
    }

    public void setMapLoadedMeshes(HashMap<String, Model> mapLoadedMeshes) {
        this.mapLoadedMeshes = mapLoadedMeshes;
    } */

    public HashMap<String, ChangedModel> getLoadedModels() {
        return loadedModels;
    }

    public List<Camera> getCamera() {
        return camera;
    }

    public void setCurrentModel(String currentModel) {
        this.currentModel = currentModel;
    }

    public void setLoadedModels(HashMap<String, ChangedModel> loadedModels) {
        this.loadedModels = loadedModels;
    }

    public void setCamera(List<Camera> camera) {
        this.camera = camera;
    }

    public List<ChangedModel> getLoadedMeshes() {
        return loadedMeshes;
    }
}
