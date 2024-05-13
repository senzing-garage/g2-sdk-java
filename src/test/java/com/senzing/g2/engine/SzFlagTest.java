package com.senzing.g2.engine;

import java.lang.reflect.*;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import static com.senzing.g2.engine.SzFlag.*;
import static com.senzing.g2.engine.Utilities.*;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@Disabled
public class SzFlagTest {
  /**
   * The {@link Map} of {@link String} constant names from
   * {@link SzFlags} to their values.
   */
  private Map<String, Long> flagsMap = new LinkedHashMap<>();

  /**
   * The {@link Map} of {@link String} constant names from
   * {@link SzFlag} to their values.
   */
  private Map<String, Long> enumsMap = new LinkedHashMap<>();

  /**
   * The {@link Map} of {@link String} field names for declared
   * constants of {@link Set}'s of {@link SzFlag} instance to the
   * actual {@link Set} of {@link SzFlag} instances.
   */
  private Map<String, Set<SzFlag>> setsMap = new LinkedHashMap<>();

  @BeforeAll
  public void reflectFlags() {
    Field[] fields = SzFlags.class.getDeclaredFields();
    for (Field field : fields) {
      int modifiers = field.getModifiers();
      if (field.getType() != Long.TYPE)
        continue;
      if (!Modifier.isStatic(modifiers))
        continue;
      if (!Modifier.isFinal(modifiers))
        continue;
      if (!field.getName().startsWith("SZ_"))
        continue;

      try {
        this.flagsMap.put(field.getName(), field.getLong(null));

      } catch (IllegalAccessException e) {
        fail("Got exception in reflection.", e);
      }
    }

    // populate the enums
    for (SzFlag flag : SzFlag.values()) {
      this.enumsMap.put(flag.name(), flag.toLong());
    }

    // populate the enum sets
    fields = SzFlag.class.getDeclaredFields();
    for (Field field : fields) {
      int modifiers = field.getModifiers();
      if (field.getType() != Set.class)
        continue;
      if (!Modifier.isStatic(modifiers))
        continue;
      if (!Modifier.isFinal(modifiers))
        continue;
      if (!field.getName().startsWith("SZ_"))
        continue;
      try {
        @SuppressWarnings("unchecked")
        Set<SzFlag> flags = (Set<SzFlag>) field.get(null);
        long value = SzFlag.toLong(flags);
        this.enumsMap.put(field.getName(), value);
        this.setsMap.put(field.getName(), flags);

      } catch (IllegalAccessException e) {
        fail("Got exception in reflection.", e);
      }
    }
  }

  private List<Arguments> getFlagsMappings() {
    List<Arguments> results = new ArrayList<>(this.flagsMap.size());
    this.flagsMap.forEach((key, value) -> {
      results.add(Arguments.of(key, value));
    });
    return results;
  }

  private List<Arguments> getEnumMappings() {
    List<Arguments> results = new ArrayList<>(this.enumsMap.size());
    this.enumsMap.forEach((key, value) -> {
      results.add(Arguments.of(key, value));
    });
    return results;
  }

  private List<SzFlag> getEnumFlags() {
    return Arrays.asList(SzFlag.values());
  }

  @ParameterizedTest
  @MethodSource("getFlagsMappings")
  public void testPrimitiveFlag(String flagName, long value) {
    assertTrue(this.enumsMap.containsKey(flagName),
        "Enum flag constant (" + flagName + ") not found for "
            + "native flag constant.");
    Long enumValue = this.enumsMap.get(flagName);
    assertEquals(value, enumValue,
        "Enum flag constant (" + flagName + ") has different value ("
            + hexFormat(enumValue) + ") than native flag constant: "
            + hexFormat(value));
  }

  @ParameterizedTest
  @MethodSource("getEnumMappings")
  public void testEnumFlag(String name, long value) {
    if (name.endsWith("_ALL_FLAGS")) {
      int length = name.length();
      String prefix = name.substring(0, length - "_ALL_FLAGS".length());
      SzFlagUsageGroup group = null;
      try {
        group = SzFlagUsageGroup.valueOf(prefix);

      } catch (Exception e) {
        fail("Failed to get SzFlagUsageGroup for ALL_FLAGS set: "
            + "set=[ " + name + "], group=[ " + prefix + "]");
      }
      long groupValue = SzFlag.toLong(group.getFlags());
      assertEquals(value, groupValue,
          "Value for group (" + group + ") has a different "
              + "primitive long value (" + hexFormat(groupValue)
              + ") than expected (" + hexFormat(value) + "): " + name);
      Set<SzFlag> set = this.setsMap.get(name);
      assertNotNull(set, "Failed to get Set of SzFlag for field: " + name);
      assertEquals(group.getFlags(), set,
          "The set of all flags for the group (" + group + ") is not "
              + "equal to the set defined for the declared constant ("
              + name + ").  expected=[ " + SzFlag.toString(group.getFlags())
              + " ], actual=[ " + SzFlag.toString(set) + " ]");
    } else {
      assertTrue(this.flagsMap.containsKey(name),
          "Primitive long flag constant not found for "
              + "enum flag constant: " + name);
      Long flagsValue = this.flagsMap.get(name);
      assertEquals(value, flagsValue,
          "Flag constant (" + name + ") has a different primitive "
              + "long value (" + hexFormat(flagsValue)
              + ") than enum flag constant (" + name + "): "
              + hexFormat(value));
    }
  }

