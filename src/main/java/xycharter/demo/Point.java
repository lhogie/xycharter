package xycharter.demo;

public class Point
{
	public double x, y;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public static int x(long p)
	{
		return (int) (p & 0x00000000FFFFFFFFL);
	}

	public static long x(long p, int x)
	{
		p &= 0xFFFFFFFF00000000L;
		return p | x;
	}

	public static int y(long p)
	{
		return (int) (p >> 32);
	}

	public static long y(long p, int y)
	{
		long l = y;
		l <<= 32;
		p &= 0x00000000FFFFFFFFL;
		return p | l;
	}

	public static long p(int x, int y)
	{
		return y(x(0, x), y);
	}

	public static String sii(long p)
	{
		return "(" + x(p) + ", " + y(p) + ")";
	}

	public static String sif(long p)
	{
		return "(" + x(p) + ", " + yf(p) + ")";
	}

	public static String sff(long p)
	{
		return "(" + xf(p) + ", " + yf(p) + ")";
	}

	public static String sfi(long p)
	{
		return "(" + xf(p) + ", " + y(p) + ")";
	}

	public static float xf(long p)
	{
		return Float.intBitsToFloat(x(p));
	}

	public static long xf(long p, float x)
	{
		return x(p, Float.floatToIntBits(x));
	}

	public static float yf(long p)
	{
		return Float.intBitsToFloat(y(p));
	}

	public static long yf(long p, float y)
	{
		return y(p, Float.floatToIntBits(y));
	}

	public static void main(String[] args)
	{
		{
			long p = 0;
			System.out.println(sii(p));
			p = x(p, 22);
			System.out.println(sii(p));
			p = x(p, 654);
			System.out.println(sii(p));
			p = y(p, 4432);
			System.out.println(sii(p));
			p = y(p, 2);
			System.out.println(sii(p));
		}
		{
			long p = 0;
			System.out.println(sfi(p));
			p = xf(p, 22);
			System.out.println(sfi(p));
			p = xf(p, 654);
			System.out.println(sfi(p));
			p = y(p, 4432);
			System.out.println(sfi(p));
			p = y(p, 2);
			System.out.println(sfi(p));
		}
	}
}
