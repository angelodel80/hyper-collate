package nl.knaw.huygens.hypercollate.collator;

/*-
 * #%L
 * hyper-collate-core
 * =======
 * Copyright (C) 2017 - 2019 Huygens ING (KNAW)
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

import nl.knaw.huygens.hypercollate.model.SimpleTokenVertex;
import nl.knaw.huygens.hypercollate.model.TextNode;
import nl.knaw.huygens.hypercollate.model.TokenVertex;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class CollatedMatch {

  private final TextNode collatedNode;
  private int nodeRank;
  private final TokenVertex witnessVertex;
  private int vertexRank;
  private final Set<String> sigils = new HashSet<>();
  private final Map<String, List<Integer>> branchPaths = new HashMap<>();

  public CollatedMatch(TextNode collatedNode, TokenVertex witnessVertex) {
    this.collatedNode = collatedNode;
    this.witnessVertex = witnessVertex;
    sigils.add(witnessVertex.getSigil());
    branchPaths.put(witnessVertex.getSigil(), witnessVertex.getBranchPath());
    sigils.addAll(collatedNode.getSigils());
    for (String s : collatedNode.getSigils()) {
      branchPaths.put(s, collatedNode.getBranchPath(s));
    }
  }

  public TextNode getCollatedNode() {
    return collatedNode;
  }

  public TokenVertex getWitnessVertex() {
    return witnessVertex;
  }

  public boolean hasWitness(String sigil) {
    return sigils.contains(sigil);
  }

  public int getNodeRank() {
    return nodeRank;
  }

  public void setNodeRank(int nodeRank) {
    this.nodeRank = nodeRank;
  }

  public int getVertexRank() {
    return vertexRank;
  }

  public CollatedMatch setVertexRank(int vertexRank) {
    this.vertexRank = vertexRank;
    return this;
  }

  public Set<String> getSigils() {
    return sigils;
  }

  public List<Integer> getBranchPath(String s) {
    return branchPaths.get(s);
  }

  @Override
  public String toString() {
    Set<String> sigils = collatedNode instanceof TextNode
        ? collatedNode.getSigils()
        : this.sigils;
    String sigilString = sigils.stream().sorted().collect(joining(","));
    StringBuilder stringBuilder = new StringBuilder("<[")//
        .append(sigilString)//
        .append("]")//
        .append(nodeRank);

    StringBuilder vString = new StringBuilder();
    if (witnessVertex instanceof SimpleTokenVertex) {
      SimpleTokenVertex sv = (SimpleTokenVertex) witnessVertex;
      vString.append(sv.getSigil())//
          .append(sv.getIndexNumber());
    } else {
      vString.append(witnessVertex.getSigil()).append(witnessVertex.getClass().getSimpleName());
    }
    return stringBuilder.append(",").append(vString.toString()).append(">").toString();
  }

  @Override
  public int hashCode() {
    return collatedNode.hashCode() * witnessVertex.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CollatedMatch) {
      CollatedMatch other = (CollatedMatch) obj;
      return collatedNode.equals(other.collatedNode) && witnessVertex.equals(other.witnessVertex);

    } else
      return false;
  }
}
