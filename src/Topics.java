/**
 *  project 2 - our version of the Logic Puzzles Game
 *  Bruna A, Daniel W, Gabriel L.
 *
 *  Topics - category's subclass
 */

import java.util.List;

public class Topics extends Category{ //Bruna 02/18
    public Topics(FileHandler fileHandler){
        super("Topics", fileHandler.getCategoryData("Topics"));
    }
}
