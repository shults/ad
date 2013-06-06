package ad;

/**
 *
 * @author shults
 */
public class Ad
{

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		double a = 0;
		double g = 0;
		double nslab = 1;
		double t = 1;
		int M = 32;
		try {
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {
					case "-h":
						throw new Exception(getHelpMsg());
					case "-a":
						a = Double.parseDouble(args[++i]);
						if (a > 1 || a < 0)
							throw new CliException("Albedo (-a) cannot be less then 0 and more than 1");
						continue;
					case "-g":
						g = Double.parseDouble(args[++i]);
						if (g < -1 || g > 1)
							throw new CliException("Anisotropy (-g) cannot be less then -1 and more than 1");
						continue;
					case "-nslab":
						nslab = Double.parseDouble(args[++i]);
						if (nslab < 0)
							throw new CliException("Refraction index (-nslab) cannot be less than zero");
						continue;
					case "-t":
						t = Double.parseDouble(args[++i]);
						if (t < 0)
							throw new CliException("Optical thickness (-t) cannot be less than zero");
						continue;
					case "-m":
						M = Integer.parseInt(args[++i]);
						if (M < 4 || M > 64)
							throw new CliException("Number of qudrature (-m) points must be in range 4..64");
						continue;
					default:
						throw new CliException("Argument " + args[i] + " not found");		
				}
			}
			if (a == 0)
				throw new CliException("Albedo (-a) cannot be equal to zero");
			Application.run(a, g, t, nslab, M);
		} catch (CliException e) {
			System.out.println();
			System.err.println(e.getMessage());
			System.out.println();
			System.out.println(getHelpMsg());
		} catch (Exception e) {
			System.out.println();
			System.err.println(e.getMessage());
		}
		System.out.println("\n\nLogger info:");
		Logger.printMessages();
	}

	private static String getHelpMsg()
	{
		String optinsFormat = "\t%s\t\t%s";
		return "This program find reflection and transmission from optical properties\n\n"
				+ "Usage: java -jar Ad [options]\n\n"
				+ "Options:\n"
				+ String.format(optinsFormat, "-h", "display help\n")
				+ String.format(optinsFormat, "-a #", "set albedo (0..1)\n")
				+ String.format(optinsFormat, "-g #", "set aisotropy (-1..1)\n")
				+ String.format(optinsFormat, "-nslab #", "\bset refraction index of medium\n")
				+ String.format(optinsFormat, "-t #", "set optical thickness\n")
				+ String.format(optinsFormat, "-m #", "set number of quadrature points [4..64]\n");
	}
}
