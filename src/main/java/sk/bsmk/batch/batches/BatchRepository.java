package sk.bsmk.batch.batches;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class BatchRepository {

  private static final Map<UUID, Batch> db = new ConcurrentHashMap<>();

  public void store(Batch batch) {
    db.put(batch.id(), batch);
  }

  public void updateState(UUID batchId, BatchState state) {
    db.computeIfPresent(batchId, (uuid, batch) -> ImmutableBatch.copyOf(batch).withState(state));
  }

  public Batch get(UUID batchId) {
    return db.get(batchId);
  }
}
