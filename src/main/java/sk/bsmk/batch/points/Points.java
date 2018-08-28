package sk.bsmk.batch.points;

import org.immutables.value.Value;

import java.time.Instant;
import java.util.UUID;

@Value.Immutable
@Value.Style(stagedBuilder = true)
public interface Points {

  @Value.Default
  default UUID id() {
    return UUID.randomUUID();
  }

  String owner();

  PointState state();

  Integer amount();

  Instant createdAt();

  Instant updatedAt();

}
