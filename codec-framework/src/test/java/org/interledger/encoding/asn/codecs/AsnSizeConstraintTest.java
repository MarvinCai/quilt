package org.interledger.encoding.asn.codecs;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * Unit tests for {@link AsnSizeConstraint}.
 */
public class AsnSizeConstraintTest {

  @Test
  public void testUnconstrained() {
    final AsnSizeConstraint constraint = AsnSizeConstraint.UNCONSTRAINED;

    assertThat(constraint.isUnconstrained(), is(true));
    assertThat(constraint.isFixedSize(), is(false));
    assertThat(constraint.getMin(), is(0));
    assertThat(constraint.getMax(), is(0));
  }

  @Test
  public void isFixedSize() {
    final AsnSizeConstraint constraint = new AsnSizeConstraint(1);

    assertThat(constraint.isUnconstrained(), is(false));
    assertThat(constraint.isFixedSize(), is(true));
    assertThat(constraint.getMin(), is(1));
    assertThat(constraint.getMax(), is(1));
  }


  @Test
  public void isFixedSizeOther() {
    final AsnSizeConstraint constraint = new AsnSizeConstraint(1, 1);

    assertThat(constraint.isUnconstrained(), is(false));
    assertThat(constraint.isFixedSize(), is(true));
    assertThat(constraint.getMin(), is(1));
    assertThat(constraint.getMax(), is(1));
  }

  @Test
  public void isNotFixedSize() {
    final AsnSizeConstraint constraint = new AsnSizeConstraint(1, 2);

    assertThat(constraint.isUnconstrained(), is(false));
    assertThat(constraint.isFixedSize(), is(false));
    assertThat(constraint.getMin(), is(1));
    assertThat(constraint.getMax(), is(2));
  }

  @Test
  public void equalsHashCode() {
    assertThat(AsnSizeConstraint.UNCONSTRAINED, is(AsnSizeConstraint.UNCONSTRAINED));
    assertThat(AsnSizeConstraint.UNCONSTRAINED, is(not(new AsnSizeConstraint(1))));

    assertThat(AsnSizeConstraint.UNCONSTRAINED.hashCode(), is(AsnSizeConstraint.UNCONSTRAINED.hashCode()));
    assertThat(AsnSizeConstraint.UNCONSTRAINED.hashCode(), is(not(new AsnSizeConstraint(1).hashCode())));
  }

}