package sk.bsmk.batch.batches;

import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(stagedBuilder = true)
public interface Batch {

  UUID id();

  String fileName();

  BatchState state();
}
