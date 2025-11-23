package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile, JSONSerializable {

    Troop troop;
    PlayingSide side;
    TroopFace face;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face)
    {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side()
    {
        return side;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face()
    {
        return face;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop()
    {
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn()
    {
        return false;
    }

    // Vrací True
    public boolean hasTroop()
    {
        return true;
    }


    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
// (z rubu na líc nebo z líce na rub)
    public TroopTile flipped()
    {
        TroopFace flippedFace = (face == TroopFace.AVERS)? TroopFace.REVERS: TroopFace.AVERS;
        TroopTile flippedTroop = new TroopTile(troop, side, flippedFace);
        return flippedTroop;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> moves = new ArrayList<>();

        if(state.armyOnTurn().side() == side)
        {
            if(state.armyOnTurn().boardTroops().at(pos).isPresent())
            {
                for(TroopAction action : troop.actions(face))
                {
                    moves.addAll(action.movesFrom(pos,side,state));
                }
            }
        }
        return moves;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");

        writer.print("\"troop\":");
        troop.toJSON(writer);

        writer.print(",\"side\":");
        side.toJSON(writer);

        writer.print(",\"face\":");
        face.toJSON(writer);

        writer.print("}");
    }

}
