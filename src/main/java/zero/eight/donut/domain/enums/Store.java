package zero.eight.donut.domain.enums;

import lombok.Getter;

@Getter
public enum Store {
    CU("CU"),
    GS25("GS25"),
    SEVENELEVEN("SEVENELEVEN"),
    EMART24("EMART24"),
    MINISTOP("MINISTOP");

    private final String store;
    Store(String store){
        this.store = store;
    }
}
