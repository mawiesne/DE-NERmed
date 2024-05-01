package de.hhn.mi.commons;

import java.io.Serial;
import java.io.Serializable;

public final class Pair<A,B> implements Serializable {

  @Serial
  private static final long serialVersionUID = -1677511349876242280L;
  
  private final A first;
  private final B second;

  public A getFirst() {
    return first;
  }

  public B getSecond() {
    return second;
  }

  private Pair (A first, B second) {
    this.first = first;
    this.second = second;
  }

  public static <A,B> Pair<A,B> of (A first, B second) {
    return new Pair<>(first,second);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Pair other = (Pair) obj;
    if (this.first != other.first &&
            (this.first == null || !this.first.equals(other.first))) {
      return false;
    }
    return !(this.second != other.second &&
            (this.second == null || !this.second.equals(other.second)));
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + (this.first != null ? this.first.hashCode() : 0);
    hash = 37 * hash + (this.second != null ? this.second.hashCode() : 0);
    return hash;
  }


  @Override
  public String toString() {
    return "Pair{" +
            "first=" + first +
            ", second=" + second +
            '}';
  }
}