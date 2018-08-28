package sk.bsmk.batch.jobs;

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
public class PointsActivationReader implements ItemReader<Points> {

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
        final List<Points> list = pointsService.findPointsToActivate();
        itemReader = new IteratorItemReader<>(list);
      }
      return itemReader.read();
    }
  }

}
