package sk.bsmk.batch.batches;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(stagedBuilder = true)
public interface RawRow {

  int lineNumber();

  String line();
}
