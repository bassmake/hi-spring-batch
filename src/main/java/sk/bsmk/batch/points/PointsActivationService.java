package sk.bsmk.batch.points;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PointsActivationService {

  private static final Logger log = LoggerFactory.getLogger(PointsActivationService.class);

  public void activate(UUID pointsId) {
    log.info("Activating points with id={}", pointsId);
  }

}
