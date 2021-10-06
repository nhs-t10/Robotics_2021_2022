package org.firstinspires.ftc.teamcode.managers.input.nodes;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManagerNodeResult;

public class ComboNode extends InputManagerInputNode {
    InputManagerInputNode[] conditionals;

    private InputManagerNodeResult result  = new InputManagerNodeResult(0f);
    private long previous;

    public ComboNode(InputManagerInputNode... conditionals) {
        this.conditionals = conditionals;
        if(conditionals.length > 30) throw new IllegalArgumentException("No more than 30 buttons in a combo");

        previous = 0L;
    }
    @Override
    public void init(InputManager boss) {
        for(InputManagerInputNode i : conditionals) i.init(boss);
    }

    @Override
    public void update() {
        boolean markedFalse = false;
        for(InputManagerInputNode i : conditionals) {
            i.update();
            //we need to keep looping to update everything,
            // so we have a helper variable to ensure that soemthing marked false will remain false
            long lastMask = 1;
            int currentBitIndex = 0;
            if(!markedFalse) {
                InputManagerNodeResult r = i.getResult();
                boolean rBool = r == null || r.getFloat() == 0;

                boolean previouslyTrue = (previous & lastMask) != 0;
                if(rBool && previouslyTrue) {
                    markedFalse = true;
                }
                FeatureManager.logger.log(previous);

                //record in bitwise:
                //set the bit to 0
                previous &= ~(1 << currentBitIndex);
                //if the value is true, set the bit to 1; otherwise, leave it 0
                previous |= (rBool ? 1 : 0) << currentBitIndex;

                //update bitindex & mask
                currentBitIndex++;
                lastMask <<= 1;
            }
        }
        this.result = new InputManagerNodeResult(0f);
    }

    @Override
    public InputManagerNodeResult getResult() {
        return result;
    }
}
