package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable{
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this(playingSide, Collections.EMPTY_MAP, TilePos.OFF_BOARD, 0); //POTENTIAL ISSUE (MAYBE TWO??? ON MAP ITS DEFFUAULT?)
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {

        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        if(troopMap.containsKey(pos)) {
            return Optional.of(troopMap.get(pos));
        }
        else{
            return Optional.empty();
        }
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        if(isLeaderPlaced()==false) {
            return TilePos.OFF_BOARD;
        }
        return leaderPosition;
    }

    public int guards() {
        //if(guards > 2 || guards < 0) { //POTENTIAL ISUE (UNCERTAIN AMOUNT OF GUARDS TO ADD)
        //    throw new RuntimeException("To much or not enough guards");
        //}
        return guards;
    }

    public boolean isLeaderPlaced() { //POTENTIAL ISSUE (WE ARE JUST PASSING THE OFF_BOARD)
        return !leaderPosition.equals(TilePos.OFF_BOARD);
    }

    public boolean isPlacingGuards() {
        if(isLeaderPlaced() && guards < 2) {
            return true;
        }
        else return false;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> set = new HashSet<>();
        for(BoardPos tile : troopMap.keySet() ) {
            if(troopMap.get(tile).hasTroop()) {
                set.add(tile);
            }
        }
        return set;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        Map<BoardPos, TroopTile> newTM = new HashMap<>(troopMap);
        TroopTile newTT = new TroopTile(troop, playingSide, TroopFace.AVERS); //POTENTIAL ISSUE (NO PLAYING SIDE KNOWN)
        if(newTM.containsKey(target)) {
            throw new IllegalArgumentException("Already placed a troop here");
        }
        newTM.put(target, newTT);

        BoardTroops newBT = new BoardTroops(playingSide, newTM, (leaderPosition() == TilePos.OFF_BOARD?target:leaderPosition), ((isPlacingGuards())?guards+1:guards));
        return newBT;
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if(!troopMap.containsKey(origin)||troopMap.containsKey(target))
        {
            throw new IllegalArgumentException("Invalid arguments");
        }

        Map<BoardPos, TroopTile> newTM = new HashMap<>(troopMap);
        TroopTile oldTT = troopMap.get(origin).flipped();

        newTM.remove(origin);
        newTM.put(target, oldTT);

        BoardTroops newBT = new BoardTroops(playingSide, newTM, (origin.equalsTo(leaderPosition().i(), leaderPosition.j())?target:leaderPosition()), guards()); //POTENTIAL ISSUE (LATE LEADER POS UPDATE)
        return newBT;
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition(), guards());
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if(!troopMap.containsKey(target)) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTM = new HashMap<>(troopMap);
        newTM.remove(target);

        return new BoardTroops(playingSide, newTM, (target.equalsTo(leaderPosition().i(), leaderPosition.j()))?TilePos.OFF_BOARD:leaderPosition, guards);
    }

    boolean diferentNaming = true;
    boolean sortBeforePrint = true;


    public void toogleNaming()
    {
        diferentNaming = !diferentNaming;
    }

    public  void toogleSort()
    {
        diferentNaming = !diferentNaming;
    }


    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");

        //PLAYING SIDE
        writer.print("\"side\":");
        playingSide.toJSON(writer);
        writer.print(",");

        //DRAKE
        writer.print("\"leaderPosition\":");
        if (leaderPosition.equals(TilePos.OFF_BOARD)) {
            writer.print("\"off-board\"");
        } else {
            leaderPosition.toJSON(writer);
        }
        writer.print(",");

        //GUARDS
        writer.print("\"guards\":");
        writer.print(guards);
        writer.print(",");


        List<BoardPos> keys = new ArrayList<>(troopMap.keySet());
        //TROPS
        if(diferentNaming) {
            writer.print("\"troopMap\":{");
        }
        else {
            writer.print("\"troops\":{");
        }
        diferentNaming = false;
        if(sortBeforePrint) {
            keys.sort(Comparator.comparing(BoardPos::toString));
        }
        sortBeforePrint = false;

        boolean first = true;
        for (BoardPos pos : keys) {
            TroopTile tile = troopMap.get(pos);

            if (!first) writer.print(",");
            first = false;

            writer.printf("\"%s\":", pos.toString());
            tile.toJSON(writer);
        }

        writer.print("}");

        writer.print("}");
    }
}