  @ParameterizedTest
  @MethodSource("getEnumFlags")
  void testGetGroups(SzFlag flag) {
    Set<SzFlagUsageGroup> groups = flag.getGroups();
    assertNotNull(groups, "Groups for flag should not be null: " + flag);
    for (SzFlagUsageGroup group : groups) {
      Set<SzFlag> flags = group.getFlags();
      assertNotNull(flags, "Flags for group should not be null: " + group);
      assertTrue(flags.contains(flag),
          "Flag (" + flag + ") has group (" + group + ") but the "
              + "group does not have the flag.  groupsForFlag=[ " + groups
              + " ], flagsForGroup=[ " + flags + "]");
    }
  }

  private List<Arguments> getSetToLongParams() {
    List<Arguments> results = new ArrayList<>();
    results.add(Arguments.of(null, 0L));
    results.add(Arguments.of(EnumSet.noneOf(SzFlag.class), 0L));
    results.add(Arguments.of(EnumSet.of(SZ_NO_FLAGS), 0L));

    SzFlag[] flags = SzFlag.values();
    int start = 0;
    for (int loop = 0; loop < 3; loop++) {
      for (int count = 1; count < 10; count++) {
        // initialize the set
        EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);

        // initialize the composite value
        long value = 0L;

        // reset the start if need be
        if (start > flags.length - count) {
          start = count;
        }
        int end = start + count;

        // loop through flags and add them to the set
        for (int index = start; index < end; index++, start++) {
          SzFlag flag = flags[index];
          set.add(flag);
          value |= flag.toLong();
        }

        results.add(Arguments.of(set, value));
      }
    }

    return results;
  }

  private List<Arguments> getSetToStringParams() {
    List<Arguments> results = new ArrayList<>();
    results.add(Arguments.of(
        null,
        "{ NONE } [0000 0000 0000 0000]"));
    results.add(Arguments.of(
        EnumSet.noneOf(SzFlag.class),
        "{ NONE } [0000 0000 0000 0000]"));
    results.add(Arguments.of(
        EnumSet.of(SZ_NO_FLAGS),
        "SZ_NO_FLAGS [0000 0000 0000 0000]"));

    StringBuilder sb = new StringBuilder(300);
    SzFlag[] flags = SzFlag.values();
    int start = 0;
    for (int loop = 0; loop < 3; loop++) {
      for (int count = 1; count < 10; count++) {
        // clear the string builder
        sb.delete(0, sb.length());

        // initialize the set
        EnumSet<SzFlag> set = EnumSet.noneOf(SzFlag.class);

        // initialize the composite value
        long value = 0L;

        // reset the start if need be
        if (start > flags.length - count) {
          start = count;
        }

        String prefix = "";

        int end = start + count;

        // loop through flags and add them to the set
        for (int index = start; index < end; index++, start++) {
          SzFlag flag = flags[index];
          set.add(flag);
          value |= flag.toLong();
          sb.append(prefix);
          sb.append(flag.name());
          prefix = " | ";
        }
        sb.append(" [").append(hexFormat(value)).append("]");

        results.add(Arguments.of(set, sb.toString()));
      }
    }

    return results;
  }

  @ParameterizedTest
  @MethodSource("getSetToLongParams")
  void testSetToLong(Set<SzFlag> flagSet, long expected) {
    long actual = SzFlag.toLong(flagSet);
    assertEquals(expected, actual,
        "toLong(EnumSet<SzFlag>) returned " + hexFormat(actual)
            + " instead of " + hexFormat(expected) + ": " + flagSet);
  }

  @ParameterizedTest
  @MethodSource("getSetToStringParams")
  void testSetToString(Set<SzFlag> flagSet, String expected) {
    String actual = SzFlag.toString(flagSet);
    assertEquals(expected, actual,
        "toString(EnumSet<SzFlag>) did not return as expected: "
            + flagSet);
  }
}
