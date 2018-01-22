package nl.knaw.huygens.hypercollate.model;

/*-
 * #%L
 * hyper-collate-core
 * =======
 * Copyright (C) 2017 - 2018 Huygens ING (KNAW)
 * =======
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import org.antlr.v4.misc.OrderedHashMap;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.internal.Iterables;

import java.util.*;

public class CollationGraphAssert extends AbstractObjectAssert<CollationGraphAssert, CollationGraph> {

  /**
   * Creates a new <code>{@link CollationGraphAssert}</code> to make assertions on actual CollationGraph.
   *
   * @param actual the CollationGraph we want to make assertions on.
   */
  public CollationGraphAssert(CollationGraph actual) {
    super(actual, CollationGraphAssert.class);
  }

  @org.assertj.core.util.CheckReturnValue
  public CollationGraphAssert containsTextNodesMatching(TextNodeSketch... textNodeSketches) {
    Set<TextNodeSketch> actualTextNodeSketches = getActualTextNodeSketches();
    Set<TextNodeSketch> expectedTextNodeSketches = new HashSet<>(Arrays.asList(textNodeSketches));
    expectedTextNodeSketches.removeAll(actualTextNodeSketches);

    String errorMessage = "\nNo nodes found matching %s;\nNodes found: %s";
    if (!expectedTextNodeSketches.isEmpty()) {
      failWithMessage(errorMessage, expectedTextNodeSketches, actualTextNodeSketches);
    }

    return myself;
  }

  @org.assertj.core.util.CheckReturnValue
  public CollationGraphAssert doesNotContainTextNodesMatching(TextNodeSketch... textNodeSketches) {
    Set<TextNodeSketch> actualTextNodeSketches = getActualTextNodeSketches();
    List<TextNodeSketch> textNodeSketchList = Arrays.asList(textNodeSketches);
    Set<TextNodeSketch> unexpectedTextNodeSketches = new HashSet<>(textNodeSketchList);
    unexpectedTextNodeSketches.retainAll(actualTextNodeSketches);

    String errorMessage = "\nExpected %s not to match with any of %s, but found matches with %s";
    if (!unexpectedTextNodeSketches.isEmpty()) {
      failWithMessage(errorMessage, actualTextNodeSketches, textNodeSketchList, unexpectedTextNodeSketches);
    }

    return myself;
  }

  @org.assertj.core.util.CheckReturnValue
  public CollationGraphAssert containsOnlyTextNodesMatching(TextNodeSketch... textNodeSketches) {
    Set<TextNodeSketch> actualTextNodeSketches = getActualTextNodeSketches();
    Set<TextNodeSketch> expectedTextNodeSketches = new HashSet<>(Arrays.asList(textNodeSketches));
    Iterables.instance().assertContainsAll(info, actualTextNodeSketches, expectedTextNodeSketches);
    return myself;
  }

  @org.assertj.core.util.CheckReturnValue
  public CollationGraphAssert containsMarkupNodesMatching(MarkupNodeSketch... markupNodeSketches) {
    Set<MarkupNodeSketch> actualMarkupNodeSketches = getActualMarkupNodeSketches();
    Set<MarkupNodeSketch> expectedMarkupNodeSketches = new HashSet<>(Arrays.asList(markupNodeSketches));
    Iterables.instance().assertContainsAll(info, actualMarkupNodeSketches, expectedMarkupNodeSketches);
    return myself;
  }

  public static class TextNodeSketch {
    Map<String, String> witnessTokenSegments = new OrderedHashMap<>();

    public TextNodeSketch withWitnessSegmentSketch(String sigil, String mergedtokens) {
      witnessTokenSegments.put(sigil, mergedtokens);
      return this;
    }

    @Override
    public int hashCode() {
      return witnessTokenSegments.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof TextNodeSketch
          && ((TextNodeSketch) obj).witnessTokenSegments.equals(witnessTokenSegments);
    }

    @Override
    public String toString() {
      return "[\n" + witnessTokenSegments.keySet()//
          .stream()//
          .map(s -> String.format("  (%s:%s)", s, witnessTokenSegments.get(s).replace("\n", "\\n")))//
          .collect(joining(",\n"))//
          + "\n]";
    }
  }

  public static TextNodeSketch textNodeSketch() {
    return new TextNodeSketch();
  }

  public TextNodeSketch toTextNodeSketch(TextNode node) {
    TextNodeSketch textNodeSketch = textNodeSketch();
    node.getSigils().forEach(s ->
        textNodeSketch.withWitnessSegmentSketch(s, ((MarkedUpToken) node.getTokenForWitness(s)).getContent())
    );
    return textNodeSketch;
  }

  public MarkupNodeSketch toMarkupNodeSketch(MarkupNode node) {
    return markupNodeSketch(node.getSigil(), node.getMarkup().getTagName(), node.getMarkup().getAttributeMap());
  }

  /**
   * An entry point for CollationGraphAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myCollationGraph)</code> and get specific assertion with code completion.
   *
   * @param actual the CollationGraph we want to make assertions on.
   * @return a new <code>{@link CollationGraphAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static CollationGraphAssert assertThat(CollationGraph actual) {
    return new CollationGraphAssert(actual);
  }

  public static class MarkupNodeSketch {
    private final String sigil;
    private final String tag;
    private final Map<String, String> attributes;

    MarkupNodeSketch(String sigil, String tag, Map<String, String> attributes) {
      this.sigil = sigil;
      this.tag = tag;
      this.attributes = attributes;
    }

    @Override
    public int hashCode() {
      return sigil.hashCode() + tag.hashCode() + attributes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof MarkupNodeSketch
          && ((MarkupNodeSketch) obj).sigil.equals(sigil)
          && ((MarkupNodeSketch) obj).tag.equals(tag)
          && ((MarkupNodeSketch) obj).attributes.equals(attributes);
    }
  }

  public static MarkupNodeSketch markupNodeSketch(String sigil, String tag, Map<String, String> attributes) {
    return new MarkupNodeSketch(sigil, tag, attributes);
  }

  public static MarkupNodeSketch markupNodeSketch(String sigil, String tag) {
    return markupNodeSketch(sigil, tag, new HashMap<>());
  }

  private Set<TextNodeSketch> getActualTextNodeSketches() {
    return actual.traverseTextNodes()//
        .stream()//
        .filter(n -> !n.getSigils().isEmpty())//
        .map(this::toTextNodeSketch)//
        .collect(toSet());
  }

  private Set<MarkupNodeSketch> getActualMarkupNodeSketches() {
    return actual.getMarkupNodeStream()//
        .map(this::toMarkupNodeSketch)//
        .collect(toSet());
  }

}
