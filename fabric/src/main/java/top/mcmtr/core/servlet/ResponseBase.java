package top.mcmtr.core.servlet;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import top.mcmtr.core.simulation.Simulator;

public class ResponseBase<T> {
    protected final String data;
    protected final Object2ObjectAVLTreeMap<String, String> parameters;
    protected final T body;
    protected final long currentMillis;
    protected final Simulator simulator;

    public ResponseBase(String data, Object2ObjectAVLTreeMap<String, String> parameters, T body, long currentMillis, Simulator simulator) {
        this.data = data;
        this.parameters = parameters;
        this.body = body;
        this.currentMillis = currentMillis;
        this.simulator = simulator;
    }
}