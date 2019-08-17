package sk.bsmk.batch.job;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.InputValueSwitch;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sk.bsmk.batch.parser.PointsImportRow;
import sk.bsmk.batch.parser.PointsImportRowTypeA;
import sk.bsmk.batch.parser.PointsImportRowTypeB;
import sk.bsmk.batch.person.Person;

/**
 * Here are used {@link com.univocity.parsers.common.processor.core.Processor}s to actually process
 * csv rows, a.k.a. business logic.
 */
@Component
public class PersonItemProcessor implements ItemProcessor<RawRow, Person> {

  private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

  private final CsvParser csvParser;
  private final BeanListProcessor<PointsImportRowTypeA> processorTypeA =
      new BeanListProcessor<>(PointsImportRowTypeA.class, 1);

  private final BeanListProcessor<PointsImportRowTypeB> processorTypeB =
      new BeanListProcessor<>(PointsImportRowTypeB.class, 1);

  private final InputValueSwitch inputSwitchProcessor = new InputValueSwitch(0);

  {
    inputSwitchProcessor.addSwitchForValue("A", processorTypeA);
    inputSwitchProcessor.addSwitchForValue("B", processorTypeB);

    final CsvParserSettings settings = new CsvParserSettings();
    settings.getFormat().setDelimiter(',');
    settings.setProcessor(inputSwitchProcessor);

    csvParser = new CsvParser(settings);
  }

  @Override
  public Person process(final RawRow rawRow) throws Exception {

    csvParser.parse(new StringReader(rawRow.line()));

    final PointsImportRow row = extractRow();

    final Person transformedPerson =
        new Person(row.getFirstName(), row.getLastName(), row.getPoints());

    log.info("Converting (" + rawRow + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }

  private PointsImportRow extractRow() {
    if (processorTypeA.getBeans().size() == 1) {
      return processorTypeA.getBeans().get(0);
    }
    if (processorTypeB.getBeans().size() == 1) {
      return processorTypeB.getBeans().get(0);
    }
    throw new RuntimeException("Unable to extract row");
  }
}
