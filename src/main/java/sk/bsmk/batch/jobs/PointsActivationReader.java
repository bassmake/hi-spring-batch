package sk.bsmk.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.bsmk.batch.points.Points;
import sk.bsmk.batch.points.PointsService;

import java.util.List;

@Component
@StepScope
public class PointsActivationReader implements ItemReader<Points> {

  private static final Logger log = LoggerFactory.getLogger(PointsActivationReader.class);

  private final PointsService pointsService;
  private final Object lock = new Object();
  private IteratorItemReader<Points> itemReader;

  @Autowired
  public PointsActivationReader(PointsService pointsService) {
    this.pointsService = pointsService;
  }

  @Override
  public Points read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    synchronized (lock) {
      if (itemReader == null) {
        log.info("Creating new iterator item reader");
        final List<Points> list = pointsService.findPointsToActivate();
        itemReader = new IteratorItemReader<>(list);
      }
      return itemReader.read();
    }
  }

}
