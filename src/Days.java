/**
 *  project 2 - our version of the Logic Puzzles Game
 *  Bruna A, Daniel W, Gabriel L.
 *
 *  Days - category's subclass
 */

import java.util.List;

public class Days extends Category{ //Bruna 02/18
    public Days(FileHandler fileHandler){
        super("Days", fileHandler.getCategoryData("Days"));
    }
}
