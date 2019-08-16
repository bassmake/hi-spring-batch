package sk.bsmk.batch.jobs;

import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AppBatchConfigurer extends BasicBatchConfigurer {

  public AppBatchConfigurer(BatchProperties properties, DataSource dataSource, TransactionManagerCustomizers transactionManagerCustomizers) {
    super(properties, dataSource, transactionManagerCustomizers);
  }

}
