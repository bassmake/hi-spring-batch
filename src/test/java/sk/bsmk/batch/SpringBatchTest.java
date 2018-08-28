package sk.bsmk.batch;

import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class SpringBatchTest {

  @Autowired
  JobRegistry jobRegistry;

  @Autowired
  JobExplorer jobExplorer;

}
