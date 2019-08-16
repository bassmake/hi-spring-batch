package sk.bsmk.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;
import sk.bsmk.batch.batches.RawRow;

public class RowFailureListener extends ItemListenerSupport<String, RawRow> {

  private static final Logger log = LoggerFactory.getLogger(RowFailureListener.class);

  @Override
  public void onProcessError(String item, Exception e) {
    log.error("Problem processing {}", item, e);
  }
}
