package sk.bsmk.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import sk.bsmk.batch.points.Points;

import java.util.List;

@Component
public class PointsActivationWriter implements ItemWriter<Points> {

  private static final Logger log = LoggerFactory.getLogger(PointsActivationWriter.class);

  @Override
  public void write(List<? extends Points> items) throws Exception {
    items.forEach(
      points -> log.info("Processed {}", points)
    );
  }

}
