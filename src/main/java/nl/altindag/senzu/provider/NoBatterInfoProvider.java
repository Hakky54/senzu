package nl.altindag.senzu.provider;

public class NoBatterInfoProvider implements BatteryInfoProvider {

    private static final NoBatterInfoProvider INSTANCE = new NoBatterInfoProvider();

    private NoBatterInfoProvider() {}

    @Override
    public String getBatteryLevel() {
        return "Could not find battery information";
    }

    public static NoBatterInfoProvider getInstance() {
        return INSTANCE;
    }
}
