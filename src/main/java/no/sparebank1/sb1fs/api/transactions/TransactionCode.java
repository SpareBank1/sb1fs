package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum TransactionCode {
    R_013, R_014, R_156, R_200, R_201, R_531, R_561, R_710, R_714, R_760;

    @JsonValue
    public String toValue() {
        switch (this) {
        case R_013: return "R_013";
        case R_014: return "R_014";
        case R_156: return "R_156";
        case R_200: return "R_200";
        case R_201: return "R_201";
        case R_531: return "R_531";
        case R_561: return "R_561";
        case R_710: return "R_710";
        case R_714: return "R_714";
        case R_760: return "R_760";
        }
        return null;
    }

    @JsonCreator
    public static TransactionCode forValue(String value) throws IOException {
        if (value.equals("R_013")) return R_013;
        if (value.equals("R_014")) return R_014;
        if (value.equals("R_156")) return R_156;
        if (value.equals("R_200")) return R_200;
        if (value.equals("R_201")) return R_201;
        if (value.equals("R_531")) return R_531;
        if (value.equals("R_561")) return R_561;
        if (value.equals("R_710")) return R_710;
        if (value.equals("R_714")) return R_714;
        if (value.equals("R_760")) return R_760;
        throw new IOException("Cannot deserialize TransactionCode");
    }
}
