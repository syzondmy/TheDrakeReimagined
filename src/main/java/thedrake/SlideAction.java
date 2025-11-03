package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target;

        int shift = 1;
        boolean flag = true;
        while(flag) {
            Offset2D Oshift = new Offset2D(offset().x * shift, offset().y * shift);
            target = origin.stepByPlayingSide(Oshift, side);
            if(state.canStep(origin,target) || state.canCapture(origin,target)) {
                if (state.canStep(origin, target)) {
                    result.add(new StepOnly(origin, (BoardPos) target));
                } else if (state.canCapture(origin, target)) {
                    result.add(new StepAndCapture(origin, (BoardPos) target));
                    break;
                }
                shift+=1;
            }
            else {
                flag = false;
            }
        }


        return result;
    }
}
