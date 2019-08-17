package sk.bsmk.batch.jobs;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.InputValueSwitch;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import sk.bsmk.batch.batches.RawRow;
import sk.bsmk.batch.parser.PointsImportColumn;
import sk.bsmk.batch.parser.PointsImportRow;
import sk.bsmk.batch.person.Person;

public class PersonItemProcessor implements ItemProcessor<RawRow, Person> {

  private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

  private final CsvParser csvParser;
  private final BeanListProcessor<PointsImportRow> processor =
      new BeanListProcessor<>(PointsImportRow.class, 1);

  {
    final ObjectRowProcessor typeAProcessor =
        new ObjectRowProcessor() {
          @Override
          public void rowProcessed(Object[] row, ParsingContext context) {}
        };

    final InputValueSwitch inputValueSwitch =
        new InputValueSwitch(PointsImportColumn.TYPE.name()) {};

    final CsvParserSettings settings = new CsvParserSettings();
    settings.getFormat().setDelimiter(',');
    //    settings.setHeaders(
    //        PointsImportColumn.TYPE.name(),
    //        PointsImportColumn.FIRST_NAME.name(),
    //        PointsImportColumn.LAST_NAME.name(),
    //        PointsImportColumn.POINTS.name());
    //    settings.setProcessor(inputValueSwitch);
    settings.setProcessor(processor);
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

    csvParser.parse(new StringReader(rawRow.line()));
    final PointsImportRow row = processor.getBeans().get(0);

    final Person transformedPerson =
        new Person(row.getFirstName(), row.getSecondName(), row.getPoints());

    log.info("Converting (" + rawRow + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }
}
