package Components.Types;

public enum Registre
{
    A,
    B,
    D,
    X,
    Y,
    S,
    U,
    PC,
    DP,
    CC,

    E,
    F,
    H,
    I,
    N,
    Z,
    V,
    C;

    public boolean est8Bits()
    {
        return switch (this)
        {
            case A, B, DP, CC -> true;
            default -> false;
        };
    }
}
