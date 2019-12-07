/**
 * 
 */
package ml.bootcode.core.scrapers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ml.bootcode.entities.Article;
import ml.bootcode.entities.Author;

/**
 * Scraper for the article list.
 * 
 * @author sunnyb
 *
 */
public class ArticleListScraper {

	// Regular expression rules to scrap.
	private final static String SKIP_CHARS_REGX = ".*?";
	private final static String RANK_REGX = "class=\"rank\">(?<rank>.*?)\\.</span>";
	private final static String LINK_REGX = "class=\"title\"><a href=\"(?<link>.*?)\"";
	private final static String TITLE_REGX = "class=\"storylink\">(?<title>.*?)</a>";
	private final static String SOURCE_REGX = "class=\"sitestr\">(?<source>.*?)</span>";
	private final static String SCORE_REGX = "class=\"subtext\">.+?class=\"score\".+?>(?<score>.+?) points</span>";
	private final static String AUTHOR_URL_REGX = "by <a href=\"(?<authorUrl>.+?)\"";
	private final static String AGE_REGX = "class=\"age\">.+?>(?<age>.+?) ago</a>";
	private final static String COMMENT_COUNT_REGX = "\\|" + SKIP_CHARS_REGX + "\\|" + SKIP_CHARS_REGX
			+ ">(?<commentCount>.*?)(&nbsp;comments|&nbsp;comment|discuss)</a>";

	// Page URL.
	private URL url;

	/**
	 * @param url
	 * @throws MalformedURLException
	 */
	public ArticleListScraper(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	/**
	 * Scraps the page.
	 * 
	 * @param withAuthor Whether to scrap authors also or not.
	 * @return List of articles.
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Article> scrap(Boolean withAuthor) throws IOException, ParseException {

		// Regular expression to scrap the whole page.
		String regex = RANK_REGX + SKIP_CHARS_REGX + LINK_REGX + SKIP_CHARS_REGX + TITLE_REGX + SKIP_CHARS_REGX
				+ SOURCE_REGX + SKIP_CHARS_REGX + SCORE_REGX + SKIP_CHARS_REGX + AUTHOR_URL_REGX + SKIP_CHARS_REGX
				+ AGE_REGX + SKIP_CHARS_REGX + COMMENT_COUNT_REGX;

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

		// Article list to return.
		List<Article> articles = new ArrayList<>();

		// Loop through the matches.
		while (matcher.find()) {

			Author author = null;

			// If with author then only scrap authors page.
			if (withAuthor) {

				// Get the author URL first.
				String authorUrl = url.toString() + matcher.group("authorUrl");

				// Scrap the author details.
				AuthorProfileScraper authorScraper = new AuthorProfileScraper(authorUrl);
				author = authorScraper.scrap();
			}

			// Comment count.
			Integer commentCount = 0;

			// if comment count is non empty string parse to int.
			if (matcher.group("commentCount") != null && !matcher.group("commentCount").isEmpty()) {
				commentCount = Integer.parseInt(matcher.group("commentCount"));
			}

			// Prepare the article object.
			Article article = new Article();
			article.setTitle(matcher.group("title"));
			article.setLink(matcher.group("link"));
			article.setRank(Integer.parseInt(matcher.group("rank")));
			article.setScore(Integer.parseInt(matcher.group("score")));
			article.setAuthor(author);
			article.setAge(matcher.group("age"));
			article.setCommentCount(commentCount);
			article.setSource(matcher.group("source"));

			articles.add(article);
		}

		return articles;
	}
}
