package me.excel.tools.extractor;

import me.excel.tools.factory.TestPersonModel;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by hanwen on 2016/12/22.
 */
public class BooleanZhExtractorTest {

  @Test
  public void testGetStringValue() throws Exception {

    TestPersonModel person = TestPersonModel.create();

    BooleanZhExtractor booleanZhExtractor = new BooleanZhExtractor("person.male");
    String result = booleanZhExtractor.getStringValue(person);

    assertEquals("否", result);

  }

}