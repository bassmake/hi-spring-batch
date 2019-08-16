package sk.bsmk.batch.jobs;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import sk.bsmk.batch.batches.PointsImportColumn;
import sk.bsmk.batch.batches.RawRow;
import sk.bsmk.batch.person.Person;

public class PersonItemProcessor implements ItemProcessor<RawRow, Person> {

  private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

  private final CsvParser csvParser;

  {
    final CsvParserSettings settings = new CsvParserSettings();
    settings.getFormat().setDelimiter(',');
    settings.setHeaders(
        PointsImportColumn.FIRST_NAME.name(),
        PointsImportColumn.LAST_NAME.name(),
        PointsImportColumn.POINTS.name());
    //    settings.setProcessorErrorHandler(new RowProcessorErrorHandler() {
    //      @Override
    //      public void handleError(DataProcessingException error, Object[] inputRow, ParsingContext
    // context) {
    //        // TODO
    //      }
    //    });
    csvParser = new CsvParser(settings);
  }

  @Override
  public Person process(final RawRow rawRow) throws Exception {

    final Record record = csvParser.parseRecord(rawRow.line());

    final String firstName = record.getString(PointsImportColumn.FIRST_NAME).toUpperCase();
    final String lastName = record.getString(PointsImportColumn.LAST_NAME).toUpperCase();
    final int points = record.getInt(PointsImportColumn.POINTS);

    final Person transformedPerson = new Person(firstName, lastName, points);

    log.info("Converting (" + record + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }
}
