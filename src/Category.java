/**
 *  project 2 - our version of the Logic Puzzles Game
 *  Bruna A, Daniel W, Gabriel L.
 *
 *  Category - abstract class that handles the organization of data from an input file.
 *  Each subclass (Days, Topics, Presenters) will extend this class and gather data from FileHandler.
 */

import java.util.ArrayList;

import java.util.List;

public abstract class Category { //Bruna 02/18
    protected String name;
    protected List<String> data;

    public Category(String name, List<String> data) { //Bruna 02/18
        this.name = name;
        this.data = data;
    }

    public String getName() { //Bruna 02/18
        return name;
    }

    public List<String> getData() { //Bruna 02/18
        return data;
    }
}
