package top.mcmtr.core.integration;

import org.mtr.core.data.Position;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.Data;
import top.mcmtr.core.generated.integration.IntegrationSchema;

import java.util.function.Consumer;

public class Integration extends IntegrationSchema {
    private static Data DATA;

    public Integration(Data data) {
        super();
        DATA = data;
    }

    public Integration(ReaderBase readerBase, Data data) {
        super(readerBase);
        DATA = data;
        updateData(readerBase);
    }

    public void iterateCatenaries(Consumer<Catenary> consumer) {
        catenaries.forEach(consumer);
    }

    public void iterateCatenaryNodePositions(Consumer<Position> consumer) {
        catenaryNodePositions.forEach(consumer);
    }

    public void add(ObjectSet<Catenary> catenaries, ObjectSet<Position> catenaryNodePositions) {
        if (catenaries != null) {
            this.catenaries.addAll(catenaries);
        }
        if (catenaryNodePositions != null) {
            this.catenaryNodePositions.addAll(catenaryNodePositions);
        }
    }

    public boolean hasData() {
        return !catenaries.isEmpty() || !catenaryNodePositions.isEmpty();
    }
}