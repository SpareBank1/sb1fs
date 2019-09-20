package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum TransactionType {
    GEBYR_OG_OMKOSTNINGER, L_NN, OVERF_RING, OVERF_RING_MELLOM_EGNE_KONTOER, REGNINGSBETALING, UKJENT, VAREKJ_P, VISA;

    @JsonValue
    public String toValue() {
        switch (this) {
        case GEBYR_OG_OMKOSTNINGER: return "Gebyr og omkostninger";
        case L_NN: return "L\u251c\u00a9nn";
        case OVERF_RING: return "Overf\u251c\u00a9ring";
        case OVERF_RING_MELLOM_EGNE_KONTOER: return "Overf\u251c\u00a9ring mellom egne kontoer";
        case REGNINGSBETALING: return "Regningsbetaling";
        case UKJENT: return "UKJENT";
        case VAREKJ_P: return "Varekj\u251c\u00a9p";
        case VISA: return "Visa";
        }
        return null;
    }

    @JsonCreator
    public static TransactionType forValue(String value) throws IOException {
        if (value.equals("Gebyr og omkostninger")) return GEBYR_OG_OMKOSTNINGER;
        if (value.equals("L\u251c\u00a9nn")) return L_NN;
        if (value.equals("Overf\u251c\u00a9ring")) return OVERF_RING;
        if (value.equals("Overf\u251c\u00a9ring mellom egne kontoer")) return OVERF_RING_MELLOM_EGNE_KONTOER;
        if (value.equals("Regningsbetaling")) return REGNINGSBETALING;
        if (value.equals("UKJENT")) return UKJENT;
        if (value.equals("Varekj\u251c\u00a9p")) return VAREKJ_P;
        if (value.equals("Visa")) return VISA;
       else return UKJENT;
    }
}
