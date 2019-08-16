package sk.bsmk.batch.jobs;

public class JobParameterKeys {

  private static final String START = "#{jobParameters['";
  private static final String END = "']}";

  public static final String FILE_NAME = "FILE_NAME";
  public static final String FILE_NAME_PARAM = START + FILE_NAME + END;

  public static final String BATCH_ID = "BATCH_ID";
  public static final String BATCH_ID_PARAM = START + BATCH_ID + END;
}
