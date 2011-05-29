package net.sf.jcommon.util;

import java.util.*;

/** A permutation general class from which we can obtain a permutation
 * of a list, array or string. 
 * See <a href="http://beradrian.wordpress.com/2010/10/29/combinations-and-permutations">this article</a>.
 */
public class Permutation {
	
	/** The random generator used to generate indices of random permutations. */
	private static final Random RANDOM_GENERATOR = new Random();
	/** A mapping of indices in this permutation. 
	 * The {@code i}th element will be in the {@code indices[i]} position.
	 */
	private int[] indices;
	
	protected Permutation(int[] indices) {
		this.indices = indices;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(indices);
	}

	@Override
	public boolean equals(Object that) {
		return (that instanceof Permutation) && (Arrays.equals(indices, ((Permutation)that).indices));
	}

	@Override
	public String toString() {
		return Arrays.toString(indices);
	}

	public <T> List<T> permute(List<T> src) {
		return permute(src, new ArrayList<T>());
	}
		
	public <T> List<T> permute(List<T> src, List<T> dest) {
		dest.clear();
		for (int  i = 0; i < indices.length; i++) {
			dest.add(src.get(indices[i]));
		}
		return dest;
	}
	
	public String permute(CharSequence src) {
		return permute(src, new StringBuilder()).toString();
	}
		
	public StringBuilder permute(CharSequence src, StringBuilder dest) {
		dest.setLength(0);
		for (int  i = 0; i < indices.length; i++) {
			dest.append(src.charAt(indices[i]));
		}
		return dest;
	}

	public <T> T[] permute(T[] src, T[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public byte[] permute(byte[] src) {
		return permute(src, new byte[src.length]);
	}
	
	public byte[] permute(byte[] src, byte[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public short[] permute(short[] src) {
		return permute(src, new short[src.length]);
	}
	
	public short[] permute(short[] src, short[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public int[] permute(int[] src) {
		return permute(src, new int[src.length]);
	}
	
	public int[] permute(int[] src, int[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public long[] permute(long[] src) {
		return permute(src, new long[src.length]);
	}
	
	public long[] permute(long[] src, long[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public boolean[] permute(boolean[] src) {
		return permute(src, new boolean[src.length]);
	}
	
	public boolean[] permute(boolean[] src, boolean[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public char[] permute(char[] src) {
		return permute(src, new char[src.length]);
	}
	
	public char[] permute(char[] src, char[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public float[] permute(float[] src) {
		return permute(src, new float[src.length]);
	}
	
	public float[] permute(float[] src, float[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	public double[] permute(double[] src) {
		return permute(src, new double[src.length]);
	}
	
	public double[] permute(double[] src, double[] dest) {
		for (int  i = 0; i < indices.length; i++) {
			dest[i] = src[indices[i]];
		}
		return dest;
	}
	
	
	/** Creates a permutation from the given indices. */
	public static Permutation fromIndices(int[] indices) {
		return new Permutation(indices);
	}
	
	public static Permutation random(int n) {
		return random(n, RANDOM_GENERATOR);
	}
	
	public static Permutation random(int n, Random randomGenerator) {
		int[] r = new int[n];
		for (int  i = 0; i < r.length - 1; i++) {
			r[i] = randomGenerator.nextInt(n - i - 1);
		}
		r[r.length - 1] = 0;
		return fromRepresentation(r);
	}
	
	public static Iterator<Permutation> iterator(int n) {
		return new PermutationIterator(n);
	}

	public static Permutation getPermutation(int n, long k) {
		int[] r = new int[n];
		for (int i = 0; i < n - 1; i++) {
			r[n - 2 - i] = (int)(k % (i + 2));
			k /= i + 2;
		}
		r[n - 1] = 0;
		return fromRepresentation(r);
	}

	/** Creates a permutation from the given representation. */
	public static Permutation fromRepresentation(int[] r) {
		int m = 0;
		for (int  i = 0; i < r.length; i++) {
			if (r[i] >= 0)
				m++;
		}	
		int[] indices = new int[m];
		for (int  i = 0; i < indices.length; i++) {
			indices[i] = -1;
		}
		for (int  i = 0; i < r.length; i++) {
			if (r[i] >= 0) {
				indices[findAvailIndex(indices, r[i])] = i;
			}
		}
		return fromIndices(indices);
	}
	
	private static int findAvailIndex(int[] indices, int index) {
		int j = -1;
		for (int  i = 0; i < indices.length; i++) {
			if (indices[i] < 0)
				j++;
			if (j == index)
				return i;
		}
		return -1;
	}

	private static class PermutationIterator implements Iterator<Permutation> {

		private int[] representation;
		
		public PermutationIterator(int n) {
			if (n <= 0)
				throw new IllegalArgumentException("The permutation order must be bigger than 0");
			representation = new int[n];
			representation[n - 1] = -1;
		}
			
		@Override
		public boolean hasNext() {
			return representation[representation.length - 1] <= 0;
		}

		@Override
		public Permutation next() {
			Permutation nextPerm = null;
			switch(representation[representation.length - 1]) {
				case -1:
					for (int j = 0; j < representation.length; j++) {
						representation[j] = 0;
					}
				case 0:
					nextPerm = Permutation.fromRepresentation(representation);
					int i = 0;
					representation[i]++;
					while ((representation[i] > representation.length - i - 1) && (i < representation.length - 1)) {
						representation[i] = 0;
						representation[++i]++;
					}
					break;
				default:
					nextPerm = null;
			}
			return nextPerm;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove a permutation");
		}

	}

}
