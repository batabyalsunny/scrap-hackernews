/**
 * 
 */
package ml.bootcode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ml.bootcode.core.scrapers.ArticleListScraper;
import ml.bootcode.entities.Article;

/**
 * Main application class.
 * 
 * @author sunnyb
 *
 */
public class ScrapHackernewsApp {

	// Url to scrap.
	private final static String URL = "https://news.ycombinator.com/";

	private static ArticleListScraper scraper;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Scraper instance.
		try {
			ScrapHackernewsApp.scraper = new ArticleListScraper(URL);
		} catch (MalformedURLException e) {
			System.out.println("Unable to reach " + URL);
		}

		ScrapHackernewsApp app = new ScrapHackernewsApp();

		// Check the first argument flag.
		switch (args[0]) {

		// List authors case.
		case "-a":
			app.listAuthors();
			break;

		// List stories case.
		case "-s":
			app.listArticles();
			break;

		default:
			// Edge case for no cmd line arg provided.
			System.out.println("What to list out? Please chose an option: -a for authors -s for stories.");
			break;
		}
	}

	/**
	 * Prints out user names and there Karma points from top page of.
	 */
	public void listAuthors() {

		List<Article> articles = new ArrayList<>();

		// Scrap and get data.
		try {
			articles = scraper.scrap(true);
		} catch (IOException | ParseException e) {
			System.out.println("There was some error while parsing the page content");
		}

		System.out.println("All authors and their karma points");
		System.out.println("====================================\n");

		// Print
		Iterator<Article> it = articles.iterator();
		while (it.hasNext()) {
			Article article = it.next();
			System.out.println("Author: " + article.getAuthor().getUserName());
			System.out.println("Karma Point: " + article.getAuthor().getKarma() + "\n");
		}
	}

	/**
	 * Lists all stories sorted by comment count.
	 */
	public void listArticles() {

		List<Article> articles = new ArrayList<>();

		// Scrap and get data.
		try {
			articles = scraper.scrap(false);
		} catch (IOException | ParseException e) {
			System.out.println("There was some error while parsing the page content");
		}

		// Sort the articles by comment count.
		Collections.sort(articles, new Comparator<Article>() {

			@Override
			public int compare(Article o1, Article o2) {
				return o1.getCommentCount().compareTo(o2.getCommentCount());
			}
		});

		System.out.println("All stories sorted by number of comments");
		System.out.println("==========================================\n");

		// Print.
		Iterator<Article> it = articles.iterator();
		while (it.hasNext()) {
			Article article = it.next();
			System.out.println("Title: " + article.getTitle());
			System.out.println("Comment Count: " + article.getCommentCount() + "\n");
		}
	}
}
