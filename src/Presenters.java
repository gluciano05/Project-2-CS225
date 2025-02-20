/**
 *  project 2 - our version of the Logic Puzzles Game
 *  Bruna A, Daniel W, Gabriel L.
 *
 *  Presenters - category's subclass
 */

import java.util.List;

public class Presenters extends Category{ //Bruna 02/20
    public Presenters(FileHandler fileHandler){
        super("Presenters", fileHandler.getPresenters());
    }
}
