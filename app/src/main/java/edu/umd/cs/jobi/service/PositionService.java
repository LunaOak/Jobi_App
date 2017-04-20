package edu.umd.cs.jobi.service;


import java.util.List;

import edu.umd.cs.jobi.model.Position;

public interface PositionService {
    public void addPositionToDb(Position position);
    public Position getSPositionById(String id);
    public List<Position> getAllPositions();
    public List<Position> getFavoritePositions();
}
