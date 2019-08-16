package sk.bsmk.batch.batches;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Repository
public class BatchRepository {

  private static final Map<UUID, Batch> db = new ConcurrentHashMap<>();

  private static final MultiValueMap<UUID, Integer> unprocessedRows = new LinkedMultiValueMap<>();

  public void store(Batch batch) {
    db.put(batch.id(), batch);
  }

  public void updateState(UUID batchId, BatchState state) {
    db.computeIfPresent(batchId, (uuid, batch) -> ImmutableBatch.copyOf(batch).withState(state));
  }

  public Batch get(UUID batchId) {
    return db.get(batchId);
  }

  public void addUnprocessedRow(UUID batchId, Integer rowNumber) {
    unprocessedRows.add(batchId, rowNumber);
  }

  public List<Integer> getUnprocessedRows(UUID batchId) {
    final List<Integer> errors = unprocessedRows.get(batchId);
    return errors == null ? Collections.emptyList() : errors;
  }
}
