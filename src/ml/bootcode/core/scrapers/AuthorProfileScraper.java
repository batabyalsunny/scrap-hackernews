package ml.bootcode.core.scrapers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ml.bootcode.entities.Author;

/**
 * @author sunnyb
 *
 */
public class AuthorProfileScraper {

	// Regular expression rules to scrap.
	private final static String SKIP_CHARS_REGX = ".*?";
	private final static String USERNAME_REGX = "class=\"hnuser\">(?<userName>.*?)</a>";
	private final static String CREATED_AT_REGX = "created:" + SKIP_CHARS_REGX + "<a" + SKIP_CHARS_REGX
			+ ">(?<createdAt>.+?)</a>";
	private final static String KARMA_REGX = "karma:" + SKIP_CHARS_REGX + "<td>(?<karma>.+?)</td>";
	private final static String ABOUT_REGX = "about:" + SKIP_CHARS_REGX + "<td>(?<about>.+?)</td>";

	// Date format.
	private final static String DEFAULT_DATE_FORMAT = "MMMM dd, yyyy";

	private URL url;

	/**
	 * @param url
	 * @throws MalformedURLException
	 */
	public AuthorProfileScraper(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public Author scrap() throws IOException, ParseException {

		// Regular expression to scrap the author page.
		String regex = USERNAME_REGX + SKIP_CHARS_REGX + CREATED_AT_REGX + SKIP_CHARS_REGX + KARMA_REGX
				+ SKIP_CHARS_REGX + ABOUT_REGX;

		// Open the url connection.
		URLConnection con = url.openConnection();

		// Read the page HTML as string.
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String html = in.lines().map(line -> {
			return line.trim();
		}).collect(Collectors.joining());

		// Regex pattern and matcher.
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);

		// Prepare the auhor object.
		Author author = new Author();

		// If match found.
		if (matcher.find()) {
			author.setUserName(matcher.group("userName"));
//			author.setCreatedAt(new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(matcher.group("createdAt")));
			author.setKarma(Integer.parseInt(matcher.group("karma").trim()));
			author.setAbout(matcher.group("about"));
		}

		return author;
	}
}
