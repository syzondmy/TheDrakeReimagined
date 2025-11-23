package thedrake;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

public class Troop implements JSONSerializable {
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    private final List<TroopAction> aversActions;
    private final List<TroopAction> reversActions;

    // Hlavní konstruktor
    public Troop(String name,  Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reversActions)
    {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    // Konstruktor, který nastavuje oba pivoty na stejnou hodnotu
    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions, Offset2D pivot)
    {
        this(name, pivot, pivot, aversActions,reversActions);
    }

    // Konstruktor, který nastavuje oba pivoty na hodnotu [1, 1]
    public Troop(String name, List<TroopAction> aversActions, List<TroopAction>reversActions)
    {
        this(name, new Offset2D(1,1),new Offset2D(1,1), aversActions, reversActions);
    }

    // Vrací jméno jednotky
    public String name()
    {
        return name;
    }

    // Vrací pivot na zadané straně jednotky
    public Offset2D pivot(TroopFace face)
    {
        switch (face) {
            case AVERS -> {
                return this.aversPivot;
            }
            default -> {
                return this.reversPivot;
            }
        }
    }

    //Vrací seznam akcí pro zadanou stranu jednotky
    public List<TroopAction> actions(TroopFace face) {
        return (face == TroopFace.AVERS)? aversActions : reversActions;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", this.name);
    }
}
