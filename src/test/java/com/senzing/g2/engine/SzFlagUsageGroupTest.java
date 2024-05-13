package com.senzing.g2.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzFlagUsageGroupTest {

  private List<SzFlagUsageGroup> getEnumFlagGroups() {
    return Arrays.asList(SzFlagUsageGroup.values());
  }

  @ParameterizedTest
  @MethodSource("getEnumFlagGroups")
  void testGetFlags(SzFlagUsageGroup group) {
    Set<SzFlag> flags = group.getFlags();
    assertNotNull(flags, "Flags for group should not be null: " + group);
    for (SzFlag flag : flags) {
      Set<SzFlagUsageGroup> groups = flag.getGroups();
      assertNotNull(flags, "Groups for flag should not be null: " + flag);
      assertTrue(
          groups.contains(group),
          "Group ("
              + group
              + ") has flag ("
              + flag
              + ") but the "
              + "flag does not have the group.  flagsForGroup=[ "
              + flags
              + " ], groupsForFlag=[ "
              + groups
              + "]");
    }
  }
}
