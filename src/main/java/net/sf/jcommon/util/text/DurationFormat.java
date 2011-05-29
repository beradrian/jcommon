package net.sf.jcommon.util.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

@SuppressWarnings("serial")
public class DurationFormat extends Format {

	private enum Format {
		HHH(60 * 60 * 1000), HH(60 * 60 * 1000, 24, 2), H(60 * 60 * 1000, 24, 1),
		hhh(60 * 60 * 1000), hh(60 * 60 * 1000, 12, 2), h(60 * 60 * 1000, 12, 1),
		mmm(60 * 1000, 1, 1), mm(60 * 1000, 60, 2), m(60 * 1000, 60, 1), 
		sss(1000, 1, 1), ss(1000, 60, 2), s(1000, 60, 1),
		SSSS(1, 1, 1), SSS(1, 1000, 3), SS(1, 1000, 2), S(1, 1000, 1);
		
		private int division, upper, minChars;
		
		private Format(int division, int upper, int minChars) {
			this.division = division;
			this.upper = upper;
			this.minChars = minChars;
		}

		Format(int division) {
			this(division, 1, 1);
		}
		
		Format() {
			this(1, 1, 1);
		}
		
		public String format(long value) {
			value /= division;
			if (upper != 1)
				value %= upper;
			StringBuilder s = new StringBuilder().append(value);
			for (int i = s.length(); i < minChars; i++)
				s.insert(0, '0');
			return s.toString();
		}
	}
	
	private static DurationFormat DEFAULT_FORMAT = new DurationFormat("HHH:mm:ss.SSS");
	
	private String pattern;
	
	public DurationFormat() {
		this("hh:mm:ss.SS");
	}

	public DurationFormat(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if (obj instanceof Number) {
			long valInMs = ((Number)obj).longValue();
			boolean negative = false;
			if (valInMs < 0) {
				valInMs = -valInMs;
				negative = true;
			}
			StringBuilder s = new StringBuilder(pattern);
			for (Format f : Format.values()) {
				int idx = s.indexOf(f.name());
				if (idx >= 0) {
					s.replace(idx, idx + f.name().length(), f.format(valInMs));
				}
			}
			if (negative)
				s.insert(0, "-");
			return toAppendTo.append(s.toString());
		}
		return toAppendTo;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		throw new IllegalArgumentException("Method not implemented");
	}

	public static String format(Number duration) {
		return DEFAULT_FORMAT.format(duration, new StringBuffer(), null).toString();
	}

	public static String format(Number duration, String pattern) {
		return new DurationFormat(pattern).format(duration, new StringBuffer(), null).toString();
	}
}
