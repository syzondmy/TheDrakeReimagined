package thedrake;

import java.io.PrintWriter;
import java.io.Serializable;

public enum PlayingSide implements JSONSerializable {
    ORANGE,
    BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", this.name());
    }
}
