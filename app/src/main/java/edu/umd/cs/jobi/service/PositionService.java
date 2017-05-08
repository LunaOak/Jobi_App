package edu.umd.cs.jobi.service;


import java.util.List;

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Position;

public interface PositionService {
    public void addPositionToDb(Position position);
    public boolean deletePositionById(String id);
    public Position getPositionById(String id);
    public List<Position> getAllPositions();
    public List<Position> getFavoritePositions();
    public Contact getContactById(String id);
    public boolean deleteContactById(String id);
}
